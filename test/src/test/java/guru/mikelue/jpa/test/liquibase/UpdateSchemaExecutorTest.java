package guru.mikelue.jpa.test.liquibase;

import guru.mikelue.jpa.test.DatabaseEnvUtil;

import liquibase.resource.ClassLoaderResourceAccessor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import javax.sql.DataSource;

public class UpdateSchemaExecutorTest {
    private SchemaAssert schemaAssert;
    private LiquibaseBuilder liquibaseBuilder;

    public UpdateSchemaExecutorTest() {}

    /**
     * <p>Test the updating with the default behaviour.</p>
     */
    @Test
    public void defaultUpdate()
    {
        LiquibaseExecutor executor = new UpdateSchemaExecutor();
        liquibaseBuilder.runExecutor(executor);

        schemaAssert.assertTableExists("TT_USER");
        schemaAssert.assertTableExists("TT_PRODUCT");
        schemaAssert.assertTableExists("TT_ITEM");
    }
    /**
     * <p>Test the updating with particular context.</p>
     */
    @Test
    public void updateWithContext()
    {
        UpdateSchemaExecutor executor = new UpdateSchemaExecutor();
        executor.setContexts("c1");
        liquibaseBuilder.runExecutor(executor);

        schemaAssert.assertTableExists("TT_USER");
        schemaAssert.assertTableExists("TT_PRODUCT");
        schemaAssert.assertTableNotExists("TT_ITEM");
    }
    /**
     * <p>Test the updating with particular number of changes.</p>
     */
    @Test(dependsOnMethods="updateWithDropFirst")
    public void updateWithChangesToApply()
    {
        UpdateSchemaExecutor executor = new UpdateSchemaExecutor();
        executor.setDropFirst(true);
        executor.setChangesToApply(1);
        liquibaseBuilder.runExecutor(executor);

        schemaAssert.assertTableExists("TT_USER");
        schemaAssert.assertTableNotExists("TT_PRODUCT");
        schemaAssert.assertTableNotExists("TT_ITEM");
    }
    /**
     * <p>Test the updating with dropping schema first.</p>
     *
     * <p>This test would execute update database twice(with "runAlways=true").</p>
     */
    @Test
    public void updateWithDropFirst()
    {
        /**
         * Prepare independent environment
         */
        DataSource dataSource = DatabaseEnvUtil.buildDataSource("UpdateSchemaExecutorTest.updateWithDropFirst");
        LiquibaseBuilder liquibaseBuilder = LiquibaseBuilder.build(
            "guru/mikelue/jpa/test/liquibase/UpdateSchemaExecutorTest-updateWithDropFirst.xml", new ClassLoaderResourceAccessor(),
            dataSource
        );
        SchemaAssert schemaAssert = new SchemaAssert(dataSource);
        // :~)

        /**
         * Execute update twice
         */
        UpdateSchemaExecutor executor = new UpdateSchemaExecutor();
        executor.setDropFirst(true);

        liquibaseBuilder.runExecutor(executor);
        liquibaseBuilder.runExecutor(executor);
        // :~)

        schemaAssert.assertTableExists("TT_USER");
    }

    /**
     * Seperate each database with each test method
     */
    @BeforeMethod
    public void setupEnvironment(Method method)
    {
        DataSource dataSource = DatabaseEnvUtil.buildDataSource("UpdateSchemaExecutorTest." + method.getName());

        liquibaseBuilder = LiquibaseBuilder.build(
            "guru/mikelue/jpa/test/liquibase/UpdateSchemaExecutorTest.xml", new ClassLoaderResourceAccessor(),
            dataSource
        );
        schemaAssert = new SchemaAssert(dataSource);
    }
}
