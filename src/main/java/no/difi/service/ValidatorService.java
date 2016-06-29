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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
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
        final ValidationResult validationResult = validateXMLSyntax(file.getInputStream());
        return validationResult.getValid() ? validateXMLSchema(file.getInputStream()) : validationResult;
    }

    private Resource[] getXsds() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(RESOURCES);
    }

    private ValidationResult validateXMLSyntax(final InputStream stream) throws IOException {
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
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_IO_ERROR.key())).result(e.getMessage()).build();
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
            final javax.xml.validation.Validator validator = schemaFactory.newSchema(xsds).newValidator();
            validator.validate(new StreamSource(stream));

            return ValidationResult.builder().valid(true).message(environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key())).result(environment.getRequiredProperty(Message.VALIDATION_OK_RESULT.key())).build();
        } catch (SAXException ex) {
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
