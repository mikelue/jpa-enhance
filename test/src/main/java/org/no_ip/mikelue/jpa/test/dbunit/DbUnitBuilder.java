package org.no_ip.mikelue.jpa.test.dbunit;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.TransactionOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY;

import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * This class is a builder class for running {@link DatabaseOperation}.<p>
 *
 * This class only supports {@link DataSource} currently.<p>
 */
public class DbUnitBuilder {
    private static Logger logger = LoggerFactory.getLogger(DbUnitBuilder.class);
    private boolean runAsTransaction = false;

    /**
     * This configurer does nothing when gets called.
     *
     * This is the default configurer if client doesn't provide one.
     */
    private static class DoNothingConfigurer implements DbUnitConnectionConfigurer {
        @Override
        public void config(IDatabaseConnection conn) throws Exception {}
    }

    /**
     * Building this object by {@link DataSource} and {@link IDataTypeFactory}.<p>
     *
     * @param newDataSource the data source
     * @param newDataTypeFactory the data type factory
     *
     * @return builder object for running database operation
     *
     * @see #runDatabaseOperation
     */
    public static DbUnitBuilder build(DataSource newDataSource, IDataTypeFactory newDataTypeFactory)
    {
        return new DbUnitBuilder(newDataSource, newDataTypeFactory, new DoNothingConfigurer());
    }
    /**
     * Building this object by {@link DataSource} and {@link IDataTypeFactory}.<p>
     *
     * Additionally, this builder method allows client to customize {@link DatabaseDataSourceConnection} by
     * implementing {@link DbUnitConnectionConfigurer}.<p>
     *
     * @param newDataSource the data source
     * @param newDataTypeFactory the data type factory
     * @param newConnConfigurer the callback object for customizing {@link DatabaseDataSourceConnection}
     *
     * @return builder object for running database operation
     *
     * @see #runDatabaseOperation
     */
    public static DbUnitBuilder build(DataSource newDataSource, IDataTypeFactory newDataTypeFactory, DbUnitConnectionConfigurer newConnConfigurer)
    {
        return new DbUnitBuilder(newDataSource, newDataTypeFactory, newConnConfigurer);
    }

    private DataSource dataSource;
    private IDataTypeFactory dataTypeFactory;
    private DbUnitConnectionConfigurer connConfigurer;

    private DbUnitBuilder(DataSource newDataSource, IDataTypeFactory newDataTypeFactory, DbUnitConnectionConfigurer newConnConfigurer)
    {
        logger.info(
            "Initialize DbUnitBuilder: [{}] : [{}]",
            newDataSource.getClass().getSimpleName(),
            newDataTypeFactory.getClass().getSimpleName()
        );

        dataSource = newDataSource;
        dataTypeFactory = newDataTypeFactory;
        connConfigurer = newConnConfigurer;
    }

    /**
     * This method run database operation and throw wrapping {@link DbUnitExecuteException} comes from
     * any DBUnit methods.<p>
     *
     * @param sourceDataSet the source data set
     * @param dbOperation the operation run to database
     */
    public void runDatabaseOperation(
        IDataSet sourceDataSet, DatabaseOperation dbOperation
    ) {
        IDatabaseConnection dbConn = null;

        /**
         * Prepare and configure database connection
         */
        try {
            dbConn = new DatabaseDataSourceConnection(dataSource);
        } catch (Exception e) {
            logger.error("Preparing database connection error", e);
            throw new DbUnitExecuteException(this, e);
        }
        // :~)

        /**
         * Execute the operation and ensure that the connection will be released completely
         */
        try {
            doRunDatabaseOperation(dbConn, sourceDataSet, dbOperation);
        } catch (DbUnitExecuteException e) {
            throw e;
        } catch (Exception e) {
            throw new DbUnitExecuteException(this, e);
        } finally {
            releaseConnection(dbConn);
        }
        // :~)
    }

    /**
     * Checks whether the operation of dataset is run in transaction.<p>
     *
     * @see #setRunAsTransaction
     */
    public boolean isRunAsTransaction()
    {
        return runAsTransaction;
    }
    /**
     * Setup whether the operation of dataset is run in transaction.<p>
     *
     * @see #setRunAsTransaction
     */
    public void setRunAsTransaction(boolean newRunAsTransaction)
    {
        runAsTransaction = newRunAsTransaction;
    }

    private void doRunDatabaseOperation(
        IDatabaseConnection dbConn, IDataSet sourceDataSet, DatabaseOperation dbOperation
    ) {
        /**
         * Configure database connection
         */
        try {
            dbConn.getConfig().setProperty(
                PROPERTY_DATATYPE_FACTORY, dataTypeFactory
            );
        } catch (Exception e) {
            logger.error("Preparing database connection error", e);
            throw new DbUnitExecuteException(this, e);
        }

        try {
            connConfigurer.config(dbConn);
        } catch (Exception e) {
            logger.error("Customize database connection error", e);
            throw new DbUnitExecuteException(this, e);
        }
        // :~)

        logger.debug("Before executing database operation[{}].", dbOperation);

        /**
         * Execute operation
         */
        try {
            if (logger.isInfoEnabled()) {
                logger.info(
                    "Perform DataSet: [{}] : Operation: [{}]",
                    sourceDataSet.getTableNames(), dbOperation.getClass().getSimpleName()
                );
            }

            /**
             * Process whether running operation in a transaction
             */
            if (runAsTransaction) {
                logger.debug("Uses transactional operation");
				dbOperation = new TransactionOperation(dbOperation);
            }

            dbOperation.execute(dbConn, sourceDataSet);
            // :~)
        } catch (Exception e) {
            logger.error("Executing database operation error", e);
            throw new DbUnitExecuteException(this, e);
        }
        // :~)

		if (logger.isDebugEnabled()) {
			logger.debug("Executing database operation[{}] successfully.", dbOperation.getClass().getSimpleName());
		}
    }

    @Override
    public String toString()
    {
        return String.format(
            "[%s] : [%s]",
            dataSource.getClass().getSimpleName(),
            dataTypeFactory.getClass().getSimpleName()
        );
    }

    private void releaseConnection(IDatabaseConnection conn)
    {
        if (conn == null) {
            return;
        }

		try {
			conn.close();
			logger.debug("Close database connection");
		} catch (SQLException e) {
			logger.error("Closing database connection error", e);
			throw new DbUnitExecuteException(this, e);
		}
    }
}
