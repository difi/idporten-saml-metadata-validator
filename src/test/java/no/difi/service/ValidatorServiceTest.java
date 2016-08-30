package no.difi.service;

import no.difi.config.MetadataConfig;
import no.difi.domain.*;
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
import static no.difi.domain.DetailsStatus.ERROR;
import static no.difi.domain.DetailsStatus.INFO;
import static no.difi.domain.DetailsStatus.WARNING;
import static no.difi.domain.Message.*;
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
        ValidationResult expected = createExpected(false, environment.getRequiredProperty(VALIDATION_ERROR_XML.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createNoneXml().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_fail_wilt_error_xml_when_input_lack_end_tag() throws Exception {
        ValidationResult expected = createExpected(false, environment.getRequiredProperty(VALIDATION_ERROR_XML.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createXmlWithMissingTag().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_return_validation_ok_when_xsd_validates() throws Exception {
        ValidationResult expected = createExpected(true, environment.getRequiredProperty(VALIDATION_OK_MESSAGE.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createMultipartFileXml().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_return_fault_message_when_file_does_not_validate_against_xsd() throws Exception {
        ValidationResult expected = createExpected(false, environment.getRequiredProperty(Message.VALIDATION_FAILED.key()));

        ValidationResult actual = validatorService.validate(new MockMultipartFile("test", createMetafileStringWithExtraInvalidTag().getBytes()));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_write_error_message_when_file_does_not_validate_against_xsd() throws Exception {
        assertEquals(environment.getProperty(VALIDATION_FAILED.key()), validatorService.validate(createMetaFileWithInvalidTag()).getMessage());
    }

    @Test
    public void should_accept_valid_metafile_with_namespace() throws Exception {
        assertEquals(environment.getProperty(VALIDATION_OK_RESULT.key()), validatorService.validate(createValidMetafileWithNamespace()).getResult());
    }

    @Test
    public void should_write_error_result_when_file_does_not_validate_against_xsd() throws Exception {
        assertNotEquals(environment.getProperty(Message.VALIDATION_OK_RESULT.key()), validatorService.validate(createMetaFileWithInvalidTag()).getResult());
    }

    @Test
    public void should_get_error_when_entity_id_is_missing_from_xml() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.entityid")).status(ERROR).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(null, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_info_when_entity_id_is_ok() throws Exception {
        ValidationResult expected = createExpected(true,
                environment.getRequiredProperty(VALIDATION_OK_MESSAGE.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.valid.entityid")).status(INFO).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_logout_url_is_missing_from_xml() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.logouturl")).status(ERROR).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, null, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_assertion_consumer_url_is_missing_from_xml() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.assertionconsumerurl")).status(ERROR).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, null));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_assertion_consumer_url_link_is_blank() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.assertionconsumerurl")).status(ERROR).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL_BLANK_LOCATION));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_assertion_consumer_url_link_does_not_contain_http_or_https() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.assertionconsumerurl")).status(ERROR).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL_BULLSHIT_LOCATION));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_warning_when_assertion_consumer_url_starts_with_http() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.http.assertionconsumerurl")).status(WARNING).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL_HTTP_LOCATION));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_info_when_assertion_consumer_url_starts_with_https() throws Exception {
        ValidationResult expected = createExpected(true,
                environment.getRequiredProperty(VALIDATION_OK_MESSAGE.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.https.assertionconsumerurl")).status(INFO).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_logout_location_is_blank() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.logouturl")).status(INFO).build()
                );

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL_BLANK_LOCATION, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_logout_location_does_not_contain_http_or_https() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.logouturl")).status(INFO).build()
        );

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL_BULLSHIT_LOCATION, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_warning_when_logout_starts_with_http() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.http.logouturl")).status(WARNING).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL_HTTP_LOCATION, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_info_when_logout_starts_with_https() throws Exception {
        ValidationResult expected = createExpected(true,
                environment.getRequiredProperty(VALIDATION_OK_MESSAGE.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.https.logouturl")).status(INFO).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_logout_response_location_is_blank() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.responselocationurl")).status(ERROR).build()
        );

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL_BLANK_RESPONSE_LOCATION, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_error_when_logout_response_location_does_not_contain_http_or_https() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.missing.responselocationurl")).status(ERROR).build()
        );

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL_BULLSHIT_RESPONSE_LOCATION, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_warning_when_logout_response_location_starts_with_http() throws Exception {
        ValidationResult expected = createExpected(false,
                environment.getRequiredProperty(VALIDATION_FAILED.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.http.responselocationurl")).status(WARNING).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL_HTTP_RESPONSE_LOCATION, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }

    @Test
    public void should_get_info_when_logout_response_location_starts_with_https() throws Exception {
        ValidationResult expected = createExpected(true,
                environment.getRequiredProperty(VALIDATION_OK_MESSAGE.key()),
                DetailsMessage.builder().details(environment.getRequiredProperty("validation.param.https.responselocationurl")).status(INFO).build());

        final ValidationResult actual = validatorService.validate(createMultipartFileXml(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL));

        assertValidationResult(expected, actual);
    }
}