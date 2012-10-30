package org.no_ip.mikelue.jpa.test.dbunit;

import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.Test;
import org.testng.Assert;

public class DbUnitActionTest extends AbstractDbUnitEnvTestBase {
    public DbUnitActionTest() {}

    /**
     * Test simple case for run test of DBUnit.<p>
     *
     * Because this type is just a wrapper of its containing types,
     * the completed testing is wrote in {@link DbUnitBuilderTest}.<p>
     */
    @Test
    public void buildData()
    {
        DbUnitAction action = new DbUnitAction(getDbUnitBuilder(), DatabaseOperation.INSERT, buildTestData());
        action.executeAction();

        Assert.assertEquals(
            getJdbcTmpl().queryForInt("SELECT COUNT(*) FROM tt_person"),
            2
        );
    }
}
