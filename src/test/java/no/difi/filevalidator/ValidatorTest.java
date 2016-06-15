package no.difi.filevalidator;

import no.difi.config.MetadataConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MetadataConfig.class})
public class ValidatorTest {

    @Autowired
    private Validator validator;

    private RedirectAttributes redirectAttributes;

    @Spy
    InputStream inputStreamSpyMock = new ByteArrayInputStream(createValidXml().getBytes());

    @Before
    public void setUp() throws Exception {
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    public void should_return_true_when_file_is_valid() throws Exception {
        boolean valid = validator.validate(createValidMultipartFileXml(), redirectAttributes);

        assertTrue(valid);
    }

    @Test
    public void should_not_write_error_message_when_file_is_valid() throws Exception {
        validator.validate(createValidMultipartFileXml(), redirectAttributes);

        assertNull(redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    public void should_return_false_when_file_has_invalid_xml_syntax() throws Exception {
        Boolean valid = validator.validate(createInvalidMultipartFileXml(), redirectAttributes);

        assertFalse(valid);
    }

    @Test
    public void should_give_invalid_xml_message_when_file_has_invalid_xml_syntax() throws Exception {
        String invalidXML = "Filen er ikke gyldig xml";

        validator.validate(createInvalidMultipartFileXml(), redirectAttributes);

        assertEquals(invalidXML, redirectAttributes.getFlashAttributes().get("message"));
    }

    @Test
    public void should_write_error_when_file_has_invalid_xml_syntax() throws Exception {
        validator.validate(createInvalidMultipartFileXml(), redirectAttributes);

        assertNotNull(redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    public void should_write_error_when_io_exception_is_thrown() throws IOException{
        String errorMessage = "IO Exception was thrown";
        PowerMockito.doThrow(new IOException(errorMessage)).when(inputStreamSpyMock).close();

        validator.validate(inputStreamSpyMock, redirectAttributes);

        assertEquals(errorMessage, redirectAttributes.getFlashAttributes().get("error"));
    }

    private static InputStream createValidMultipartFileXml() throws Exception {
        MockMultipartFile fileMock = new MockMultipartFile("test", createValidXml().getBytes());
        return fileMock.getInputStream();
    }

    private static InputStream createInvalidMultipartFileXml() throws Exception {
        MockMultipartFile fileMock = new MockMultipartFile("test", createInvalidXml().getBytes());
        return fileMock.getInputStream();
    }

    private static String createInvalidXml() {
        return "note>" +
                "<to>Tove</to>" +
                "<from>Jani</from>" +
                "<heading>Reminder</heading>" +
                "<body>Don't forget me this weekend!</body>" +
                "</note>";
    }

    private static String createValidXml() {
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
                "</EntityDescriptor>";
    }

}