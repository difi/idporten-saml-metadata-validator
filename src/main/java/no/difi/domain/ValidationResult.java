package no.difi.domain;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidationResult {
    private boolean valid;
    private String message;
    private String result;
    private List<DetailsMessage> details;

    private ValidationResult(boolean valid, String message, Set<DetailsMessage> details, String result){
        this.valid = valid;
        this.message = message;
        this.result = result;
        this.details = new ArrayList<>();
        this.details.addAll(details);
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

    public ArrayList<DetailsMessage> getDetails() {
        return new ArrayList<>(details);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private boolean valid;
        private String response;
        private String result;
        private Set<DetailsMessage> details = new HashSet<>();

        public Builder valid(final boolean valid){
            this.valid = valid;
            return this;
        }

        public Builder message(final String response){
            this.response = response;
            return this;
        }

        public Builder details(DetailsMessage details) {
            this.details.add(details);
            return this;
        }

        public Builder result(final String result){
            this.result = result;
            return this;
        }

        public ValidationResult build(){
            return new ValidationResult(valid, response, details, result);
        }
    }
}
