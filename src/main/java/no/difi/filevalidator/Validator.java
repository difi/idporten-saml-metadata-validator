package no.difi.filevalidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.Arrays;
import java.util.List;

@Component
public class Validator {

    private static final String VALIDATION_ERROR_XML = "validation.error.xml";
    private static final String VALIDATION_ERROR_XSD = "validation.error.xsd";
    private static final String VALIDATION_OK_MESSAGE = "validation.ok.message";
    private static final String VALIDATION_OK_RESULT = "validation.ok.result";
    private static final String METADATA_XSDS = "metadata.xsds";
    private static final String RESOURCES = "/xml/*";

    private final Environment environment;

    private String message;
    private String result;

    @Autowired
    public Validator(final Environment environment){

        this.environment = environment;
        setMessage("");
        setResult("");
    }

    private Resource[] getXsds() throws IOException {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        return resolver.getResources(RESOURCES);
    }

    public boolean validate(final MultipartFile file) throws IOException {

        InputStream stream = file.getInputStream();
        if(!validateXML(stream)){
            setMessage(environment.getProperty(VALIDATION_ERROR_XML));
            stream.close();
            return false;
        }else{
            stream = resetInputStream(file, stream);
        }
        if(!validateXMLSchema(stream)){
            setMessage(environment.getProperty(VALIDATION_ERROR_XSD));
            stream.close();
            return false;
        }
        setMessage(environment.getProperty(VALIDATION_OK_MESSAGE));
        setResult(environment.getProperty(VALIDATION_OK_RESULT));
        stream.close();
        return true;
    }

    private InputStream resetInputStream(final MultipartFile file, final InputStream stream) throws IOException {
        stream.close();
        return file.getInputStream();
    }

    private boolean validateXML(final InputStream xml) throws IOException {

        if(xml == null){
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
            builder.parse(xml);
        } catch (SAXException e) {
            //TODO: Log stacktrace to file
            setResult(e.getMessage());
            return false;
        } catch (IOException e) {
            //TODO: Log stacktrace to file
            setResult(e.getMessage());
            return false;
        }

        return true;
    }

    private boolean validateXMLSchema(final InputStream xml) throws IOException {

        final Resource[] xsdResources = getXsds();
        final StreamSource[] xsds = generateStreamSourceFromXsdResources(xsdResources);


            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            final Schema schema = schemaFactory.newSchema(xsds);
            final javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        } catch (org.xml.sax.SAXException ex) {
            setResult(ex.getMessage());
            return false;
        }
    }

    private StreamSource[] generateStreamSourceFromXsdResources(final Resource[] resources)throws IOException{

        final List<String> xsds = Arrays.asList(environment.getProperty(METADATA_XSDS).split("\\s*,\\s*"));
        final StreamSource[] sortedArray = new StreamSource[resources.length];

        for (Resource resource : resources) {
            sortedArray[xsds.indexOf(resource.getFilename())] = new StreamSource((resource.getInputStream()));
        }

        return sortedArray;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }
}
