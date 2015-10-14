package guru.mikelue.jpa.test.dbunit.annotation;

import org.dbunit.dataset.IDataSet;

/**
 * This class is the wrapper type of exception when building {@link IDataSet}.
 */
public class BuildDataSetException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct with built {@link IDataSet} and thrown exception
     *
     * @param classOfDataSet The type of built {@link IDataSet}
     * @param e The thrown exception
     */
    public BuildDataSetException(Class<? extends IDataSet> classOfDataSet, Exception e)
    {
        super(
            String.format(
                "Build DataSet Error for: [%s]", classOfDataSet.getSimpleName()
            ),
            e
        );
    }
}
