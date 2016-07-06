package no.difi.domain;

public enum DetailsStatus {
    MISSING_ENTITY("validation.param.missing.entityid"),
    MISSING_LOGOUT_URL("validation.param.missing.logouturl"),
    MISSING_ASSERTION_CONSUMER_URL("validation.param.missing.assertionconsumerurl"),
    MISSING_PUBLIC_CERTIFICATE("validation.param.missing.publiccertificate");

    private String key;

    DetailsStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
