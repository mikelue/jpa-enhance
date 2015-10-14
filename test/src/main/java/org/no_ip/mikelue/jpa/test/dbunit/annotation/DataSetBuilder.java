package org.no_ip.mikelue.jpa.test.dbunit.annotation;

import org.dbunit.dataset.IDataSet;

/**
 * This interface is used to customize the building of {@link IDataSet} object
 * with different platforms.
 */
public interface DataSetBuilder {
    /**
     * Implements this method to provide building mechanism for {@link IDataSet} object.
     *
     * @param classOfDataSet The class of {@link IDataSet}
     *
     * @return Initialized {@link IDataSet} object
     *
     * @throws BuildDataSetException The wrapper excpetion when building data fail
     */
    public IDataSet buildDataSet(Class<? extends IDataSet> classOfDataSet) throws BuildDataSetException;
}
