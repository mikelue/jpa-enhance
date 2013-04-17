package org.no_ip.mikelue.jpa.paging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.ArrayUtils;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.Assert;

/**
 * 測試分頁查詢主要的工具方法
 */
public class PagingUtilTest {
	public PagingUtilTest() {}

	private static final List<Integer> testData;
	private static final List<Integer> testEmptyData = Collections.<Integer>emptyList();
	static {
		testData = new ArrayList<Integer>(100);
		for (int i = 1; i <= 100; i++) {
			testData.add(i);
		}
	}

	@Mocked
	private TypedQuery<Integer> testTypedQuery;

	/**
	 * 測試 List 資料分頁
	 */
	@Test(dataProvider="variousListPagingTestData")
	public void filterDataToPage(
		PagingRequestBean requestBean,
		List<Integer> resultData, List<Integer> expectedData,
		int expectedTargetPageNumber, int expectedTotalPageNumber
	) {
		PagingResultBean resultBean = new PagingResultBean(requestBean);

		Assert.assertEquals(
			PagingUtil.filterDataToPage(resultData, resultBean, FetchPhase.LastFetch), expectedData
		);
		Assert.assertEquals(
			resultBean.getResultPageNumberOfTarget(), expectedTargetPageNumber
		);
		Assert.assertEquals(
			resultBean.getResultTotalPageNumber(), expectedTotalPageNumber
		);
	}

	/**
	 * 測試 Query 資料分頁
	 */
	@Test(dataProvider="variousQueryPagingTestData")
	public void queryAndFilterDataToPage(
		PagingRequestBean requestBean,
		final List<Integer> firstQueryData, final List<Integer> lastQueryData,
		List<Integer> expectedData, int expectedTargetPageNumber, int expectedTotalPageNumber
	) {
		PagingResultBean resultBean = new PagingResultBean(requestBean);

		new NonStrictExpectations()
		{{
			testTypedQuery.getResultList();
			result = firstQueryData;
			result = lastQueryData;
		}};

		Assert.assertEquals(
			PagingUtil.queryAndFilterDataToPage(testTypedQuery, resultBean, FetchPhase.LastFetch), expectedData
		);
		Assert.assertEquals(
			resultBean.getResultPageNumberOfTarget(), expectedTargetPageNumber
		);
		Assert.assertEquals(
			resultBean.getResultTotalPageNumber(), expectedTotalPageNumber
		);
	}

	@DataProvider(name="variousQueryPagingTestData")
	private Object[][] variousQueryPagingTestData()
	{
		return new Object[][] {
			/**
			 * 一般情況
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3),
				testData.subList(40, 100), null,
				testData.subList(40, 50), 5, 3
			},
			// :~)
			/**
			 * 超出所需頁數的情況
			 */
			new Object[] {
				new PagingRequestBean(11, 10, 3),
				testEmptyData, testData.subList(0, 50),
				testData.subList(40, 50), 5, 0
			},
			// :~)
			/**
			 * 最後一頁測試
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 20, 3),
				testData, null,
				testData.subList(80, 100), 5, 0
			},
			// :~)
			/**
			 * 極端值測試
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, Integer.MAX_VALUE, 3),
				testEmptyData, testData,
				testData, 1, 0
			},
			// :~)
			/**
			 * 沒有任何資料
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, Integer.MAX_VALUE, 3),
				testEmptyData, testEmptyData,
				testEmptyData, 0, 0
			}
			// :~)
		};
	}

	@DataProvider(name="variousListPagingTestData")
	private Object[][] variousListPagingTestData()
	{
		return new Object[][] {
			/**
			 * 一般情況
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3),
				testData, testData.subList(40, 50),
				5, 3
			},
			// :~)
			/**
			 * 超出所需頁數的情況
			 */
			new Object[] {
				new PagingRequestBean(11, 10, 3),
				testData, testData.subList(90, 100),
				10, 0
			},
			// :~)
			/**
			 * 最後一頁測試
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 20, 3),
				testData, testData.subList(80, 100),
				5, 0
			},
			// :~)
			/**
			 * 極端值測試
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, Integer.MAX_VALUE, 3),
				testData, testData,
				1, 0
			},
			// :~)
			/**
			 * 沒有任何資料
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, Integer.MAX_VALUE, 3),
				testEmptyData, testEmptyData,
				0, 0
			}
			// :~)
		};
	}
}
