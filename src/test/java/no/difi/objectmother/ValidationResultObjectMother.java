package no.difi.objectmother;

import no.difi.domain.ValidationResult;

public class ValidationResultObjectMother {
    public static ValidationResult createExpected(boolean valid, String message, String... details) {
        final ValidationResult.Builder vrBuilder = ValidationResult.builder()
                .valid(valid)
                .message(message);
        for (String c : details) {
            vrBuilder.details(c);
        }

        return vrBuilder.build();
    }
}
