package org.no_ip.mikelue.jpa.test.springframework;

import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.TestException;

public class DataAccessExceptionAssertTest {
    public DataAccessExceptionAssertTest() {}

    /**
     * <p>Test the message and thrown of {@link TestException}.</p>
     */
    @Test
    public void assertExceptionContent()
    {
        final String testMessage = "Content of DataAccessException",
              assertErrorMessage = "Test assert message";

        DataIntegrityViolationException testException = new DataIntegrityViolationException(
            "Has integrity violation", new Exception(testMessage)
        );

        try {
            DataAccessExceptionAssert.assertExceptionContent(
                testException, "No such content in DataAccessException", assertErrorMessage
            );
        } catch (TestException e) {
            Assert.assertTrue(
                e.getMessage().contains(testMessage)
            );
            Assert.assertTrue(
                e.getMessage().contains(assertErrorMessage)
            );

            return;
        }

        Assert.fail("Assert of DataAccessException didn't throw TestException");
    }
}
