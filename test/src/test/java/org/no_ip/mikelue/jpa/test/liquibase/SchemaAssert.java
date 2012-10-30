package org.no_ip.mikelue.jpa.test.liquibase;

import org.testng.Assert;
import org.testng.TestException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;

/**
 * Assert the schema.<p>
 */
public class SchemaAssert {
    private SimpleJdbcTemplate jdbcTmpl;

    public SchemaAssert(DataSource newDataSource)
    {
        jdbcTmpl = new SimpleJdbcTemplate(newDataSource);
    }

    /**
     * Assert that a table should exists.<p>
     */
    public void assertTableExists(String tableName)
    {
        if (
            !jdbcTmpl.queryForObject(
                " SELECT COUNT(TABLE_NAME)" +
                " FROM INFORMATION_SCHEMA.TABLES" +
                " WHERE TABLE_NAME = ?",
                Integer.class, tableName
            ).equals(1)
        ) {
            Assert.fail(String.format("Table[%s] should exist, but not.", tableName));
        }
    }
    /**
     * Assert that a table shouldn't exists.<p>
     */
    public void assertTableNotExists(String tableName)
    {
        if (
            !jdbcTmpl.queryForObject(
                " SELECT COUNT(TABLE_NAME)" +
                " FROM INFORMATION_SCHEMA.TABLES" +
                " WHERE TABLE_NAME = ?",
                Integer.class, tableName
            ).equals(0)
        ) {
            Assert.fail(String.format("Table[%s] should not exist, but not.", tableName));
        }
    }
}
