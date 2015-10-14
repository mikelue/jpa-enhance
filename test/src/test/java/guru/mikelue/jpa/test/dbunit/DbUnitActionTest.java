package guru.mikelue.jpa.test.dbunit;

import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.Test;
import org.testng.Assert;

public class DbUnitActionTest extends AbstractDbUnitEnvTestBase {
    public DbUnitActionTest() {}

    /**
     * <p>Test simple case for run test of DBUnit.</p>
     *
     * Because this type is just a wrapper of its containing types,
     * <p>the completed testing is wrote in {@link DbUnitBuilderTest}.</p>
     */
    @Test
    public void buildData()
    {
        DbUnitAction action = new DbUnitAction(getDbUnitBuilder(), DatabaseOperation.INSERT, buildTestData());
        action.executeAction();

        Assert.assertEquals(
            getJdbcTmpl().queryForObject("SELECT COUNT(*) FROM tt_person", Integer.class),
            new Integer(2)
        );
    }
}
