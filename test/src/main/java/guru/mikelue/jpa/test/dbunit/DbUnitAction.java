package guru.mikelue.jpa.test.dbunit;

import guru.mikelue.jpa.test.testng.Action;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The container type which could be execute a {@link DatabaseOperation}
 * in predefined environment.
 *
 * <p>The goal of this type is to provide a single entry to execute
 * an "<b>action</b>" of DbUnit dataset in convenient way.</p>
 *
 * <p>The {@link #executeAction()} method is used to execute {@link #getDbUnitOperation()} by {@link #getDbUnitBuilder()}.</p>
 */
public class DbUnitAction implements Action {
    private Logger logger = LoggerFactory.getLogger(DbUnitAction.class);
    private DbUnitBuilder builder;
    private DatabaseOperation databaseOperation;
    private IDataSet dataset;

    /**
     * Constructs this object with necessary properties.
     *
     * @param newDbUnitBuilder The builder for running {@link DatabaseOperation}
     * @param newDatabaseOperation The executor gotten called by {@link DbUnitBuilder}
     * @param newDataset The dataset which contains data to be operated
     *
     * @see #getDbUnitBuilder()
     * @see #getDbUnitOperation()
     * @see #getDbUnitDataSet()
     */
    public DbUnitAction(
        DbUnitBuilder newDbUnitBuilder, DatabaseOperation newDatabaseOperation,
        IDataSet newDataset
    ) {
        logger.debug("Initialize DbUnit action");
        builder = newDbUnitBuilder;
        databaseOperation = newDatabaseOperation;
        dataset = newDataset;
    }

    /**
     * Gets the builder contained by this object.
     *
     * @return The builder to run {@link #getDbUnitOperation()}
     */
    public DbUnitBuilder getDbUnitBuilder()
    {
        return builder;
    }
    /**
     * Gets the operation of DBUnit.
     *
     * @return The operation which is going to be executed by {@link #getDbUnitBuilder()}
     */
    public DatabaseOperation getDbUnitOperation()
    {
        return databaseOperation;
    }

    /**
     * Gets the {@link IDataSet} for operating.
     *
     * @return The dataset which is going to operated
     */
    public IDataSet getDbUnitDataSet()
    {
        return dataset;
    }

    /**
     * Executes {@link #getDbUnitOperation()} by {@link #getDbUnitBuilder()}.
     *
     * @throws DbUnitActionException The thrown exception if there is an error when executing action
     */
    @Override
    public void executeAction() throws DbUnitActionException
    {
        try {
            logger.debug("Before execute action");
            getDbUnitBuilder().runDatabaseOperation(
                getDbUnitDataSet(), getDbUnitOperation()
            );
            logger.debug("Execute action finished");
        } catch (DbUnitExecuteException e) {
            logger.error("Execute action error", e);
            throw new DbUnitActionException(this, e);
        }
    }
}
