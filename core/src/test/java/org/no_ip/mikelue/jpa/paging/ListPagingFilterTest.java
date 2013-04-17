package org.no_ip.mikelue.jpa.paging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * 測試直接處理 List 的資料分頁功能
 */
public class ListPagingFilterTest {
	public ListPagingFilterTest() {}

	private static final List<Integer> testData;
	private static final List<Integer> testEmptyData = Collections.<Integer>emptyList();
	static {
		testData = new ArrayList<Integer>(100);
		for (int i = 1; i <= 100; i++) {
			testData.add(i);
		}
	}

	/**
	 * 第一階段 List 分頁測試
	 */
	@Test
	public void firstPhasePaging()
	{
		PagingResultBean resultBean = null;
		ListPagingFilter<Integer> filter = new ListPagingFilter<Integer>(testData);

		/**
		 * 一般情況
		 */
		resultBean = new PagingResultBean(
			new PagingRequestBean(5, 10, 3)
		);
		Assert.assertEquals(
			filter.firstPhasePaging(resultBean),
			testData.subList(40, testData.size())
		);
		// :~)

		/**
		 * 超出頁數的情況(沒有任何資料)
		 */
		resultBean = new PagingResultBean(
			new PagingRequestBean(11, 10, 3)
		);
		Assert.assertEquals(
			filter.firstPhasePaging(resultBean),
			testEmptyData
		);
		// :~)
	}
	/**
	 * 第一階段 List 分頁測試(空 List)
	 */
	@Test
	public void firstPhasePagingWithEmptyData()
	{
		PagingResultBean resultBean = null;
		ListPagingFilter<Integer> filter = new ListPagingFilter<Integer>(testEmptyData);

		/**
		 * 一般情況
		 */
		resultBean = new PagingResultBean(
			new PagingRequestBean(5, 10, 3)
		);
		Assert.assertEquals(
			filter.firstPhasePaging(resultBean),
			testEmptyData
		);
		// :~)
	}
	/**
	 * 第二階段 List 分頁測試
	 */
	@Test
	public void lastPhasePaging()
	{
		PagingResultBean resultBean = null;
		ListPagingFilter<Integer> filter = new ListPagingFilter<Integer>(testData);

		/**
		 * 一般情況
		 */
		resultBean = new PagingResultBean(
			new PagingRequestBean(11, 10, 3)
		);
		Assert.assertEquals(
			filter.lastPhasePaging(resultBean),
			testData
		);
		// :~)
	}
	/**
	 * 第二階段 List 分頁測試(空資料)
	 */
	@Test
	public void lastPhasePagingWithEmptyData()
	{
		PagingResultBean resultBean = null;
		ListPagingFilter<Integer> filter = new ListPagingFilter<Integer>(testEmptyData);

		/**
		 * 一般情況
		 */
		resultBean = new PagingResultBean(
			new PagingRequestBean(11, 10, 3)
		);
		Assert.assertEquals(
			filter.lastPhasePaging(resultBean),
			testEmptyData
		);
		// :~)
	}
}
