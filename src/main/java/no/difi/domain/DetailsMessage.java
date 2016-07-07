package no.difi.domain;

public class DetailsMessage {
    private String details;
    private DetailsStatus status;

    private DetailsMessage(String details, DetailsStatus status) {
        this.details = details;
        this.status = status;
    }

    public String getDetail() {
        return details;
    }

    public DetailsStatus getStatus() {
        return status;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private String details;
        private DetailsStatus status;

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder status(DetailsStatus status) {
            this.status = status;
            return this;
        }

        public DetailsMessage build(){
            return new DetailsMessage(details, status);
        }
    }
}
