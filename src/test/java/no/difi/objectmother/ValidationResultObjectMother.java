package no.difi.objectmother;

import no.difi.domain.ValidationResult;

public class ValidationResultObjectMother {
    public static ValidationResult createExpected(boolean valid, String message) {
        return ValidationResult.builder().valid(valid).message(message).build();
    }
}
