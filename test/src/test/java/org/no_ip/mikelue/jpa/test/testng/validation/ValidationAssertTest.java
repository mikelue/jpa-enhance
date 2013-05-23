package org.no_ip.mikelue.jpa.test.testng.validation;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.metadata.ConstraintDescriptor;

import mockit.Mock;
import mockit.Mocked;
import mockit.MockUp;
import mockit.NonStrictExpectations;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ValidationAssertTest {
	@Mocked
	private Set<ConstraintViolation<Object>> mockViolations;
	@Mocked
	private Iterator<ConstraintViolation<Object>> mockIterator;
	@Mocked
	private ConstraintViolation<Object> mockViolation;
	@Mocked
	private ConstraintDescriptor<?> mockConstraintDescriptor;

	public ValidationAssertTest() {}

	/**
	 * Tests the successful assertion of sole violation.<p>
	 */
	@Test
	public void assertViolationAsSuccess()
	{
		prepareMockSoleViolations(1);

		ValidationAssert.assertViolation(
			mockViolations, AssertTrue.class
		);
	}

	/**
	 * Tests throwing error of wrong type of violation.<p>
	 */
	@Test(expectedExceptions=AssertionError.class)
	public void assertViolationWithWrongTypeOfViolations()
	{
		prepareMockSoleViolations(1);

		new NonStrictExpectations()
		{
			AssertFalse mockAnnotation;

			{
				mockConstraintDescriptor.getAnnotation();
				result = mockAnnotation;
			}
		};

		ValidationAssert.assertViolation(
			mockViolations, AssertTrue.class
		);
	}

	/**
	 * Tests throwing error of assertion for wrong number of violations.<p>
	 */
	@Test(dataProvider="AssertViolationWithWrongNumberOfViolations", expectedExceptions=AssertionError.class)
	public void assertViolationWithWrongNumberOfViolations(int numberOfViolations)
	{
		prepareMockSoleViolations(numberOfViolations);

		ValidationAssert.assertViolation(
			mockViolations, AssertTrue.class
		);
	}
	@DataProvider(name="AssertViolationWithWrongNumberOfViolations")
	private Object[][] getAssertViolationWithWrongNumberOfViolations()
	{
		return new Object[][] {
			{ 2 },
			{ 0 }
		};
	}

	/**
	 * Tests the successful assertion of multiple violations.<p>
	 */
	@Test
	public void assertInViolationsAsSuccess()
	{
		new NonStrictExpectations()
		{
			AssertTrue mockAnnotation;

			{
				mockViolation.getConstraintDescriptor();
				result = mockConstraintDescriptor;

				mockConstraintDescriptor.getAnnotation();
				result = mockAnnotation;
			}
		};

		Set<ConstraintViolation<Object>> testViolations = new HashSet<ConstraintViolation<Object>>(1);
		testViolations.add(mockViolation);

		ValidationAssert.assertInViolations(
			testViolations, AssertTrue.class
		);
	}

	/**
	 * Tests the failure assertion of no violation.<p>
	 */
	@Test(expectedExceptions=AssertionError.class)
	public void assertInViolationsWithNoViolation()
	{
		Set<ConstraintViolation<Object>> testViolations = new HashSet<ConstraintViolation<Object>>(0);

		ValidationAssert.assertInViolations(
			testViolations, AssertTrue.class
		);
	}

	/**
	 * Tests the failure assertion of wrong type of violations.<p>
	 */
	@Test(expectedExceptions=AssertionError.class)
	public void assertInViolationsWithWrongTypeofViolations()
	{
		Set<ConstraintViolation<Object>> testViolations = new HashSet<ConstraintViolation<Object>>(2);
		testViolations.add(buildMockViolation());
		testViolations.add(buildMockViolation());

		ValidationAssert.assertInViolations(
			testViolations, AssertTrue.class
		);
	}
	private ConstraintViolation<Object> buildMockViolation()
	{
		/**
		 * Builds the wrong type of violation
		 */
		new NonStrictExpectations()
		{
			AssertFalse mockAnnotation;

			{
				mockConstraintDescriptor.getAnnotation();
				result = mockAnnotation;
			}
		};
		// :~)

		return new MockUp<ConstraintViolation<Object>>() {
			@Mock
			public ConstraintDescriptor<?> getConstraintDescriptor()
			{
				return mockConstraintDescriptor;
			}
		}
			.getMockInstance();
	}

	private void prepareMockSoleViolations(
		final int numberOfViolations
	) {
		/**
		 * Simulate the success of assertion for sole violation
		 */
		new NonStrictExpectations()
		{
			AssertTrue mockAnnotation;

			{
				mockViolations.size();
				result = numberOfViolations;

				mockViolations.iterator();
				result = mockIterator;

				mockIterator.next();
				result = mockViolation;

				mockViolation.getConstraintDescriptor();
				result = mockConstraintDescriptor;

				mockConstraintDescriptor.getAnnotation();
				result = mockAnnotation;
			}
		};
		// :~)
	}
}
