package org.no_ip.mikelue.jpa.paging;

import java.util.Collections;
import static java.util.Arrays.asList;

import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import mockit.MockUp;
import mockit.Verifications;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * 測試二階段查詢功能
 */
public class TwoPhasePagingRunnerTest {
	public TwoPhasePagingRunnerTest() {}

	@Mocked
	private TwoPhasePagingFilter<Integer> filter;

	/**
	 * 測試在二階段查詢中，第一階段找到資料的情況
	 */
	@Test
	public void hasDataInFirstPhaseInTwoPhase()
	{
		final int testSizeOfData = 10;

		new Expectations()
		{{
			filter.firstPhasePaging(withInstanceOf(PagingResultBean.class));
			result = asList(new Integer[testSizeOfData]);
		}};
		new MockUp<PagingResultBean>() {
			PagingResultBean it;

			@Mock(reentrant=true)
			void setResultSize(int size, FetchPhase fetchPhase)
			{
				it.setResultSize(size, fetchPhase);
				Assert.assertEquals(testSizeOfData, size);
			}
		};

		PagingResultBean resultBean = new PagingResultBean(
			new PagingRequestBean(1, 1, 0)
		);
		TwoPhasePagingRunner.runTwoPhasePagingFilter(
			filter, resultBean, FetchPhase.LastFetch
		);
	}
	/**
	 * 測試在第二階段找到資料的情況
	 */
	@Test
	public void hasDataInLastPhase()
	{
		final int testSizeOfData = 10;

		new Expectations()
		{{
			filter.firstPhasePaging(withInstanceOf(PagingResultBean.class));
			result = Collections.<Integer>emptyList();

			filter.lastPhasePaging(withInstanceOf(PagingResultBean.class));
			result = asList(new Integer[testSizeOfData]);
		}};
		new MockUp<PagingResultBean>() {
			PagingResultBean it;

			@Mock(reentrant=true)
			void setResultSize(int size, FetchPhase fetchPhase)
			{
				it.setResultSize(size, fetchPhase);

				switch (fetchPhase) {
					case FirstFetch:
						Assert.assertEquals(0, size);
						break;
					case LastFetch:
						Assert.assertEquals(testSizeOfData, size);
						break;
					default:
						throw new RuntimeException("Unsupported fetch phase: " + fetchPhase.toString());
				}
			}
		};

		PagingResultBean resultBean = new PagingResultBean(
			new PagingRequestBean(2, 1, 0)
		);
		TwoPhasePagingRunner.runTwoPhasePagingFilter(
			filter, resultBean, FetchPhase.LastFetch
		);
	}
	/**
	 * 測試二階段查詢後，沒有資料的情況
	 */
	@Test
	public void emptyDataInLastPhase()
	{
		new Expectations()
		{{
			filter.firstPhasePaging(withInstanceOf(PagingResultBean.class));
			result = Collections.<Integer>emptyList();

			filter.lastPhasePaging(withInstanceOf(PagingResultBean.class));
			result = Collections.<Integer>emptyList();
		}};
		new MockUp<PagingResultBean>() {
			PagingResultBean it;

			@Mock(reentrant=true)
			void setResultSize(int size, FetchPhase fetchPhase)
			{
				it.setResultSize(size, fetchPhase);

				switch (fetchPhase) {
					case FirstFetch:
						Assert.assertEquals(0, size);
						break;
					case LastFetch:
						Assert.assertEquals(0, size);
						break;
					default:
						throw new RuntimeException("Unsupported fetch phase: " + fetchPhase.toString());
				}
			}
		};

		PagingResultBean resultBean = new PagingResultBean(
			new PagingRequestBean(2, 1, 0)
		);
		TwoPhasePagingRunner.runTwoPhasePagingFilter(
			filter, resultBean, FetchPhase.LastFetch
		);
	}

	/**
	 * 測試二階段查詢，查詢第一頁，但沒有資料的情況(不會做第二次查詢)
	 */
	@Test
	public void emptyDataWithLastPhaseAndFirstPage()
	{
		new Expectations()
		{{
			filter.firstPhasePaging(withInstanceOf(PagingResultBean.class));
			result = Collections.<Integer>emptyList();
		}};
		new MockUp<PagingResultBean>() {
			PagingResultBean it;

			@Mock(reentrant=true)
			void setResultSize(int size, FetchPhase fetchPhase)
			{
				it.setResultSize(size, fetchPhase);

				Assert.assertEquals(0, size);
				Assert.assertEquals(fetchPhase, FetchPhase.FirstFetch);
			}
		};

		PagingResultBean resultBean = new PagingResultBean(
			new PagingRequestBean(1, 1, 0)
		);
		TwoPhasePagingRunner.runTwoPhasePagingFilter(
			filter, resultBean, FetchPhase.LastFetch
		);
	}

	/**
	 * 測試單一階段查詢，但沒有資料的情況
	 */
	@Test
	public void emptyDataWithFirstPhase()
	{
		new Expectations()
		{{
			filter.firstPhasePaging(withInstanceOf(PagingResultBean.class));
			result = Collections.<Integer>emptyList();
		}};
		new MockUp<PagingResultBean>() {
			PagingResultBean it;

			@Mock(reentrant=true)
			void setResultSize(int size, FetchPhase fetchPhase)
			{
				it.setResultSize(size, fetchPhase);

				Assert.assertEquals(0, size);
				Assert.assertEquals(fetchPhase, FetchPhase.FirstFetch);
			}
		};

		PagingResultBean resultBean = new PagingResultBean(
			new PagingRequestBean(2, 1, 0)
		);
		TwoPhasePagingRunner.runTwoPhasePagingFilter(
			filter, resultBean, FetchPhase.FirstFetch
		);
	}

	/**
	 * 測試取得最後一頁資料的情況
	 */
	@Test
	public void lastPage()
	{
		new Expectations()
		{{
			filter.lastPhasePaging(withInstanceOf(PagingResultBean.class));
			result = Collections.<Integer>emptyList();
		}};

		PagingResultBean resultBean = new PagingResultBean(
			new PagingRequestBean(PagingRequestBean.LAST_PAGE, 1, 0)
		);
		TwoPhasePagingRunner.runTwoPhasePagingFilter(
			filter, resultBean, FetchPhase.FirstFetch
		);
	}
}
