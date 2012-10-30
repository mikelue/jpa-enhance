package org.no_ip.mikelue.jpa.test.dbunit;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

public class DbUnitBuilderTest extends AbstractDbUnitEnvTestBase {
    public DbUnitBuilderTest() {}

    /**
     * Test building data(with HSQLDB, in a transaction).<p>
     */
    @Test
    public void buildData()
    {
        DbUnitBuilder builder = DbUnitBuilder.build(
            getDataSource(), getDataTypeFactory()
        );
        builder.setRunAsTransaction(true);

        builder.runDatabaseOperation(
            buildTestData(), DatabaseOperation.INSERT
        );

        Assert.assertEquals(
            getJdbcTmpl().queryForInt("SELECT COUNT(*) FROM tt_person"),
            2
        );
    }

    /**
     * Test the callback for {@ink DbUnitConnectionConfigurer}.<p>
     */
    @Test(dependsOnMethods="buildData")
    public void connectionConfigurer()
    {
        class FakeConfigurer implements DbUnitConnectionConfigurer {
            boolean called = false;

            FakeConfigurer() {}

            @Override
            public void config(IDatabaseConnection conn) throws Exception
            {
                called = true;
            }
        }

        FakeConfigurer configurer = new FakeConfigurer();

        DbUnitBuilder builder = DbUnitBuilder.build(
            getDataSource(), getDataTypeFactory(), configurer
        );

        builder.runDatabaseOperation(
            buildTestData(), DatabaseOperation.INSERT
        );

        Assert.assertTrue(configurer.called);
    }

    @AfterMethod
    private void cleanData()
    {
        getJdbcTmpl().update("DELETE FROM tt_person");
    }
}
