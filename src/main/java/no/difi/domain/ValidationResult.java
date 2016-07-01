package no.difi.domain;


public class ValidationResult {
    private boolean valid;
    private String message;
    private String result;

    private ValidationResult(boolean valid, String message, String result){
        this.valid = valid;
        this.message = message;
        this.result = result;
    }

    public boolean getValid(){
        return valid;
    }

    public String getMessage(){
        return message == null ? "" : message;
    }

    public String getResult(){
        return result == null ? "" : result;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private boolean valid;
        private String message;
        private String result;

        public Builder valid(final boolean valid){
            this.valid = valid;
            return this;
        }

        public Builder message(final String message){
            this.message = message;
            return this;
        }

        public Builder result(final String result){
            this.result = result;
            return this;
        }

        public ValidationResult build(){
            return new ValidationResult(valid, message, result);
        }
    }
}
