package org.no_ip.mikelue.jpa.paging;

import javax.persistence.Query;

import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * 測試 {@link Query} 的分頁查詢功能
 */
public class QueryPagingFilterTest {
	public QueryPagingFilterTest() {}

	@Mocked
	private Query testQuery;

	/**
	 * 第一階段查詢測試
	 */
	@Test
	public void firstPhase()
	{
		/**
		 * 測試一般情況
		 */
		new Expectations()
		{{
			testQuery.setFirstResult(5);
			testQuery.setMaxResults(16);
			testQuery.getResultList();
		}};
		new QueryPagingFilter(testQuery).firstPhasePaging(
			new PagingResultBean(new PagingRequestBean(2, 5, 2))
		);
		// :~)

		/**
		 * 測試最後一頁
		 */
		new Expectations()
		{{
			testQuery.setFirstResult(0);
			testQuery.setMaxResults(Integer.MAX_VALUE);
			testQuery.getResultList();
		}};
		new QueryPagingFilter(testQuery).firstPhasePaging(
			new PagingResultBean(new PagingRequestBean(PagingRequestBean.LAST_PAGE, 5, 2))
		);
		// :~)
	}

	/**
	 * 第二階段查詢測試
	 */
	@Test
	public void lastPhase()
	{
		/**
		 * 測試一般情況
		 */
		new Expectations()
		{{
			testQuery.setFirstResult(0);
			testQuery.setMaxResults(16);
			testQuery.getResultList();
		}};
		new QueryPagingFilter(testQuery).lastPhasePaging(
			new PagingResultBean(new PagingRequestBean(2, 5, 2))
		);
		// :~)

		/**
		 * 測試最後一頁
		 */
		new Expectations()
		{{
			testQuery.setFirstResult(0);
			testQuery.setMaxResults(Integer.MAX_VALUE);
			testQuery.getResultList();
		}};
		new QueryPagingFilter(testQuery).lastPhasePaging(
			new PagingResultBean(new PagingRequestBean(PagingRequestBean.LAST_PAGE, 5, 2))
		);
		// :~)
	}
}
