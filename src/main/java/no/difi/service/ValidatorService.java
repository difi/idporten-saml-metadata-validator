package no.difi.service;

import no.difi.domain.Message;
import no.difi.domain.ValidationResult;
import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.xml.parse.BasicParserPool;
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
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
        String xml = org.apache.commons.io.IOUtils.toString(file.getInputStream(), "UTF-8");

        final ValidationResult validationResult = validateXMLSyntax(xml);
        return validationResult.getValid() ? validateXMLSchema(xml) : validationResult;
    }

    private ValidationResult validateXMLSyntax(final String xml) throws IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (ParserConfigurationException e) {
            //TODO: Log stacktrace to file
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_GENERAL_ERROR.key())).result(e.getMessage()).build();
        } catch (SAXException e) {
            //TODO: Log stacktrace to file
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_ERROR_XML.key())).result(e.getMessage()).build();
        } catch (IOException e) {
            //TODO: Log stacktrace to file
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_IO_ERROR.key())).result(e.getMessage()).build();
        }
        return ValidationResult.builder().valid(true).message(environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key())).result(environment.getRequiredProperty(Message.VALIDATION_OK_RESULT.key())).build();
    }

    private ValidationResult validateXMLSchema(final String xml) throws IOException {
        final StreamSource source = new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        try {
            final Schema schema = SAMLSchemaBuilder.getSAML11Schema();
            org.opensaml.xml.parse.ParserPool parser = new BasicParserPool();
            parser.setSchema(schema);

            final Validator validator = schema.newValidator();
            validator.validate(source);

        } catch (SAXException e) {
            return ValidationResult.builder().valid(false).message(environment.getRequiredProperty(Message.VALIDATION_ERROR_XSD.key())).result(e.getMessage()).build();
        }

        return ValidationResult.builder().valid(true).message(environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key())).result(environment.getRequiredProperty(Message.VALIDATION_OK_RESULT.key())).build();
    }
}
