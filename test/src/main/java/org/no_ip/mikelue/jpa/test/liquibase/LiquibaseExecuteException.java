package org.no_ip.mikelue.jpa.test.liquibase;

/**
 * The wrapper exception while running from {@link LiquibaseBuilder#runExecutor(LiquibaseExecutor)}.<p>
 */
public class LiquibaseExecuteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct this class with {@link Exception cause exception}.<p>
     *
     * @param builder The running builider
     * @param e The causing exception
     */
    public LiquibaseExecuteException(LiquibaseBuilder builder, Exception e)
    {
        super(
            String.format("Execute Liquibase error: [%s]", builder.getChangeLogFile()),
            e
        );
    }
}
