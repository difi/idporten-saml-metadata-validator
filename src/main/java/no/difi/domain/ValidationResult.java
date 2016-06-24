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
        return message;
    }

    public String getResult(){
        return result;
    }

    public static ValidationResultBuilder builder(){
        return new ValidationResultBuilder();
    }

    public static class ValidationResultBuilder {
        private boolean valid;
        private String message;
        private String result;

        public ValidationResultBuilder(){

        }

        public ValidationResultBuilder valid(final boolean valid){
            this.valid = valid;
            return this;
        }

        public ValidationResultBuilder message(final String message){
            this.message = message;
            return this;
        }

        public ValidationResultBuilder result(final String result){
            this.result = result;
            return this;
        }

        public ValidationResult build(){
            return new ValidationResult(valid, message, result);
        }
    }


}
