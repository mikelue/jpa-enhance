package org.no_ip.mikelue.jpa.test.liquibase;

import org.no_ip.mikelue.jpa.test.DatabaseEnvUtil;

import liquibase.Liquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import javax.sql.DataSource;

public class LiquibaseActionTest {
    private JdbcTemplate jdbcTmpl;
    private DataSource dataSource;

    public LiquibaseActionTest() {}

    /**
     * Test simple case for building schema.<p>
     *
     * Because this type is just a wrapper of its containing types,
     * the completed testing is wrote in {@link LiquibaseBuilderTest}.<p>
     */
    @Test
    public void buildSchema()
    {
        LiquibaseBuilder builder = LiquibaseBuilder.build(
            "org/no_ip/mikelue/jpa/test/liquibase/LiquibaseBuilderTest.xml", new ClassLoaderResourceAccessor(),
            dataSource
        );

        LiquibaseExecutor executor =
            new LiquibaseExecutor() {
                @Override
                public void executeLiquibase(Liquibase liquibase) throws Exception
                {
                    liquibase.dropAll();
                    liquibase.update("buildSchema");
                }
            };

        LiquibaseAction action = new LiquibaseAction(builder, executor);
        action.executeAction();

        Assert.assertEquals(
            jdbcTmpl.queryForInt(
                " SELECT COUNT(TABLE_NAME)" +
                " FROM INFORMATION_SCHEMA.TABLES" +
                " WHERE TABLE_NAME = 'TT_SAMPLE_TABLE'"
            ),
            1
        );
    }

    @BeforeClass
    private void prepareDataSource() throws Exception
    {
        dataSource = DatabaseEnvUtil.buildDataSource("LiquibaseActionTest");
        jdbcTmpl = new JdbcTemplate(dataSource);
    }
}
