package guru.mikelue.jpa.test.liquibase;

import guru.mikelue.jpa.test.testng.ExecuteActionException;

/**
 * The wrapper exception for {@link LiquibaseExecuteException}.
 */
public class LiquibaseActionException extends ExecuteActionException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with source {@link LiquibaseActionException}.
     *
     * @param action The running action
     * @param e The source of exception
     */
    public LiquibaseActionException(LiquibaseAction action, LiquibaseExecuteException e)
    {
        super(action, e);
    }
}
