package no.difi.filevalidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;

@Component
public class Validator {
    private final Environment environment;

    @Autowired
    public Validator(final Environment environment){

        this.environment = environment;
    }

    private Resource[] getXSDFileNames() throws IOException {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        return resolver.getResources("/xml/*");
    }

    public boolean validate(MultipartFile file, final RedirectAttributes redirectAttributes) throws IOException {

        final Resource[] resources = getXSDFileNames();
        InputStream stream = file.getInputStream();
        if(!validateXML(stream, redirectAttributes)){
            redirectAttributes.addFlashAttribute("message", environment.getProperty("validation.error.xml"));
            stream.close();
            return false;
        }else{
            stream.close();
            stream = file.getInputStream();
        }
        if(!validateXMLSchemas(resources, stream, redirectAttributes)){
            redirectAttributes.addFlashAttribute("message", environment.getProperty("validation.error.xsd"));
            stream.close();
            return false;
        }
        redirectAttributes.addFlashAttribute("message", environment.getProperty("validation.ok"));
        stream.close();
        return true;
    }

    private boolean validateXML(final InputStream stream, final RedirectAttributes redirectAttributes) throws IOException {

        if(stream == null){
            return false;
        }
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        final DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            //TODO: Log stacktrace to file
            e.printStackTrace();
            return false;
        }
        try {
            builder.parse(stream);
        } catch (SAXException e) {
            //TODO: Log stacktrace to file
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return false;
        } catch (IOException e) {
            //TODO: Log stacktrace to file
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return false;
        }

        return true;
    }

    private boolean validateXMLSchemas(final Resource[] xsdResources, final InputStream stream, final RedirectAttributes redirectAttributes) throws IOException {

        return validateXMLSchema(xsdResources[0].getInputStream(), stream, redirectAttributes, xsdResources);
    }

    private boolean validateXMLSchema(final InputStream xsd, final InputStream stream, final RedirectAttributes redirectAttributes, final Resource[] xsdResources) throws IOException {

        StreamSource[] xsds = generateStreamSourceFromXsdPaths(xsdResources);

        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsds);
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(stream));
            return true;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return false;
        }
    }

    private static StreamSource[] generateStreamSourceFromXsdPaths(final Resource[] resources)throws IOException{

        StreamSource[] sortedArray = new StreamSource[resources.length-1];


        int i=0;
        for (Resource resource : resources) {
            if(resource.getFilename().contains("xml.xsd")){
                i=0;
            }else if(resource.getFilename().contains("xmldsig-core-schema.xsd")){
                i=1;
            }else if(resource.getFilename().contains("xenc-schema.xsd")){
                i=2;
            }else if(resource.getFilename().contains("saml-schema-assertion-2.0.xsd")){
                i=3;
            }else if(resource.getFilename().contains("saml-schema-metadata-2.0.xsd")){
                i=4;
            }else if(resource.getFilename().contains("difi-saml-schema-metadata-2.0.xsd")){
                i=5;
            }

            sortedArray[i] = new StreamSource((resource.getInputStream()));
        }

        return sortedArray;
    }
}
