package guru.mikelue.jpa.test.testng.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ValidationAssertTest {
	private Validator validator = Validation
		.buildDefaultValidatorFactory()
		.getValidator();

	public ValidationAssertTest() {}

	private static class SampleBeanForValidation {
		@Max(10)
		int max10;
		@Min(10)
		int min10;
	}

	/**
	 * Tests the successful assertion of sole violation.
	 */
	@Test
	public void assertViolationAsSuccess()
	{
		Set<ConstraintViolation<SampleBeanForValidation>> violations =
			validator.validateValue(SampleBeanForValidation.class, "max10", 11);

		ValidationAssert.assertViolation(
			violations, Max.class
		);
	}

	/**
	 * Tests throwing error of wrong type of violation.
	 */
	@Test(expectedExceptions=AssertionError.class)
	public void assertViolationWithWrongTypeOfViolations()
	{
		Set<ConstraintViolation<SampleBeanForValidation>> violations =
			validator.validateValue(SampleBeanForValidation.class, "max10", 11);

		ValidationAssert.assertViolation(
			violations, AssertTrue.class
		);
	}

	/**
	 * Tests throwing error of assertion for wrong number of violations.
	 */
	@Test(dataProvider="AssertViolationWithWrongNumberOfViolations", expectedExceptions=AssertionError.class)
	public void assertViolationWithWrongNumberOfViolations(int sampleValueOfMax10, int sampleValueOfMin10)
	{
		SampleBeanForValidation sampleBean = new SampleBeanForValidation();
		sampleBean.max10 = sampleValueOfMax10;
		sampleBean.min10 = sampleValueOfMin10;

		Set<ConstraintViolation<SampleBeanForValidation>> violations =
			validator.validate(sampleBean);

		ValidationAssert.assertViolation(
			violations, AssertTrue.class
		);
	}
	@DataProvider(name="AssertViolationWithWrongNumberOfViolations")
	private Object[][] getAssertViolationWithWrongNumberOfViolations()
	{
		return new Object[][] {
			{ 11, 9 }, // Two violations
			{ 10, 10 } // No violation
		};
	}

	/**
	 * Tests the successful assertion of multiple violations.
	 */
	@Test
	public void assertInViolationsAsSuccess()
	{
		Set<ConstraintViolation<SampleBeanForValidation>> testViolations =
			validator.validateValue(SampleBeanForValidation.class, "max10", 11);

		ValidationAssert.assertInViolations(
			testViolations, Max.class
		);
	}

	/**
	 * Tests the failure assertion of no violation.
	 */
	@Test(expectedExceptions=AssertionError.class)
	public void assertInViolationsWithNoViolation()
	{
		Set<ConstraintViolation<SampleBeanForValidation>> testViolations =
			validator.validateValue(SampleBeanForValidation.class, "max10", 10);

		ValidationAssert.assertInViolations(
			testViolations, Max.class
		);
	}

	/**
	 * Tests the failure assertion of wrong type of violations.
	 */
	@Test(expectedExceptions=AssertionError.class)
	public void assertInViolationsWithWrongTypeofViolations()
	{
		Set<ConstraintViolation<SampleBeanForValidation>> testViolations =
			validator.validateValue(SampleBeanForValidation.class, "max10", 11);

		ValidationAssert.assertInViolations(
			testViolations, AssertTrue.class
		);
	}
}
