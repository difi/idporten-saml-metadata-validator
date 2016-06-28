package no.difi.service;

import no.difi.domain.Message;
import no.difi.domain.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
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

@Service
public class ValidatorService {

    private static final String METADATA_XSDS = "metadata.xsds";
    private static final String RESOURCES = "/xml/*";

    private final Environment environment;

    @Autowired
    public ValidatorService(final Environment environment) {
        this.environment = environment;
    }

    public ValidationResult validate(final MultipartFile file) throws IOException {
        InputStream stream = file.getInputStream();
        final ValidationResult validationResult = validateXML(stream);
        if (!validationResult.getValid()) {
            stream.close();
            return validationResult;
        } else {
            stream = resetInputStream(file, stream);
        }
        return validateXMLSchema(stream);
    }

    private InputStream resetInputStream(final MultipartFile file, final InputStream stream) throws IOException {
        stream.close();
        return file.getInputStream();
    }

    private Resource[] getXsds() throws IOException {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        return resolver.getResources(RESOURCES);
    }

    private ValidationResult validateXML(final InputStream stream) throws IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.newDocumentBuilder().parse(stream);
        } catch (ParserConfigurationException e) {
            //TODO: Log stacktrace to file
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_GENERAL_ERROR.key())).result(e.getMessage()).build();
        } catch (SAXException e) {
            //TODO: Log stacktrace to file
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_ERROR_XML.key())).result(e.getMessage()).build();
        } catch (IOException e) {
            //TODO: Log stacktrace to file
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_GENERAL_ERROR.key())).result(e.getMessage()).build();
        } finally {
            stream.close();
        }
        return ValidationResult.builder().valid(true).message(environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key())).result(environment.getRequiredProperty(Message.VALIDATION_OK_RESULT.key())).build();
    }

    private ValidationResult validateXMLSchema(final InputStream stream) throws IOException {

        final Resource[] xsdResources = getXsds();
        final StreamSource[] xsds = generateStreamSourceFromXsdResources(xsdResources);

        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            final Schema schema = schemaFactory.newSchema(xsds);
            final javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(stream));

            return ValidationResult.builder().valid(true).message(environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key())).result(environment.getRequiredProperty(Message.VALIDATION_OK_RESULT.key())).build();
        } catch (org.xml.sax.SAXException ex) {
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_ERROR_XSD.key())).result(ex.getMessage()).build();
        } finally {
            stream.close();
        }
    }

    private StreamSource[] generateStreamSourceFromXsdResources(final Resource[] resources) throws IOException {

        final List<String> xsds = Arrays.asList(environment.getRequiredProperty(METADATA_XSDS).split("\\s*,\\s*"));
        final StreamSource[] sortedArray = new StreamSource[resources.length];

        for (Resource resource : resources) {
            sortedArray[xsds.indexOf(resource.getFilename())] = new StreamSource((resource.getInputStream()));
        }

        return sortedArray;
    }
}
