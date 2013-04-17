package org.no_ip.mikelue.jpa.test.testng.validation;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.testng.Assert;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * This object provide various assertion for JSR-303 testing.<p>
 *
 * @see AbstractValidationTestBase
 */
public class ValidationAssert {
    private ValidationAssert() {}

    /**
     * Asserts expected violation, whichi is exactly the sole violation.<p>
     *
     * @param violations The violations produced by {@link Validator#validate()}
     * @param expectedAnnotationType The mandatory expected class of violated annotation
	 *
	 * @see #assertInViolations
     */
    public static <T> void assertViolation(
		Set<ConstraintViolation<T>> violations,
		Class<? extends Annotation> expectedAnnotationType
	) {
        notNull(violations);
        notNull(expectedAnnotationType);

		// Ensures that there is only one violation
        Assert.assertEquals(violations.size(), 1, String.format("Wrong number of violations: [%d]", violations.size()));

        /**
         * Asserts that the expected violation
         */
        Class<? extends Annotation> testAnnnotationType =
            violations.iterator().next().getConstraintDescriptor().getAnnotation().getClass();
        Assert.assertTrue(
            expectedAnnotationType.isAssignableFrom(testAnnnotationType),
            String.format(
                "Expected validation annotation: [%s]. But got: [%s]",
                expectedAnnotationType.getCanonicalName(), testAnnnotationType.getCanonicalName()
            )
        );
        // :~)
    }

    /**
     * Asserts expected violation, which is contained in one of violations.<p>
     *
     * @param violations The violations produced by {@link Validator#validate()}
     * @param expectedAnnotationType The mandatory expected class of violated annotation
	 *
	 * @see #assertViolation
     */
    public static <T> void assertInViolations(
		Set<ConstraintViolation<T>> violations,
		Class<? extends Annotation> expectedAnnotationType
	) {
        notNull(violations);
        notNull(expectedAnnotationType);

        Assert.assertTrue(violations.size() >= 1, "None of violations");

        /**
         * Check each of violations to find out the expected one
         */
        for (ConstraintViolation<T> violation: violations) {
            Class<? extends Annotation> testAnnnotationType =
                violation.getConstraintDescriptor().getAnnotation().getClass();

            if (expectedAnnotationType.isAssignableFrom(testAnnnotationType)) {
                return;
            }
        }
        // :~)

        /**
         * Can't find a matched violation
         */
        Assert.fail(
            String.format(
                "Expected validation annotation: [%s]. But got none of matching ones",
                expectedAnnotationType.getCanonicalName()
            )
        );
        // :~)
    }
}
