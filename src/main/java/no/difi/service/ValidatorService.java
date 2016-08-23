package no.difi.service;

import no.difi.domain.*;
import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static no.difi.domain.DetailsStatus.ERROR;
import static no.difi.domain.DetailsStatus.INFO;

@Service
public class ValidatorService {
    private static String extSchema = "/xml/difi-saml-schema-metadata-2.0.xsd";

    private final Environment environment;

    @Autowired
    public ValidatorService(final Environment environment) {
        this.environment = environment;
    }

    public ValidationResult validate(final MultipartFile file) throws IOException {
        String xml = org.apache.commons.io.IOUtils.toString(file.getInputStream(), "UTF-8");

        ValidationResult validationResult = validateXMLSyntax(xml);
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
        ValidationResult.Builder validationResult = ValidationResult.builder().valid(true);

        try {
            final Schema schema = SAMLSchemaBuilder.getSAML11Schema();
            SAMLSchemaBuilder.addExtensionSchema(extSchema);

            final BasicParserPool parserPool = new BasicParserPool();
            parserPool.setNamespaceAware(true);
            parserPool.setSchema(schema);

            final Document doc = parserPool.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            final Element element = doc.getDocumentElement();

            validationResult = validateEntityId(validationResult, element);
            validationResult = valdateAssertionUrl(validationResult, doc);
            validationResult = validateLogoutLocationUrl(validationResult, doc);
            validationResult = validateLogoutResponseLocationUrl(validationResult, doc);

            schema.newValidator().validate(source);
        } catch (SAXException e) {
            return validationResult.valid(false).message(environment.getRequiredProperty(Message.VALIDATION_ERROR_XSD.key())).result(e.getMessage()).build();
        } catch (XMLParserException e) {
            if (!xml.contains("LogoutService")) {
                validationResult.details(
                        DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.logouturl")).status(ERROR).build());
            }
            if (!xml.contains("AssertionConsumerService")) {
                validationResult.details(
                        DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.assertionconsumerurl")).status(ERROR).build());
            }
            return validationResult.valid(false).message(environment.getRequiredProperty(Message.VALIDATION_FAILED.key())).result(e.getMessage()).build();
        }

        if (validationResult.isValid()) {
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

    private ValidationResult.Builder valdateAssertionUrl(ValidationResult.Builder validationResult, Document document) {
        final String location = getElementAttributeValue(document, "AssertionConsumerService", "Location");
        return validateElement(validationResult, location, "assertionconsumerurl");
    }

    private ValidationResult.Builder validateLogoutLocationUrl(ValidationResult.Builder validationResult, Document document) {
        final String location = getElementAttributeValue(document, "SingleLogoutService", "Location");
        return validateElement(validationResult, location, "logouturl");
    }

    private ValidationResult.Builder validateLogoutResponseLocationUrl(ValidationResult.Builder validationResult, Document document) {
        final String location = getElementAttributeValue(document, "SingleLogoutService", "ResponseLocation");
        return validateElement(validationResult, location, "responselocationurl");
    }

    private ValidationResult.Builder validateElement(ValidationResult.Builder validationResult, String location, String detailsEnd) {
        if (location.length() == 0 || (!location.startsWith("http://") && !location.startsWith("https://"))) {
            return validationResult.valid(false)
                    .details(DetailsMessage.builder()
                            .status(DetailsStatus.ERROR)
                            .details(environment.getRequiredProperty("validation.param.missing." + detailsEnd)).build());
        }
        else if (location.startsWith("http://")) {
            return validationResult.valid(false)
                    .details(DetailsMessage.builder()
                            .status(DetailsStatus.WARNING)
                            .details(environment.getRequiredProperty("validation.param.http." + detailsEnd)).build());
        }
        else {
            return validationResult
                    .details(DetailsMessage.builder()
                            .status(DetailsStatus.INFO)
                            .details(environment.getRequiredProperty("validation.param.https." + detailsEnd)).build());
        }
    }

    private ValidationResult.Builder validateEntityId(ValidationResult.Builder validationResult, Element element) {
        final String entityID = element.getAttributes().getNamedItem("entityID").getNodeValue();
        if (entityID.isEmpty() || entityID.equals("null")) {
            return validationResult.valid(false).details(
                    DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.entityid")).status(ERROR).build());
        }
        else {
            return validationResult.details(
                    DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.valid.entityid")).status(INFO).build());
        }
    }

    private String getElementAttributeValue(Document document, String element, String attribute) {
        final Node node = getNode(document, element);
        return getAttributeValue(node, attribute);
    }

    private String getAttributeValue(Node acs, String attribute) {
        for (int i = 0; i < acs.getAttributes().getLength(); i++) {
            if (acs.getAttributes().item(i).getLocalName().equals(attribute)) {
                return acs.getAttributes().item(i).getNodeValue();
            }
        }
        return "";
    }

    private Node getNode(Document doc, String nodeName) {
        final Node spssoDescriptor = doc.getDocumentElement().getFirstChild();
        if (spssoDescriptor.getLocalName().equals("SPSSODescriptor")) {
            Node node = spssoDescriptor.getFirstChild();
            while (!node.getLocalName().equals(nodeName)) {
                node = node.getNextSibling();
                if (node == null) {
                    return null;
                }
            }
            return node.getLocalName().equals(nodeName) ? node : null;
        }
        return null;
    }
}
