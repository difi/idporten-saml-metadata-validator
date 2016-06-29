package no.difi.controller;

import no.difi.CustomAsserts;
import no.difi.config.MetadataConfig;
import no.difi.domain.Message;
import no.difi.domain.ValidationResult;
import no.difi.objectmother.ValidationResultObjectMother;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static no.difi.CustomAsserts.*;
import static no.difi.MockMultipartFiles.*;
import static no.difi.domain.Message.VALIDATION_ERROR_XML;
import static no.difi.domain.Message.VALIDATION_ERROR_XSD;
import static no.difi.domain.Message.VALIDATION_OK_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MetadataConfig.class})
public class MetadataControllerIntegrationTest {
    @Autowired
    MetadataController metadataController;

    @Autowired
    Environment environment;

    private RedirectAttributesModelMap redirectAttributes;

    @Before
    public void setUp() {
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    public void should_show_panel_when_validation_fails() {
        metadataController.handleFileUpload(new MockMultipartFile("test", createNoneXml().getBytes()), redirectAttributes);

        ValidationResult result = (ValidationResult) redirectAttributes.getFlashAttributes().get("validationResult");

        assertFalse(result.getValid());
        assertEquals(redirectAttributes.getFlashAttributes().get("showpanel"), true);
    }

    @Test
    public void should_show_panel_when_validation_is_ok() throws Exception {
        metadataController.handleFileUpload(new MockMultipartFile("test", createValidMultipartFileXml().getBytes()), redirectAttributes);

        ValidationResult result = (ValidationResult) redirectAttributes.getFlashAttributes().get("validationResult");

        assertTrue(result.getValid());
        assertEquals(redirectAttributes.getFlashAttributes().get("showpanel"), true);
    }

    @Test
    public void should_fail_with_validation_error_xml_when_input_file_is_not_xml() {
        ValidationResult expected = ValidationResultObjectMother.createExpected(false, retrieveEnvironmentMessage(VALIDATION_ERROR_XML));

        metadataController.handleFileUpload(new MockMultipartFile("test", createNoneXml().getBytes()), redirectAttributes);
        ValidationResult actual = (ValidationResult) redirectAttributes.getFlashAttributes().get("validationResult");

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_fail_with_validation_error_xml_when_input_file_has_invalid_xml() {
        ValidationResult expected = ValidationResultObjectMother.createExpected(false, retrieveEnvironmentMessage(VALIDATION_ERROR_XML));

        metadataController.handleFileUpload(new MockMultipartFile("test", createXmlWithMissingTag().getBytes()), redirectAttributes);
        ValidationResult actual = (ValidationResult) redirectAttributes.getFlashAttributes().get("validationResult");

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_fail_with_validation_xsd_error_when_input_file_fails_xsd_validation() {
        ValidationResult expected = ValidationResultObjectMother.createExpected(false, retrieveEnvironmentMessage(VALIDATION_ERROR_XSD));

        metadataController.handleFileUpload(new MockMultipartFile("test", createMetafileStringWithExtraInvalidTag().getBytes()), redirectAttributes);
        ValidationResult actual = (ValidationResult) redirectAttributes.getFlashAttributes().get("validationResult");

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_succeed_with_validation_ok_when_input_file_pass_validation() throws Exception {
        ValidationResult expected = ValidationResultObjectMother.createExpected(true, retrieveEnvironmentMessage(VALIDATION_OK_MESSAGE));

        metadataController.handleFileUpload(new MockMultipartFile("test", createValidMultipartFileXml().getBytes()), redirectAttributes);
        ValidationResult actual = (ValidationResult) redirectAttributes.getFlashAttributes().get("validationResult");

        assertValidationResult(expected, actual);
    }

    private String retrieveEnvironmentMessage(Message messageKey) {
        return environment.getProperty(messageKey.key());
    }
}