package org.no_ip.mikelue.jpa.test.liquibase;

import org.no_ip.mikelue.jpa.test.DatabaseEnvUtil;

import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.Liquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.sql.SQLException;
import javax.sql.DataSource;

public class LiquibaseBuilderTest {
    private JdbcTemplate jdbcTmpl;
    private DataSource dataSource;

    public LiquibaseBuilderTest() {}

    /**
     * <p>Test build schema(would drop schema first).</p>
     */
    @Test
    public void buildSchema()
    {
        LiquibaseBuilder liquibaseBuilder = LiquibaseBuilder.build(
            "org/no_ip/mikelue/jpa/test/liquibase/LiquibaseBuilderTest.xml", new ClassLoaderResourceAccessor(),
            dataSource
        );

        liquibaseBuilder.runExecutor(
            new LiquibaseExecutor() {
                @Override
                public void executeLiquibase(Liquibase liquibase) throws Exception
                {
                    liquibase.dropAll();
                    liquibase.update("buildSchema");
                }
            }
        );

        Assert.assertEquals(
            jdbcTmpl.queryForObject(
                " SELECT COUNT(TABLE_NAME)" +
                " FROM INFORMATION_SCHEMA.TABLES" +
                " WHERE TABLE_NAME = 'TT_SAMPLE_TABLE'",
				Integer.class
            ),
            new Integer(1)
        );
    }

    @Test
    public void forceReleaseLock() throws SQLException, LiquibaseException
    {
        LiquibaseBuilder liquibaseBuilder = LiquibaseBuilder.build(
            "org/no_ip/mikelue/jpa/test/liquibase/LiquibaseBuilderTest.xml", new ClassLoaderResourceAccessor(),
            dataSource
        );

        /**
         * Execute a changeset which would throw exception
         */
        try {
            liquibaseBuilder.runExecutor(
                new LiquibaseExecutor() {
                    @Override
                    public void executeLiquibase(Liquibase liquibase) throws Exception
                    {
                        liquibase.update("forceReleaseLock");
                    }
                }
            );
        } catch (Exception e) {}
        // :~)

        Liquibase liquibase = null;
        try {
            liquibase = new Liquibase(
                "org/no_ip/mikelue/jpa/test/liquibase/LiquibaseBuilderTest.xml", new ClassLoaderResourceAccessor(),
                new JdbcConnection(dataSource.getConnection())
            );
            Assert.assertEquals(
                liquibase.listLocks().length, 0,
                "There should not be any lock."
            );
        } finally {
            liquibase.getDatabase().close();
        }
    }

    @BeforeClass
    private void prepareDataSource() throws Exception
    {
        dataSource = DatabaseEnvUtil.buildDataSource("LiquibaseBuilderTest");
        jdbcTmpl = new JdbcTemplate(dataSource);
    }
}
