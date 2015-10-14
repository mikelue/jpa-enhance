package guru.mikelue.jpa.test.liquibase;

import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.Liquibase;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * This builder object is used to initialized environment of {@link Liquibase} and
 * to execute {@link Liquibase} in IoC.
 *
 * <p>Supporting with {@link DataSource}, client could using this builder to execute
 * actions of {@link Liquibase} by providing implementation of {@link LiquibaseExecutor}.</p>
 *
 * @see #runExecutor
 * @see Liquibase
 */
public class LiquibaseBuilder {
    private static Logger logger = LoggerFactory.getLogger(LiquibaseBuilder.class);

    /**
     * This method builds a {@link LiquibaseBuilder} as same as {@link Liquibase#Liquibase},
     * but the connection of database is constructured by {@link DataSource}.
     *
     * @param newChangeLogFile The path of file for Liquibase change file
     * @param newResourceAccessor The object for locating Liquibase change files
     * @param newDataSource The connection of database
     *
     * @return Initialzed environment for building {@link Liquibase}
     */
    public static LiquibaseBuilder build(
        String newChangeLogFile, ResourceAccessor newResourceAccessor, DataSource newDataSource
    ) {
        logger.info(
            "Build Liquibase builder: {}", newChangeLogFile
        );
        return new LiquibaseBuilder(newChangeLogFile, newResourceAccessor, newDataSource);
    }

    private String changeLogFile;
    private ResourceAccessor resourceAccessor;
    private DataSource dataSource;
    private LiquibaseBuilder(
        String newChangeLogFile, ResourceAccessor newResourceAccessor, DataSource newDataSource
    ) {
        changeLogFile = newChangeLogFile;
        resourceAccessor = newResourceAccessor;
        dataSource = newDataSource;
    }

    /**
     * Get the path of change log file.
     *
     * @return The path of change log file comes from {@link #build}
     */
    public String getChangeLogFile()
    {
        return changeLogFile;
    }

    /**
     * This method would build new {@link Liquibase} object and sent it into
     * {@link LiquibaseExecutor#executeLiquibase} method.
     *
     * <p><b style="color:red">
     * This method would release locks forcely before and after the running of executor.
     * Do not use this method to production database.
     * </b></p>
     *
     * @param liquibaseExecutor The {@link LiquibaseExecutor} is going to be executed
     *
     * @throws LiquibaseExecuteException The exception thrown if there is an error while executing
     */
    public void runExecutor(LiquibaseExecutor liquibaseExecutor) throws LiquibaseExecuteException
    {
        /**
         * Initialize database connection
         */
        DatabaseConnection dbConn;
        try {
            logger.info("Initialize connection");
            dbConn = new JdbcConnection(dataSource.getConnection());
        } catch (SQLException e) {
            logger.error("Build database connection error", e);
            throw new LiquibaseExecuteException(this, e);
        }
        // :~)

        /**
         * Execute Liquibase
         */
        Boolean originalAutoCommit = true; // Used to reset the autocommit value when using connection pooling
        try {
            originalAutoCommit = dbConn.getAutoCommit();
            logger.info("Original \"auto-commit\" of connection: {}", originalAutoCommit);

            doRunExecutor(dbConn, liquibaseExecutor);
        } catch (DatabaseException e) {
            logger.error("Get auto-commit error", e);
            throw new LiquibaseExecuteException(this, e);
        } catch (LiquibaseExecuteException e) {
            logger.error("Execute liquibase error", e);
            throw e;
        } catch (Exception e) {
            logger.error("Execute liquibase error", e);
            throw new LiquibaseExecuteException(this, e);
        } finally {
            releaseConnection(dbConn, originalAutoCommit);
        }
        // :~)
    }

    private void doRunExecutor(DatabaseConnection dbConn, LiquibaseExecutor liquibaseExecutor)
    {
        /**
         * Execute Liquibase
         */
        Liquibase liquibase = null;
        try {
            logger.info("Initialize Liquibase object");
            liquibase = new Liquibase(
                getChangeLogFile(), resourceAccessor, dbConn
            );

            logger.debug("Before execute Liquibase");

            /**
             * Ensure that the lock of Liquibase is clean
             */
            forceReleaseLocks(liquibase);
            liquibaseExecutor.executeLiquibase(liquibase);
            // :~)

            logger.info("Execute Liquibase successfully");
        } catch (Exception e) {
            logger.error("Execute liquibase", e);
            throw new LiquibaseExecuteException(this, e);
        } finally {
            forceReleaseLocks(liquibase);
        }
        // :~)
    }

    private void forceReleaseLocks(Liquibase liquibase)
    {
        try {
            int locks = liquibase.listLocks().length;
            if (locks == 0) {
                return;
            }

            logger.warn("Release locks forcely: ", locks);
            liquibase.forceReleaseLocks();
        } catch (LiquibaseException e) {
            logger.error("Release locks error", e);
            throw new LiquibaseExecuteException(this, e);
        }
    }
    private void releaseConnection(DatabaseConnection dbConn, Boolean originalAutoCommit)
    {
        try {
            if (dbConn == null || dbConn.isClosed()) {
                return;
            }
        } catch (DatabaseException e) {
            throw new LiquibaseExecuteException(this, e);
        }

        try {
            /**
             * !IMPORTANT!
             * The connection polling would recycle the connection which is "autocommit = 0" to another
             * module which thought itself is in "autocommit = 1".
             *
             * This may cause the dead-lock if the second module didn't send "commit".
             */
            logger.info("Reset to original value of \"auto-commit\": {}", originalAutoCommit);
            dbConn.setAutoCommit(originalAutoCommit);
            // :~)

            dbConn.close();
            logger.debug("Close database connection");
        } catch (Exception e) {
            logger.error("Close database connection error", e);
            throw new LiquibaseExecuteException(this, e);
        }
    }
}
