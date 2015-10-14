package guru.mikelue.jpa.test.dbunit.annotation;

import guru.mikelue.jpa.test.dbunit.DbUnitAction;
import guru.mikelue.jpa.test.dbunit.DbUnitBuilder;
import guru.mikelue.jpa.test.testng.Action;
import guru.mikelue.jpa.test.testng.ActionListener;
import guru.mikelue.jpa.test.testng.ChainedActionListener;
import static guru.mikelue.jpa.test.dbunit.annotation.DataSetOperation.None;

import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an context class which is used to execute the logic defined in {@link OpDataSet}.
 *
 * <p>Client should provides {@link DbUnitBuilder} and {@link DataSetBuilder} to construct this object.</p>
 *
 * @see OpDataSet
 * @see DataSetOperation
 */
public class AnnotationDbUnitContext {
    private Logger logger = LoggerFactory.getLogger(AnnotationDbUnitContext.class);

    private DbUnitBuilder dbUnitBuilder;
    private DataSetBuilder dataSetBuilder;

    /**
     * Initialize this object with fundamental environment.
     *
     * @param newDbUnitBuilder The DbUnit environment
     * @param newDataSetBuilder The builder for generating {@link IDataSet}
     */
    public AnnotationDbUnitContext(DbUnitBuilder newDbUnitBuilder, DataSetBuilder newDataSetBuilder)
    {
        dbUnitBuilder = newDbUnitBuilder;
        dataSetBuilder = newDataSetBuilder;
    }
    /**
     * Initialize this object with fundamental environment.
	 *
     * <p>This constructor would use {@link SimpleDataSetBuilder} as default {@link DataSetBuilder}.</p>
     *
     * @param newDbUnitBuilder The DbUnit environment
     */
    public AnnotationDbUnitContext(DbUnitBuilder newDbUnitBuilder)
    {
        this(newDbUnitBuilder, new SimpleDataSetBuilder());
    }

    /**
     * Perform {@link DataSetOperation dataset operation} to database with "before action" meaning.
     *
     * <p>This method would use {@link #getDataSetBuilder()} to retrive the {@link IDataSet}.</p>
     *
     * @param opDataSet The defining object for operation
     */
    public void beforeOperation(OpDataSet opDataSet)
    {
        checkAndWarnMeaninglessOperation(opDataSet);

        /**
         * Build ChainedActionListener for multiple datasets
         */
        Class<? extends IDataSet>[] clazzOfDataSet = opDataSet.dataSetClazz();
        ActionListener[] listenersForDataSets = new ActionListener[clazzOfDataSet.length];
        for (int i = 0; i < clazzOfDataSet.length; i++) {
            logger.debug("Build class: {}", clazzOfDataSet[i].toString());
            listenersForDataSets[i] = new DbUnitContextBeforeActionListener(
                clazzOfDataSet[i], opDataSet.beforeOperation()
            );
        }
        ActionListener multiDataSetsListener = new ChainedActionListener(listenersForDataSets);
        // :~)

        logger.debug("Execute before database operation");
        multiDataSetsListener.executeBeforeAction();
    }
    /**
     * Perform {@link DataSetOperation dataset operation} to database with "after action" meaning.
     *
     * <p>This method would use {@link #getDataSetBuilder()} to retrive the {@link IDataSet}.</p>
     *
     * @param opDataSet The defining object for operation
     */
    public void afterOperation(OpDataSet opDataSet)
    {
        checkAndWarnMeaninglessOperation(opDataSet);

        /**
         * Build ChainedActionListener for multiple datasets
         */
        Class<? extends IDataSet>[] clazzOfDataSet = opDataSet.dataSetClazz();
        ActionListener[] listenersForDataSets = new ActionListener[clazzOfDataSet.length];
        for (int i = 0; i < clazzOfDataSet.length; i++) {
            logger.debug("Build class: {}", clazzOfDataSet[i].toString());
            listenersForDataSets[i] = new DbUnitContextAfterActionListener(
                clazzOfDataSet[i], opDataSet.afterOperation()
            );
        }
        ActionListener multiDataSetsListener = new ChainedActionListener(listenersForDataSets);
        // :~)

        logger.debug("Execute after database operation");
        multiDataSetsListener.executeAfterAction();
    }

    /**
     * Gets the {@link DbUnitBuilder} for executing {@link DataSetOperation operation}.
     *
     * @return The builder which was sent from {@link #AnnotationDbUnitContext(DbUnitBuilder, DataSetBuilder) constructor}
     *
     * @see DatabaseOperation
     */
    public DbUnitBuilder getDbUnitBuilder()
    {
        return dbUnitBuilder;
    }
    /**
     * Gets the {@link DataSetBuilder} for generating {@link IDataSet}.
     *
     * @return The builder which was sent from {@link #AnnotationDbUnitContext(DbUnitBuilder, DataSetBuilder) constructor}
     */
    public DataSetBuilder getDataSetBuilder()
    {
        return dataSetBuilder;
    }

    private void checkAndWarnMeaninglessOperation(OpDataSet opDataSet)
    {
        if (opDataSet.dataSetClazz().length == 0) {
            logger.warn("Nothing to operate.");
        }

        if (opDataSet.beforeOperation() == None && opDataSet.afterOperation() == None) {
            logger.warn("Operations are none in both before and after action.");
        }
    }

    private class DbUnitContextBeforeActionListener extends ActionListener {
        private Action dataSetAction;

        DbUnitContextBeforeActionListener(Class<? extends IDataSet> classOfDataSet, DataSetOperation dataSetOp)
        {
            dataSetAction = new DbUnitAction(
                getDbUnitBuilder(), dataSetOp.getDatabaseOperation(),
                getDataSetBuilder().buildDataSet(classOfDataSet)
            );
        }

        @Override
        public Action getBeforeAction()
        {
            return dataSetAction;
        }
    }
    private class DbUnitContextAfterActionListener extends ActionListener {
        private Action dataSetAction;

        DbUnitContextAfterActionListener(Class<? extends IDataSet> classOfDataSet, DataSetOperation dataSetOp)
        {
            dataSetAction = new DbUnitAction(
                getDbUnitBuilder(), dataSetOp.getDatabaseOperation(),
                getDataSetBuilder().buildDataSet(classOfDataSet)
            );
        }

        @Override
        public Action getAfterAction()
        {
            return dataSetAction;
        }
    }
}
