package org.no_ip.mikelue.jpa.test.dbunit;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.IDataTypeFactory;

import javax.sql.DataSource;

/**
 * This interface is implmented by client who wants to configure {@link IDatabaseConnection}
 * before executing database operation.
 *
 * @see DbUnitBuilder#build(DataSource, IDataTypeFactory, DbUnitConnectionConfigurer)
 */
public interface DbUnitConnectionConfigurer {
    /**
     * This method gets called with an initialized connection which
     * client needs to setup.
     *
     * @param conn the initialized object of connection
     *
     * @throws Exception Any exception comes from DBUnit for convenience of setting.
     */
    public void config(IDatabaseConnection conn) throws Exception;
}
