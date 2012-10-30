package org.no_ip.mikelue.jpa.paging;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.Validate.notNull;

/**
 * 兩階段分頁的執行 IoC 類別，主要責任在依傳入的 {@link FetchPhase} 參數，決定是否要執行兩階段查詢。<p>
 *
 * 會依資料內容，設定查詢的資料筆數，是用來設定查出的結果，
 * 以調整真正分頁的情況({@link PagingResultBean#setResultSize(int)})，分為下列兩種策略:
 * <ol>
 * 		<li>從「開始筆數」到「結束筆數」</li>
 * 		<li>從「第一筆資料」到「開始筆數 - 1」</li>
 * </ol>
 * 在上述的兩種策略，用途在第一個查詢(第一個策略)沒有資料時，重新執行第二次查詢(第二個策略)，
 * 可用來確保分頁能查出符合的資料，但不會因為要求的頁碼過大而造成找不到資料的情況。<p>
 *
 * <pre>
 * 0 ========== Start ========== End
 * ^ 第二策略   ^ 第一策略
 * </pre>
 *
 * @see PagingResultBean
 * @see TwoPhasePagingFilter
 */
public class TwoPhasePagingRunner {
	private TwoPhasePagingRunner() {}

	/**
	 * 只執行第一階段分頁
	 *
	 * @param <T> 回傳的 List Generic Type
	 * @param filter 實作取得資料的 Closure
	 * @param resultBean 分頁的資料物件
	 *
	 * @return 可能為 Empty List 的結果
	 *
	 * @see #runTwoPhasePagingFilter(TwoPhasePagingFilter, PagingResultBean, FetchPhase)
	 */
	public static <T> List<T> runTwoPhasePagingFilter(TwoPhasePagingFilter<T> filter, PagingResultBean resultBean)
	{
		return runTwoPhasePagingFilter(filter, resultBean, FetchPhase.FirstFetch);
	}
	/**
	 * 依設定執行多階段分頁
	 *
	 * @param <T> 回傳的 List Generic Type
	 * @param filter 實作取得資料的 Closure
	 * @param resultBean 分頁的資料物件
	 * @param fetchPhase 要執行的階段
	 *
	 * @return 可能為 Empty List 的結果
	 *
	 * @see #runTwoPhasePagingFilter(TwoPhasePagingFilter, PagingResultBean)
	 */
	public static <T> List<T> runTwoPhasePagingFilter(TwoPhasePagingFilter<T> filter, PagingResultBean resultBean, FetchPhase fetchPhase)
	{
		checkValidPagingResultBean(resultBean);
		fetchPhase = processFetchPhase(fetchPhase);

		/**
		 * 要求最後一頁，直接查詢全部資料，有以下特性
		 * 1. 最後一筆往前取得每頁筆數為所在資料
		 * 2. 請求後 N 頁一律為 0
		 * 3. 不會進行第二次查詢
		 */
		PagingRequestBean pagingRequestBean = resultBean.getPagingRequestBean();
		if (pagingRequestBean.getPageNumberOfTarget() == PagingRequestBean.LAST_PAGE) {
			List<T> resultData = filter.lastPhasePaging(resultBean);
			resultBean.setResultSize(resultData.size(), FetchPhase.LastFetch);

			return FetchPhase.LastFetch.filterToPage(
				resultData, pagingRequestBean.getPageSize()
			);
		}
		// :~)

		/**
		 * 兩階段查詢
		 */
		// ==================================================
		/**
		 * 第一次查詢
		 */
		List<T> resultData = filter.firstPhasePaging(resultBean);
		resultBean.setResultSize(resultData.size());
		// :~)

		/**
		 * 判斷是否要執行第二次查詢，需滿足以下條件
		 * 1. 第一階段沒有任何資料
		 * 2. 設定為二階段查詢
		 * 3. 請求的分頁大於 1
		 */
		if (
			resultBean.getResultStatus() == ResultStatus.EmptyData &&
			fetchPhase == FetchPhase.LastFetch &&
			pagingRequestBean.hasPreviousPage()
		) {
			/**
			 * 第二階段查詢以「最後一頁」為查詢策略
			 */
			pagingRequestBean.setPageNumberOfTarget(PagingRequestBean.LAST_PAGE);
			resultBean.setPagingRequestBean(pagingRequestBean);
			// :~)

			return runTwoPhasePagingFilter(filter, resultBean); // 遞迴呼叫請求「最後一頁」
		}
		// :~)

		return FetchPhase.FirstFetch.filterToPage(
			resultData, resultBean.getPagingRequestBean().getPageSize()
		);
		// ================================================== :~)
	}

	private static FetchPhase processFetchPhase(FetchPhase fetchPhase)
	{
		fetchPhase = fetchPhase == null ?
			FetchPhase.FirstFetch :
			fetchPhase;

		switch (fetchPhase) {
			case FirstFetch:
			case LastFetch:
				break;
			default:
				throw new RuntimeException("Unknown fetch phase: " + fetchPhase);
		}

		return fetchPhase;
	}

	private static void checkValidPagingResultBean(PagingResultBean pagingResultBean)
	{
		notNull(pagingResultBean, "Paging result bean is null");
		notNull(pagingResultBean.getPagingRequestBean(), "Request paging data is null");
	}
}
