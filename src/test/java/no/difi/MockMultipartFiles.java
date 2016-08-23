package no.difi;

import org.springframework.mock.web.MockMultipartFile;

public class MockMultipartFiles {
    private static String TEST_FILENAME = "test";
    public static String TEST_ENTITY_ID = "testsp";
    public static String TEST_LOGOUT_URL = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n";
    public static String TEST_LOGOUT_URL_BLANK_LOCATION = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"\" ResponseLocation=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n";
    public static String TEST_LOGOUT_URL_BULLSHIT_LOCATION = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"asdfasdf\" ResponseLocation=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n";
    public static String TEST_LOGOUT_URL_HTTP_LOCATION = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"http://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n";
    public static String TEST_LOGOUT_URL_BLANK_RESPONSE_LOCATION = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"\"/>\n";
    public static String TEST_LOGOUT_URL_BULLSHIT_RESPONSE_LOCATION = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"asdfasdf\"/>\n";
    public static String TEST_LOGOUT_URL_HTTP_RESPONSE_LOCATION = "<SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"https://eid-vag-opensso.difi.local:10001/testsp/logoutrequest\" ResponseLocation=\"http://eid-vag-opensso.difi.local:10001/testsp/logoutresponseconsumer\"/>\n";
    public static String TEST_ASSERTION_CONSUMER_URL = "<AssertionConsumerService index=\"1\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\" Location=\"https://eid-vag-opensso.difi.local:10001/testsp/assertionconsumer\"/>\n";
    public static String TEST_ASSERTION_CONSUMER_URL_BLANK_LOCATION = "<AssertionConsumerService index=\"1\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\" Location=\"\"/>\n";
    public static String TEST_ASSERTION_CONSUMER_URL_BULLSHIT_LOCATION = "<AssertionConsumerService index=\"1\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\" Location=\"adfsadf\"/>\n";
    public static String TEST_ASSERTION_CONSUMER_URL_HTTP_LOCATION = "<AssertionConsumerService index=\"1\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\" Location=\"http://eid-vag-opensso.difi.local:10001/testsp/assertionconsumer\"/>\n";

    public static String createNoneXml() {
        return "Not xml at all";
    }

    public static String createXmlWithMissingTag() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><start><end></start>";
    }

    public static MockMultipartFile createMetaFileWithInvalidTag() throws Exception {
        return new MockMultipartFile("test", createMetafileStringWithExtraInvalidTag().getBytes());
    }

    public static MockMultipartFile createValidMetafileWithNamespace() throws Exception {
        return new MockMultipartFile("evalg", createValidMetafileStringWithNamespace().getBytes());
    }

    public static MockMultipartFile createValidMultipartFileXml() throws Exception {
        return createMultipartFileXml();
    }

    public static MockMultipartFile createMultipartFileXml() throws Exception {
        return new MockMultipartFile(TEST_FILENAME, createMetaFile(TEST_ENTITY_ID, TEST_LOGOUT_URL, TEST_ASSERTION_CONSUMER_URL).getBytes());
    }

    public static MockMultipartFile createMultipartFileXml(String entityId, String logoutUrl, String assertionConsumerUrl) throws Exception {
        return new MockMultipartFile(TEST_FILENAME, createMetaFile(entityId, logoutUrl, assertionConsumerUrl).getBytes());
    }

    private static String createMetaFile(String entityId, String logoutUrl, String assertionConsumerUrl) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<EntityDescriptor entityID=\"" + entityId + "\" xmlns=\"urn:oasis:names:tc:SAML:2.0:metadata\">\n" +
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
                logoutUrl +
                "        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:persistent</NameIDFormat>\n" +
                "        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:transient</NameIDFormat>\n" +
                assertionConsumerUrl +
                "    </SPSSODescriptor>\n" +
                "</EntityDescriptor>\n";
    }

    public static String createMetafileStringWithExtraInvalidTag() {
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

    private static String createValidMetafileStringWithNamespace(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<md:EntityDescriptor entityID=\"https://evalg.stat.no/authfe\" xmlns:md=\"urn:oasis:names:tc:SAML:2.0:metadata\">\n" +
                "    <md:SPSSODescriptor AuthnRequestsSigned=\"true\"\n" +
                "        WantAssertionsSigned=\"true\" protocolSupportEnumeration=\"urn:oasis:names:tc:SAML:2.0:protocol\">\n" +
                "        <md:KeyDescriptor use=\"signing\">\n" +
                "            <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "                <ds:X509Data>\n" +
                "                    <ds:X509Certificate>MIIETjCCAzagAwIBAgIDDJEXMA0GCSqGSIb3DQEBBQUAMEsxCzAJBgNVBAYTAk5PMR0wGwYDVQQK\n" +
                "DBRCdXlwYXNzIEFTLTk4MzE2MzMyNzEdMBsGA1UEAwwUQnV5cGFzcyBDbGFzcyAzIENBIDEwHhcN\n" +
                "MTEwMjIxMTM0NTIzWhcNMTQwMjIxMTM0NTIyWjCBnDELMAkGA1UEBhMCTk8xLTArBgNVBAoMJEtP\n" +
                "TU1VTkFMLSBPRyBSRUdJT05BTC0gREVQQVJURU1FTlRFVDEbMBkGA1UECwwSS29tbXVuYWxhdmRl\n" +
                "bGluZ2VuMS0wKwYDVQQDDCRLT01NVU5BTC0gT0cgUkVHSU9OQUwtIERFUEFSVEVNRU5URVQxEjAQ\n" +
                "BgNVBAUTCTk3MjQxNzg1ODCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAucJbffql2QXT2O2J\n" +
                "X1cZD7OJdr2VbWlNC/aJvro4cFBMrIzYtxpDFMoY4t5seXFQijuf9yQleo4bg7wZ+zWyKyKp3WMn\n" +
                "Pl6rYZwXjm+Y4cxQmgg3YRIqlE27goYzMriSzRNg15spJPY2qWuqWHk+KZFb63ywG6Ahyv6RgLa2\n" +
                "Rf0CAwEAAaOCAWswggFnMAkGA1UdEwQCMAAwHwYDVR0jBBgwFoAUOBTmyPCppAP0Tj4io1vy1uCt\n" +
                "QHQwHQYDVR0OBBYEFK+nPYc6DKwgNsvowP36OKDATRoDMA4GA1UdDwEB/wQEAwIEsDAVBgNVHSAE\n" +
                "DjAMMAoGCGCEQgEaAQMCMIGvBgNVHR8EgacwgaQwNKAyoDCGLmh0dHA6Ly9jcmwucHJvZC5idXlw\n" +
                "YXNzLm5vL2NybC9CUENsYXNzM0NBMS5jcmwwbKBqoGiGZmxkYXA6Ly9sZGFwLnByb2QuYnV5cGFz\n" +
                "cy5uby9kYz1CdXlwYXNzLGRjPU5PLENOPUJ1eXBhc3MlMjBDbGFzcyUyMDMlMjBDQSUyMDE/Y2Vy\n" +
                "dGlmaWNhdGVSZXZvY2F0aW9uTGlzdDBBBggrBgEFBQcBAQQ1MDMwMQYIKwYBBQUHMAGGJWh0dHA6\n" +
                "Ly9vY3NwLnByb2QuYnV5cGFzcy5uby9CUENsYXNzMjMwDQYJKoZIhvcNAQEFBQADggEBAIJXDZGz\n" +
                "tFruVcOaFFOqVdolA86VKneg+nZohTeYDS68fWIwWShqrKKWIYlCqlg5BwylTSeXkoviCUFMy2ES\n" +
                "uPKtnZFbqoDCEHoF6o8iG3CVHblE/BzbIio81QwDRYJvpKxd9M289QemxZ0AJpMn9uz9Hx4Ne1DK\n" +
                "RKmSe+VxnyQiZH1fShmHlS/RqGzRU0c92M78xdYdhdVo6NJbxZrclE7RDkPOXG/NeAjI7mowKYLU\n" +
                "OQt5T5dWilDSdD/Mbjf1KwHDBwFO80HK45GKNEHWI2llnJEcqRPVR+W1vtzRlju9dtHU2dUh1QeS\n" +
                "kiWtqIlvEXZI1mI8RXXYCHwUZEP6H9U=</ds:X509Certificate>\n" +
                "                </ds:X509Data>\n" +
                "            </ds:KeyInfo>\n" +
                "        </md:KeyDescriptor>\n" +
                "        <md:KeyDescriptor use=\"encryption\">\n" +
                "            <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "                <ds:X509Data>\n" +
                "                    <ds:X509Certificate>MIIETjCCAzagAwIBAgIDDJEXMA0GCSqGSIb3DQEBBQUAMEsxCzAJBgNVBAYTAk5PMR0wGwYDVQQK\n" +
                "DBRCdXlwYXNzIEFTLTk4MzE2MzMyNzEdMBsGA1UEAwwUQnV5cGFzcyBDbGFzcyAzIENBIDEwHhcN\n" +
                "MTEwMjIxMTM0NTIzWhcNMTQwMjIxMTM0NTIyWjCBnDELMAkGA1UEBhMCTk8xLTArBgNVBAoMJEtP\n" +
                "TU1VTkFMLSBPRyBSRUdJT05BTC0gREVQQVJURU1FTlRFVDEbMBkGA1UECwwSS29tbXVuYWxhdmRl\n" +
                "bGluZ2VuMS0wKwYDVQQDDCRLT01NVU5BTC0gT0cgUkVHSU9OQUwtIERFUEFSVEVNRU5URVQxEjAQ\n" +
                "BgNVBAUTCTk3MjQxNzg1ODCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAucJbffql2QXT2O2J\n" +
                "X1cZD7OJdr2VbWlNC/aJvro4cFBMrIzYtxpDFMoY4t5seXFQijuf9yQleo4bg7wZ+zWyKyKp3WMn\n" +
                "Pl6rYZwXjm+Y4cxQmgg3YRIqlE27goYzMriSzRNg15spJPY2qWuqWHk+KZFb63ywG6Ahyv6RgLa2\n" +
                "Rf0CAwEAAaOCAWswggFnMAkGA1UdEwQCMAAwHwYDVR0jBBgwFoAUOBTmyPCppAP0Tj4io1vy1uCt\n" +
                "QHQwHQYDVR0OBBYEFK+nPYc6DKwgNsvowP36OKDATRoDMA4GA1UdDwEB/wQEAwIEsDAVBgNVHSAE\n" +
                "DjAMMAoGCGCEQgEaAQMCMIGvBgNVHR8EgacwgaQwNKAyoDCGLmh0dHA6Ly9jcmwucHJvZC5idXlw\n" +
                "YXNzLm5vL2NybC9CUENsYXNzM0NBMS5jcmwwbKBqoGiGZmxkYXA6Ly9sZGFwLnByb2QuYnV5cGFz\n" +
                "cy5uby9kYz1CdXlwYXNzLGRjPU5PLENOPUJ1eXBhc3MlMjBDbGFzcyUyMDMlMjBDQSUyMDE/Y2Vy\n" +
                "dGlmaWNhdGVSZXZvY2F0aW9uTGlzdDBBBggrBgEFBQcBAQQ1MDMwMQYIKwYBBQUHMAGGJWh0dHA6\n" +
                "Ly9vY3NwLnByb2QuYnV5cGFzcy5uby9CUENsYXNzMjMwDQYJKoZIhvcNAQEFBQADggEBAIJXDZGz\n" +
                "tFruVcOaFFOqVdolA86VKneg+nZohTeYDS68fWIwWShqrKKWIYlCqlg5BwylTSeXkoviCUFMy2ES\n" +
                "uPKtnZFbqoDCEHoF6o8iG3CVHblE/BzbIio81QwDRYJvpKxd9M289QemxZ0AJpMn9uz9Hx4Ne1DK\n" +
                "RKmSe+VxnyQiZH1fShmHlS/RqGzRU0c92M78xdYdhdVo6NJbxZrclE7RDkPOXG/NeAjI7mowKYLU\n" +
                "OQt5T5dWilDSdD/Mbjf1KwHDBwFO80HK45GKNEHWI2llnJEcqRPVR+W1vtzRlju9dtHU2dUh1QeS\n" +
                "kiWtqIlvEXZI1mI8RXXYCHwUZEP6H9U=</ds:X509Certificate>\n" +
                "                </ds:X509Data>\n" +
                "            </ds:KeyInfo>\n" +
                "        </md:KeyDescriptor>\n" +
                "        <md:SingleLogoutService\n" +
                "            Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\"\n" +
                "            Location=\"https://evalg.stat.no/authfe/saml/SingleLogoutRedirect\" ResponseLocation=\"https://evalg.stat.no/authfe/saml/SingleLogoutRedirect\"/>\n" +
                "        <md:NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:transient</md:NameIDFormat>\n" +
                "        <md:NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:persistent</md:NameIDFormat>\n" +
                "        <md:AssertionConsumerService\n" +
                "            Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact\"\n" +
                "            Location=\"https://evalg.stat.no/authfe/saml/SSO\" index=\"0\" isDefault=\"true\"/>\n" +
                "    </md:SPSSODescriptor>\n" +
                "</md:EntityDescriptor>";
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
