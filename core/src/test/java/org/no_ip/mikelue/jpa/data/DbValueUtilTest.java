package org.no_ip.mikelue.jpa.data;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.EnumSet;

/**
 * This class tests the result of convertion from {@link DbValueUtil}
 */
public class DbValueUtilTest {
	public DbValueUtilTest() {}

    /**
     * Test the generation of map(integer to enum object).<p>
     */
	@Test
	public void convertToIntMap()
	{
		Map<Integer, FakeStatus> valueMap = DbValueUtil.convertToIntMap(FakeStatus.class);

		Assert.assertEquals(valueMap.size(), 3);
		Assert.assertEquals(FakeStatus.Wait, valueMap.get(1));
		Assert.assertNull(valueMap.get(3));
	}
    /**
     * Test the generation of map(string to enum object).<p>
     */
	@Test
	public void convertToStringMap()
	{
		Map<String, FakeStatus> valueMap = DbValueUtil.convertToStringMap(FakeStatus.class);

		Assert.assertEquals(valueMap.size(), 3);
		Assert.assertEquals(FakeStatus.Wait, valueMap.get("1"));
		Assert.assertNull(valueMap.get("3"));
	}
    /**
     * Test the generation of {@link EnumSet} from joined integeral value.<p>
     */
	@Test(dataProvider="JoinedValueAndEnumSet")
	public void joinedValueToEnumSet(
        int testJoinedValue, EnumSet<FakeStatus> expectedEnumSet
    ) {
        Assert.assertEquals(
            DbValueUtil.joinedValueToEnumSet(FakeStatus.class, testJoinedValue),
            expectedEnumSet
        );
	}
    /**
     * Test the generation of joined integeral value from {@link EnumSet}.<p>
     */
	@Test(dataProvider="JoinedValueAndEnumSet")
	public void enumSetToJoinedValue(
        Integer expectedJoinedValue, EnumSet<FakeStatus> testEnumSet
	) {
        Assert.assertEquals(
            DbValueUtil.enumSetToJoinedValue(Integer.class, testEnumSet),
            expectedJoinedValue
        );
	}

    @DataProvider(name="JoinedValueAndEnumSet")
    private Object[][] getJoinedValueToEnumSet()
    {
        return new Object[][] {
            { 1, EnumSet.of(FakeStatus.Wait) },
            { 3, EnumSet.of(FakeStatus.Wait, FakeStatus.Working) },
            { 7, EnumSet.allOf(FakeStatus.class) },
            { 0, EnumSet.noneOf(FakeStatus.class) }
        };
    }
}
