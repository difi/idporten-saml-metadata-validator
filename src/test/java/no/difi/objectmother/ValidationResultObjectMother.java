package no.difi.objectmother;

import no.difi.domain.DetailsMessage;
import no.difi.domain.ValidationResult;

public class ValidationResultObjectMother {
    public static ValidationResult createExpected(boolean valid, String message, DetailsMessage... details) {
        final ValidationResult.Builder vrBuilder = ValidationResult.builder()
                .valid(valid)
                .message(message);
        for (DetailsMessage c : details) {
            vrBuilder.details(c);
        }

        return vrBuilder.build();
    }
}
