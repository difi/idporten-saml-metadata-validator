package no.difi.service;

import no.difi.config.MetadataConfig;
import no.difi.domain.Message;
import no.difi.domain.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static no.difi.CustomAsserts.assertValidationResult;
import static no.difi.MockMultipartFiles.*;
import static no.difi.objectmother.ValidationResultObjectMother.createExpected;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MetadataConfig.class})
public class ValidatorServiceTest {

    private ValidatorService validatorService;

    @Autowired
    private Environment environment;

    @Before
    public void setUp() {
        validatorService = new ValidatorService(environment);
    }

    @Test
    public void should_fail_with_error_xml_when_input_is_not_xml() throws Exception {
        ValidationResult expected = createExpected(false, environment.getRequiredProperty(Message.VALIDATION_ERROR_XML.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createNoneXml().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_fail_wilt_error_xml_when_input_lack_end_tag() throws Exception {
        ValidationResult expected = createExpected(false, environment.getRequiredProperty(Message.VALIDATION_ERROR_XML.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createXmlWithMissingTag().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_return_validation_ok_when_xsd_validates() throws Exception {
        ValidationResult expected = createExpected(true, environment.getRequiredProperty(Message.VALIDATION_OK_MESSAGE.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createValidMultipartFileXml().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_return_fault_message_when_file_does_not_validate_against_xsd() throws Exception {
        ValidationResult expected = createExpected(false, environment.getRequiredProperty(Message.VALIDATION_ERROR_XSD.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createMetafileStringWithExtraInvalidTag().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_write_error_message_when_file_does_not_validate_against_xsd() throws Exception {
        assertEquals(environment.getProperty(Message.VALIDATION_ERROR_XSD.key()), validatorService.validate(createMetaFileWithInvalidTag()).getMessage());
    }

    @Test
    public void should_write_error_result_when_file_does_not_validate_against_xsd() throws Exception {
        assertNotEquals(environment.getProperty(Message.VALIDATION_OK_RESULT.key()), validatorService.validate(createMetaFileWithInvalidTag()).getResult());
    }
}