package guru.mikelue.jpa.test.dbunit;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

/**
 * This is a wrapper exception while running from {@link DbUnitBuilder#runDatabaseOperation(IDataSet, DatabaseOperation)}.
 */
public class DbUnitExecuteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct this class with {@link Exception cause exception}.
     *
     * @param builder The running builider
     * @param e The causing exception
     */
    public DbUnitExecuteException(DbUnitBuilder builder, Exception e)
    {
        super(
            String.format("Execute DbUnit error: [%s]", builder.toString()),
            e
        );
    }
}
