package no.difi;

import no.difi.domain.ValidationResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomAsserts {
    public static void assertValidationResult(ValidationResult expected, ValidationResult actual) {
        assertEquals(expected.getValid(), actual.getValid());
        assertTrue(String.format("Validation message, expected <%s> but was <%s>", expected.getMessage(), actual.getMessage()),
                expected.getMessage().equals(actual.getMessage()));
        assertTrue(String.format("Number of messages not same, expected <%d> but was <%d>", expected.getDetails().size(), actual.getDetails().size()),
                expected.getDetails().size() == actual.getDetails().size());

        for (int i = 0; i < expected.getDetails().size(); i++) {
            assertEquals(expected.getDetails().get(i).getDetail(), actual.getDetails().get(i).getDetail());
            assertEquals(expected.getDetails().get(i).getStatus(), actual.getDetails().get(i).getStatus());
        }
    }
}
