package no.difi;

import no.difi.domain.ValidationResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomAsserts {
    public static void assertValidationResult(ValidationResult expected, ValidationResult actual) {
        assertEquals(expected.getValid(), actual.getValid());
        assertTrue(String.format("Validation message, expected <%s> but was <%s>", expected.getMessage(), actual.getMessage()),
                expected.getMessage().equals(actual.getMessage()));
    }
}
