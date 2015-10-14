package org.no_ip.mikelue.jpa.test.testng;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.ITestNGMethod;

public class TestNGUtilTest {
    public TestNGUtilTest() {}

    @Mocked
    private ITestNGMethod mockMethod;

    /**
     * <p>Test the checking for belonging groups of method.</p>
     */
    @Test(dataProvider="IsMethodBelongGroups")
    public void isMethodBelongGroups(
        final String[] groupsOfTestMethod, String[] checkGroups, boolean expectedResult
    ) {
        new NonStrictExpectations() {{
            mockMethod.getGroups();
            result = groupsOfTestMethod;
        }};

        Assert.assertEquals(
            TestNGUtil.isMethodBelongGroups(mockMethod, checkGroups),
            expectedResult
        );
    }

    @DataProvider(name="IsMethodBelongGroups")
    private Object[][] getIsMethodBelongGroups()
    {
        return new Object[][] {
            { new String[] {"g1"}, new String[] { "g1" }, true }, // Single groups in both
            { null, new String[] { "no-group" }, false }, // null groups in method
            { new String[0], new String[] { "no-group" }, false }, // empty groups in method
            { new String[] {"g2"}, new String[] { "non-g2" }, false }, // Simple not match
            { new String[] {"diff-1", "same-1"}, new String[] { "same-1" }, true },
            { new String[] {"same-1"}, new String[] { "diff-1", "same-1" }, true },
            { new String[] {"same-2", "diff-2"}, new String[] { "same-2" }, true },
            { new String[] {"same-2"}, new String[] { "diff-2", "same-2" }, true },
            { new String[] {"diff-n-1", "diff-n-2"}, new String[] { "diff-n-3" }, false },
            { new String[] {"diff-n-11"}, new String[] { "diff-12", "diff-13" }, false }
        };
    }
}
