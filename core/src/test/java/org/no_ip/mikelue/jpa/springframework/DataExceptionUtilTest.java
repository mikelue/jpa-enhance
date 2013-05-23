package org.no_ip.mikelue.jpa.springframework;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.Test;
import org.testng.Assert;

public class DataExceptionUtilTest {
    public DataExceptionUtilTest() {}

    /**
     * Test simple message which is contained by deeper exception.<p>
     */
    @Test
    public void containsMessage()
    {
        DataAccessException testException = new DataIntegrityViolationException(
            "Test data access exception", new SimpleException()
        );

        Assert.assertTrue(
            DataExceptionUtil.containsMessage(testException, SimpleException.TEST_MESSAGE)
        );
    }
}

class SimpleException extends Exception {
	private final static long serialVersionUID = 1L;

    static final String TEST_MESSAGE = "This is test message";

    SimpleException()
    {
        super(TEST_MESSAGE);
    }
}
