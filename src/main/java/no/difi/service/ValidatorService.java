package no.difi.service;

import no.difi.domain.DetailsStatus;
import no.difi.domain.Message;
import no.difi.domain.ValidationResult;
import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

        ValidationResult validationResult = validateXMLSyntax(xml);
        validationResult = validationResult.getValid() ? validateXMLSchema(xml) : validationResult;
        return validationResult;
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
        ValidationResult.Builder validationResult = ValidationResult.builder();
        boolean isValid = true;

        try {
            final Schema schema = SAMLSchemaBuilder.getSAML11Schema();
            final BasicParserPool parserPool = new BasicParserPool();
            parserPool.setNamespaceAware(true);
            parserPool.setSchema(schema);

            final Document doc = parserPool.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            final Element element = doc.getDocumentElement();
            final String entityID = element.getAttributes().getNamedItem("entityID").getNodeValue();
            if (entityID.isEmpty() || entityID.equals("null")) {
                isValid = false;
                validationResult.details(environment.getRequiredProperty(DetailsStatus.MISSING_ENTITY.key()));
            }


            schema.newValidator().validate(source);

        } catch (SAXException e) {
            return validationResult.valid(false).message(environment.getRequiredProperty(Message.VALIDATION_ERROR_XSD.key())).result(e.getMessage()).build();
        } catch (XMLParserException e) {
            if (!xml.contains("LogoutService")) {
                validationResult.details(environment.getRequiredProperty(DetailsStatus.MISSING_LOGOUT_URL.key()));
            }
            if (!xml.contains("AssertionConsumerService")) {
                validationResult.details(environment.getRequiredProperty(DetailsStatus.MISSING_ASSERTION_CONSUMER_URL.key()));
            }
            return validationResult.valid(false).message(environment.getRequiredProperty(Message.VALIDATION_FAILED.key())).result(e.getMessage()).build();
        }

        if (isValid) {
            return validationResult
                    .valid(true)
                    .message(environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key()))
                    .result(environment.getRequiredProperty(Message.VALIDATION_OK_RESULT.key()))
                    .build();
        }
        else {
            return validationResult
                    .valid(false)
                    .message(environment.getRequiredProperty(Message.VALIDATION_FAILED.key()))
                    .build();
        }
    }
}
