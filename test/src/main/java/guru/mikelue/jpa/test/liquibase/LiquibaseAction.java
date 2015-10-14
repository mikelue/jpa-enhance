package guru.mikelue.jpa.test.liquibase;

import guru.mikelue.jpa.test.testng.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The container type which could be execute a {@link LiquibaseExecutor}
 * in predefined environment.
 *
 * <p>The goal of this type is to provide a single entry to execute
 * an "<b>action</b>" of Liquibase in convenient way.</p>
 *
 * <p>The {@link #executeAction()} method is used to execute {@link #getLiquibaseExecutor()} by {@link #getLiquibaseBuilder()}.</p>
 */
public class LiquibaseAction implements Action {
    private Logger logger = LoggerFactory.getLogger(LiquibaseAction.class);
    private LiquibaseBuilder builder;
    private LiquibaseExecutor executor;

    /**
     * Constructs this object with necessary properties.
     *
     * @param newLiquibaseBuilder The builder for running {@link LiquibaseExecutor}
     * @param newLiquibaseExecutor The executor gotten called by {@link LiquibaseBuilder}
     *
     * @see #getLiquibaseBuilder()
     * @see #getLiquibaseExecutor()
     */
    public LiquibaseAction(
        LiquibaseBuilder newLiquibaseBuilder, LiquibaseExecutor newLiquibaseExecutor
    ) {
        logger.debug("Initialize Liquibase action");
        builder = newLiquibaseBuilder;
        executor = newLiquibaseExecutor;
    }

    /**
     * Gets the builder contained by this object.
     *
     * @return The builder to run {@link #getLiquibaseExecutor()}
     */
    public LiquibaseBuilder getLiquibaseBuilder()
    {
        return builder;
    }
    /**
     * Gets the executor of Liquibase.
     *
     * @return The executor which is going to be executed by {@link #getLiquibaseBuilder()}
     */
    public LiquibaseExecutor getLiquibaseExecutor()
    {
        return executor;
    }

    /**
     * Executes {@link #getLiquibaseExecutor()} by {@link #getLiquibaseBuilder()}.
     *
     * @throws LiquibaseActionException The thrown exception if there is an error when executing action
     */
    @Override
    public void executeAction() throws LiquibaseActionException
    {
        try {
            logger.debug("Before execute action");
            getLiquibaseBuilder().runExecutor(getLiquibaseExecutor());
            logger.debug("Execute action finished");
        } catch (LiquibaseExecuteException e) {
            logger.error("Execute action error", e);
            throw new LiquibaseActionException(this, e);
        }
    }
}
