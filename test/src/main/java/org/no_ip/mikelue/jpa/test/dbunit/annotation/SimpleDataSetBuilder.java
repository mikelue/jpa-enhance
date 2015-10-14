package org.no_ip.mikelue.jpa.test.dbunit.annotation;

import org.dbunit.dataset.IDataSet;

/**
 * This implementation would build {@link IDataSet} by {@link Class#newInstance()}.
 */
public class SimpleDataSetBuilder implements DataSetBuilder {
    public SimpleDataSetBuilder() {}

    @Override
    public IDataSet buildDataSet(Class<? extends IDataSet> classOfDataSet) throws BuildDataSetException
    {
        try {
            return classOfDataSet.newInstance();
        } catch (InstantiationException e) {
            throw new BuildDataSetException(classOfDataSet, e);
        } catch (IllegalAccessException e) {
            throw new BuildDataSetException(classOfDataSet, e);
        }
    }
}
