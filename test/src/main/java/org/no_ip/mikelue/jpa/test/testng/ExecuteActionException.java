package org.no_ip.mikelue.jpa.test.testng;

import org.testng.TestNGException;

/**
 * This class is a type of {@link TestNGException} which contains information about
 * type of {@link Action}.<p>
 *
 * @see Action#executeAction
 */
public class ExecuteActionException extends TestNGException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct with executed {@link Action} and thrown exception.
     *
     * @param action The action which is executed
     * @param e The thrown exception
     */
    public ExecuteActionException(Action action, Exception e)
    {
        super(
            String.format("Execute action error: [%s]", action.getClass().getSimpleName()),
            e
        );
    }
}
