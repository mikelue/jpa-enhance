package guru.mikelue.jpa.test.springframework;

import org.springframework.dao.DataAccessException;
import org.testng.TestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.mikelue.jpa.springframework.DataExceptionUtil;

/**
 * <p>This assert class is used to assert content of {@link DataAccessException}.</p>
 */
public class DataAccessExceptionAssert {
    private static Logger logger = LoggerFactory.getLogger(DataAccessExceptionAssert.class);

    private DataAccessExceptionAssert() {}

    /**
     * Assert if the exception's root cause contains particular message
     *
     * @param e the exception used to extract message
     * @param containedMessage the message should be contained
     *
     * @see #assertExceptionContent(DataAccessException, String, String)
     */
    public static void assertExceptionContent(DataAccessException e, String containedMessage)
    {
        assertExceptionContent(e, containedMessage, "");
    }
    /**
     * Assert if the exception's root cause contains particular message
     *
     * @param e the exception used to extract message
     * @param containedMessage the message should be contained
     * @param message the assertion error message
     *
     * @see #assertExceptionContent(DataAccessException, String)
     */
    public static void assertExceptionContent(DataAccessException e, String containedMessage, String message)
    {
        logger.debug("Check message in: {}", e.getMessage());

        if (DataExceptionUtil.containsMessage(e, containedMessage)) {
            return;
        }

        throw new TestException(String.format(
            "[%s] should contain message [%s]. Current Message: %s. %s",
            e.getClass(), containedMessage,
            e.getRootCause().getMessage(), message
        ));
    }
}
