package no.difi.service;

public enum Message {
    VALIDATION_OK_MESSAGE("validation.ok.message"),
    VALIDATION_OK_RESULT("validation.ok.result"),
    VALIDATION_ERROR_XML("validation.error.xml"),
    VALIDATION_ERROR_XSD("validation.error.xsd"),
    VALIDATION_GENERAL_ERROR("validation.general.error");

    private String key;

    Message(String key){
        this.key = key;
    }

    public String key(){
        return key;
    }
}
