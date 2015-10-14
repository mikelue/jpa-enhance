package org.no_ip.mikelue.jpa.test.dbunit;

import org.no_ip.mikelue.jpa.test.testng.ExecuteActionException;

/**
 * The wrapper exception for {@link DbUnitExecuteException}.
 */
public class DbUnitActionException extends ExecuteActionException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with source {@link DbUnitExecuteException}.
     *
     * @param action The running action
     * @param e The source of exception
     */
    public DbUnitActionException(DbUnitAction action, DbUnitExecuteException e)
    {
        super(action, e);
    }
}
