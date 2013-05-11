package org.no_ip.mikelue.jpa.test;

import org.hsqldb.jdbc.JDBCDataSourceFactory;

import java.util.Properties;
import javax.sql.DataSource;

/**
 * This class is used to build database environment for testing.
 */
public class DatabaseEnvUtil {
    private DatabaseEnvUtil() {}

    /**
     * This method constructs a {@link DataSource} with {@link JDBCDataSourceFactory} and
     * initialzie HSQLDB in "target/hsqldb/$dbName" as URL.<p>
     *
     * @param dbName the name of database in HSQLDB URL
     */
    public static DataSource buildDataSource(String dbName)
    {
        Properties prop = new Properties();
		prop.put("url", "jdbc:hsqldb:file:target/hsqldb/" + dbName + ";hsqldb.sqllog=2;hsqldb.tx=mvcc");
        prop.put("user", "any");
        prop.put("password", "any");

        try {
            return JDBCDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
