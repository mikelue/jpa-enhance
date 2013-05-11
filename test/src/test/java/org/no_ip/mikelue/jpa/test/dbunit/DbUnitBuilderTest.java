package org.no_ip.mikelue.jpa.test.dbunit;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class DbUnitBuilderTest extends AbstractDbUnitEnvTestBase {
    public DbUnitBuilderTest() {}

	/**
	 * Tests the building of data(in transaction) while a error occurs.<p>
	 */
	@Test(expectedExceptions=DatabaseUnitException.class, expectedExceptionsMessageRegExp=".*tt_person.*")
	public void buildDataWithErrorInTransaction() throws DatabaseUnitException
	{
        DbUnitBuilder builder = getDbUnitBuilder();
		builder.setRunAsTransaction(true);

		try {
			builder.runDatabaseOperation(
				new YamlDataSet(
					" tt_person: \n" +
					" - {ps_id: 1, ps_name: \"Name of 1\"}\n" +
					" - {ps_id: 2, ps_name: \"Name of 2\"}\n" +
					" - {ps_id: 1, ps_name: \"Name of 1\"}\n" // Violation of primary key
				),
				DatabaseOperation.INSERT
			);
		} catch (DbUnitExecuteException e) {
			/**
			 * Assert that there is no data that can be inserted because of rollback of transaction
			 */
			Assert.assertEquals(
				getJdbcTmpl().queryForInt("SELECT COUNT(*) FROM tt_person"),
				0
			);
			// :~)

			throw (DatabaseUnitException)e.getCause();
		}

		Assert.fail("Uneffective test");
	}

    /**
     * Tests building data(with HSQLDB, in a transaction).<p>
     */
    @Test
    public void buildData()
    {
        DbUnitBuilder builder = getDbUnitBuilder();
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
     * Tests the callback for {@ink DbUnitConnectionConfigurer}.<p>
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
