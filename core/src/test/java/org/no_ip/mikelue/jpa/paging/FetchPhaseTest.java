package org.no_ip.mikelue.jpa.paging;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.IntRange;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

/**
 * 依取得資料階段常數，測試取得頁面資料的內容是否正確
 */
public class FetchPhaseTest {
	public FetchPhaseTest() {}

	private static final int dataSize = 20;
	private static final List<Integer> testData;
	private static final List<Integer> testEmptyData = Collections.<Integer>emptyList();
	static {
		IntRange intRange = new IntRange(1, dataSize);
		testData = Arrays.asList(ArrayUtils.toObject(intRange.toArray()));
	}

	@Test(dataProvider="variousPageSize")
	public void firstPhase(int testPageSize)
	{
		Assert.assertEquals(
			FetchPhase.FirstFetch.filterToPage(testData, testPageSize),
			testData.subList(0, Math.min(testPageSize, dataSize))
		);
	}
	@Test(dataProvider="variousPageSize")
	public void lastPhase(int testPageSize)
	{
		Assert.assertEquals(
			FetchPhase.LastFetch.filterToPage(testData, testPageSize),
			testData.subList(
				Math.max(0, dataSize - testPageSize), dataSize
			)
		);
	}

	@DataProvider(name="variousPageSize", parallel=true)
	private Object[][] variousPageSize()
	{
		return new Object[][] {
			new Object[] { dataSize / 2 },
			new Object[] { dataSize },
			new Object[] { Integer.MAX_VALUE }
		};
	}
}
