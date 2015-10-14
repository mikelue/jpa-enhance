package org.no_ip.mikelue.jpa.springframework;

import org.springframework.dao.DataAccessException;

/**
 * This class is used to process exception from database.
 *
 * @see DataAccessException
 */
public class DataExceptionUtil {
    private DataExceptionUtil() {}

    /**
     * This method would check out whether or not the {@link DataAccessException#getRootCause}
     * contains particular message.
     *
     * @param e - processed exception to extract message info
     * @param message - the message to check
     *
     * @return true if the exception contains the message
     */
    public static boolean containsMessage(DataAccessException e, String message)
    {
        return e.getRootCause().getMessage().toLowerCase().contains(
            message.toLowerCase()
        );
    }
}
