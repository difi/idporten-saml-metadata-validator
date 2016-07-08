package no.difi;

import no.difi.domain.DetailsMessage;
import no.difi.domain.ValidationResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomAsserts {
    public static void assertValidationResult(ValidationResult expected, ValidationResult actual) {
        assertEquals(expected.getValid(), actual.getValid());
        assertTrue(String.format("Validation message, expected <%s> but was <%s>", expected.getMessage(), actual.getMessage()),
                expected.getMessage().equals(actual.getMessage()));

        if (expected.getDetails().size() != 0) { //If check necessary because some tests run without setting up expected details.
            for (DetailsMessage detail : actual.getDetails()) {
                if (expected.getDetails().get(0) == detail) {
                    assertEquals(expected.getDetails().get(0).getDetail(), detail.getDetail());
                    assertEquals(expected.getDetails().get(0).getStatus(), detail.getStatus());
                    break;
                }
            }
        }
    }
}
