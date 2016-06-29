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

    private static String createNoneXml() {
        return "Not xml at all";
    }

    private String createXmlWithMissingTag() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><start><end></start>";
    }

    private static MockMultipartFile createMetaFileWithInvalidTag() throws Exception {
        return new MockMultipartFile("test", createMetafileStringWithExtraInvalidTag().getBytes());
    }

    private static MockMultipartFile createValidMultipartFileXml() throws Exception {
        return new MockMultipartFile("test", createValidMetaFileFullScenario().getBytes());
    }

    private static String createMetafileStringWithExtraInvalidTag() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<EntityDescriptor entityID=\"testsp\" xmlns=\"urn:oasis:names:tc:SAML:2.0:metadata\">\n" +
                "<bullshittag>bullshit</bullshittag>\n" +
                "    <SPSSODescriptor AuthnRequestsSigned=\"true\" WantAssertionsSigned=\"true\" protocolSupportEnumeration=\"urn:oasis:names:tc:SAML:2.0:protocol\">\n" +
                "        <SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"http://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"http://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n" +
                "        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:persistent</NameIDFormat>\n" +
                "        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:transient</NameIDFormat>\n" +
                "        <AssertionConsumerService index=\"1\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\" Location=\"http://eid-vag-opensso.difi.local:10001/testsp/assertionconsumer\"/>\n" +
                "    </SPSSODescriptor>\n" +
                "</EntityDescriptor>\n";
    }

    private static String createValidMetaFileFullScenario() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<EntityDescriptor entityID=\"testsp\" xmlns=\"urn:oasis:names:tc:SAML:2.0:metadata\">\n" +
                "    <SPSSODescriptor AuthnRequestsSigned=\"true\" WantAssertionsSigned=\"true\" protocolSupportEnumeration=\"urn:oasis:names:tc:SAML:2.0:protocol\">\n" +
                "        <KeyDescriptor use=\"signing\">\n" +
                "            <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "                <ds:X509Data>\n" +
                "                    <ds:X509Certificate>\n" +
                "MIICQDCCAakCBEeNB0swDQYJKoZIhvcNAQEEBQAwZzELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNh\n" +
                "bGlmb3JuaWExFDASBgNVBAcTC1NhbnRhIENsYXJhMQwwCgYDVQQKEwNTdW4xEDAOBgNVBAsTB09w\n" +
                "ZW5TU08xDTALBgNVBAMTBHRlc3QwHhcNMDgwMTE1MTkxOTM5WhcNMTgwMTEyMTkxOTM5WjBnMQsw\n" +
                "CQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEUMBIGA1UEBxMLU2FudGEgQ2xhcmExDDAK\n" +
                "BgNVBAoTA1N1bjEQMA4GA1UECxMHT3BlblNTTzENMAsGA1UEAxMEdGVzdDCBnzANBgkqhkiG9w0B\n" +
                "AQEFAAOBjQAwgYkCgYEArSQc/U75GB2AtKhbGS5piiLkmJzqEsp64rDxbMJ+xDrye0EN/q1U5Of+\n" +
                "RkDsaN/igkAvV1cuXEgTL6RlafFPcUX7QxDhZBhsYF9pbwtMzi4A4su9hnxIhURebGEmxKW9qJNY\n" +
                "Js0Vo5+IgjxuEWnjnnVgHTs1+mq5QYTA7E6ZyL8CAwEAATANBgkqhkiG9w0BAQQFAAOBgQB3Pw/U\n" +
                "QzPKTPTYi9upbFXlrAKMwtFf2OW4yvGWWvlcwcNSZJmTJ8ARvVYOMEVNbsT4OFcfu2/PeYoAdiDA\n" +
                "cGy/F2Zuj8XJJpuQRSE6PtQqBuDEHjjmOQJ0rV/r8mO1ZCtHRhpZ5zYRjhRC9eCbjx9VrFax0JDC\n" +
                "/FfwWigmrW0Y0Q==\n" +
                "                    </ds:X509Certificate>\n" +
                "                </ds:X509Data>\n" +
                "                        </ds:KeyInfo>\n" +
                "        </KeyDescriptor>\n" +
                "        <KeyDescriptor use=\"encryption\">\n" +
                "            <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "                <ds:X509Data>\n" +
                "                    <ds:X509Certificate>\n" +
                "MIICQDCCAakCBEeNB0swDQYJKoZIhvcNAQEEBQAwZzELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNh\n" +
                "bGlmb3JuaWExFDASBgNVBAcTC1NhbnRhIENsYXJhMQwwCgYDVQQKEwNTdW4xEDAOBgNVBAsTB09w\n" +
                "ZW5TU08xDTALBgNVBAMTBHRlc3QwHhcNMDgwMTE1MTkxOTM5WhcNMTgwMTEyMTkxOTM5WjBnMQsw\n" +
                "CQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEUMBIGA1UEBxMLU2FudGEgQ2xhcmExDDAK\n" +
                "BgNVBAoTA1N1bjEQMA4GA1UECxMHT3BlblNTTzENMAsGA1UEAxMEdGVzdDCBnzANBgkqhkiG9w0B\n" +
                "AQEFAAOBjQAwgYkCgYEArSQc/U75GB2AtKhbGS5piiLkmJzqEsp64rDxbMJ+xDrye0EN/q1U5Of+\n" +
                "RkDsaN/igkAvV1cuXEgTL6RlafFPcUX7QxDhZBhsYF9pbwtMzi4A4su9hnxIhURebGEmxKW9qJNY\n" +
                "Js0Vo5+IgjxuEWnjnnVgHTs1+mq5QYTA7E6ZyL8CAwEAATANBgkqhkiG9w0BAQQFAAOBgQB3Pw/U\n" +
                "QzPKTPTYi9upbFXlrAKMwtFf2OW4yvGWWvlcwcNSZJmTJ8ARvVYOMEVNbsT4OFcfu2/PeYoAdiDA\n" +
                "cGy/F2Zuj8XJJpuQRSE6PtQqBuDEHjjmOQJ0rV/r8mO1ZCtHRhpZ5zYRjhRC9eCbjx9VrFax0JDC\n" +
                "/FfwWigmrW0Y0Q==\n" +
                "                    </ds:X509Certificate>\n" +
                "                </ds:X509Data>\n" +
                "                        </ds:KeyInfo>\n" +
                "            <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes128-cbc\">\n" +
                "                <xenc:KeySize xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">128</xenc:KeySize>\n" +
                "                        </EncryptionMethod>\n" +
                "        </KeyDescriptor>\n" +
                "        <SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"http://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"http://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n" +
                "        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:persistent</NameIDFormat>\n" +
                "        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:transient</NameIDFormat>\n" +
                "        <AssertionConsumerService index=\"1\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\" Location=\"http://eid-vag-opensso.difi.local:10001/testsp/assertionconsumer\"/>\n" +
                "    </SPSSODescriptor>\n" +
                "</EntityDescriptor>\n";
    }
}