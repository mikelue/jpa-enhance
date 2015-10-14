package guru.mikelue.jpa.query;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SortingTypeTest {
	public SortingTypeTest() {}

	/**
	 * 測試取得 Sorting Type 的方法
	 */
	@Test
	public void getSortingType()
	{
		Assert.assertEquals(SortingType.ASC, SortingType.getSortingType("asc"));
		Assert.assertEquals(SortingType.DESC, SortingType.getSortingType("desc"));

		Assert.assertEquals(SortingType.ASC, SortingType.getSortingType(null, SortingType.ASC));
		Assert.assertEquals(SortingType.DESC, SortingType.getSortingType("    ", SortingType.DESC));
	}
}
