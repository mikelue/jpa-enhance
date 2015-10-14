package org.no_ip.mikelue.jpa.test.liquibase;

import org.no_ip.mikelue.jpa.test.DatabaseEnvUtil;

import liquibase.resource.ClassLoaderResourceAccessor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import javax.sql.DataSource;

public class RollbackSchemaExecutorTest {
    private SchemaAssert schemaAssert;
    private LiquibaseBuilder liquibaseBuilder;

    public RollbackSchemaExecutorTest() {}

    /**
     * <p>Test the updating with particular context.</p>
     */
    @Test(dependsOnMethods="rollbackWithChangesToRollbackTo")
    public void rollbackWithContext()
    {
        RollbackSchemaExecutor executor = new RollbackSchemaExecutor(4);
        executor.setContexts("c1");
        liquibaseBuilder.runExecutor(executor);

        schemaAssert.assertTableNotExists("TT_USER");
        schemaAssert.assertTableNotExists("TT_PRODUCT");
        schemaAssert.assertTableExists("TT_ITEM");
    }
    /**
     * <p>Test the updating with particular tag.</p>
     */
    @Test
    public void rollbackWithTag()
    {
        RollbackSchemaExecutor executor = new RollbackSchemaExecutor("1.1");
        liquibaseBuilder.runExecutor(executor);

        schemaAssert.assertTableExists("TT_USER");
        schemaAssert.assertTableExists("TT_PRODUCT");
        schemaAssert.assertTableNotExists("TT_ITEM");
    }
    /**
     * <p>Test the updating with particular number of changes.</p>
     */
    @Test
    public void rollbackWithChangesToRollbackTo()
    {
        RollbackSchemaExecutor executor = new RollbackSchemaExecutor(4);
        liquibaseBuilder.runExecutor(executor);

        schemaAssert.assertTableNotExists("TT_USER");
        schemaAssert.assertTableNotExists("TT_PRODUCT");
        schemaAssert.assertTableNotExists("TT_ITEM");
    }

    /**
     * Seperate each database with each test method
     */
    @BeforeMethod
    public void setupEnvironment(Method method)
    {
        DataSource dataSource = DatabaseEnvUtil.buildDataSource("RollbackSchemaExecutorTest." + method.getName());

        liquibaseBuilder = LiquibaseBuilder.build(
            "org/no_ip/mikelue/jpa/test/liquibase/RollbackSchemaExecutorTest.xml", new ClassLoaderResourceAccessor(),
            dataSource
        );
        schemaAssert = new SchemaAssert(dataSource);

        // Update the schema with full of tables before executing rollback test
        liquibaseBuilder.runExecutor(new UpdateSchemaExecutor());
    }
}
