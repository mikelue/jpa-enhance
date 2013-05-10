package org.no_ip.mikelue.jpa.paging;

import static org.apache.commons.lang3.Validate.isTrue;

import static org.no_ip.mikelue.jpa.commons.number.MathUtil.add;
import static org.no_ip.mikelue.jpa.commons.number.MathUtil.multiply;

/**
 * 分頁的結果物件，本類別有兩組方法，一組給 JPA 用來設定 {@link javax.persistence.Query#setFirstResult(int)} 與
 * {@link javax.persistence.Query#setMaxResults(int)} 用。<p>
 *
 * 資料取出後，可能有下列情況:
 * <ul>
 * 		<li>所要求的頁數不存在(沒有資料)</li>
 * 		<li>本頁後顯示頁數不足或已到最後一頁</li>
 * </ul>
 * 在上述情況發生時，另一組方法，會用來設定/取得正確分頁結果，包含目前頁數(可能為 0，代表沒有資料)，
 * 本頁後顯示頁數(可能為 0，代表已到最後一頁)<p>
 *
 * {@link #setResultSize(int, FetchPhase)} 是用在查詢不到資料時，再度重新查詢前幾頁資料的處理<p>
 *
 * 若第一次查不到資料，本物件提供 {@link #setResultSize(int, FetchPhase)}，目地讓 client code 重新從頭查詢資料，
 * 並重設新的資料筆數<p>
 *
 * @see TwoPhasePagingRunner
 */
public class PagingResultBean {
	/**
	 * 尚未初始化的結果分頁資料
	 */
	public static final int UNKNOWN_RESULT = Integer.MIN_VALUE;

	private int resultPageNumberOfTarget = UNKNOWN_RESULT;
	private int resultTotalPageNumber = UNKNOWN_RESULT;
	private ResultStatus resultStatus = ResultStatus.NotReady;

	private PagingRequestBean pagingRequestBean = null;

	/**
	 * 所有資料為 null 的建構子
	 *
	 * @see #PagingResultBean(PagingRequestBean)
	 */
	public PagingResultBean()
	{
		this(null);
	}

	/**
	 * 以要求的分頁資料，建構本物件
	 *
	 * @param newPagingRequestBean 要求的分頁資料
	 */
	public PagingResultBean(PagingRequestBean newPagingRequestBean)
	{
		setPagingRequestBean(newPagingRequestBean);
	}

	/**
	 * 設定分頁的請求內容，所有之前查詢的筆數設定，會被還原為「未初始化」的狀態({@link #UNKNOWN_RESULT})。
	 *
	 * @param newPagingRequestBean 分頁的請求內容
	 *
	 * @see #getPagingRequestBean()
	 */
	public void setPagingRequestBean(PagingRequestBean newPagingRequestBean)
	{
		if (newPagingRequestBean != null) {
			PagingRequestBean.checkValidPagingBean(newPagingRequestBean);
			newPagingRequestBean = new PagingRequestBean(newPagingRequestBean);
		}

		pagingRequestBean = newPagingRequestBean;

		/**
		 * 重設所有的查詢結果
		 */
		resultPageNumberOfTarget = UNKNOWN_RESULT;
		resultTotalPageNumber = UNKNOWN_RESULT;
		resultStatus = ResultStatus.NotReady;
		// :~)
	}

	/**
	 * 取得分頁的請求內容，可能為 null。
	 *
	 * @return 分頁的請求內容，可能為 null
	 *
	 * @see #getPagingRequestBean()
	 */
	public PagingRequestBean getPagingRequestBean()
	{
		return new PagingRequestBean(pagingRequestBean);
	}

	/**
	 * {@link javax.persistence.Query#setFirstResult(int)} 的資料來源，用來取得第一筆開始的資料筆數。<p>
	 *
	 * 開始的筆數為「要求頁碼 - 1」*「每頁筆數」<p>
	 *
	 * 若請求分頁為 {@link PagingRequestBean#LAST_PAGE}(最後一頁)，會傳回 0
	 *
	 * @return 大於等於 0 的開始資料筆數，最大值為 {@link Integer#MAX_VALUE}
	 *
	 * @see #getLastRecordNumber()
	 */
	public int getFirstRecordNumber()
	{
		if (pagingRequestBean == null) {
			throw new IllegalStateException("Paging request is null");
		}

		/**
		 * 要求最後一頁
		 */
		if (pagingRequestBean.getPageNumberOfTarget() == PagingRequestBean.LAST_PAGE) {
			return 0;
		}
		// :~)

		/**
		 * 超出 Integer 最大值，值接傳回最大值
		 */
		return multiply(
			pagingRequestBean.getPageNumberOfTarget() - 1,
			pagingRequestBean.getPageSize()
		);
		// :~)
	}
	/**
	 * {@link javax.persistence.Query#setMaxResults(int)} 的資料來源，用來取得最後一筆查詢的筆數(最大回傳筆數)。<p>
	 *
	 * 在一般的情況下，會傳回「本頁後顯示頁數 + 1」*「每頁筆數」+ 1<p>
	 *
	 * 若請求分頁為 {@link PagingRequestBean#LAST_PAGE}(最後一頁)，會傳回 {@link Integer#MAX_VALUE}
	 *
	 * @return 大於 0 最後一筆查詢的筆數，最大值為 {@link Integer#MAX_VALUE}
	 *
	 * @see #getFirstRecordNumber()
	 */
	public int getLastRecordNumber()
	{
		if (pagingRequestBean == null) {
			throw new IllegalStateException("Paging request is null");
		}

		/**
		 * 要求最後一頁
		 */
		if (pagingRequestBean.getPageNumberOfTarget() == PagingRequestBean.LAST_PAGE) {
			return Integer.MAX_VALUE;
		}
		// :~)

		/**
		 * 「本頁後顯示頁數 + 1」*「每頁筆數」+ 1
		 */
		return
			add(
				multiply(
					add(pagingRequestBean.getPageNumberAfterTarget(), 1), // 本頁後顯示頁數 + 1
					pagingRequestBean.getPageSize() // 每頁筆數
				), 1 // 最後結果加 1(用來判斷後面是否還有更多頁)
			);
		// :~)
	}

	/**
	 * 設定第一次查詢結果(第一種策略)的分頁資料，依結果筆數，會產生不同的結果分頁資料。<p>
	 *
	 * 本方法會呼叫 {@link #setResultSize(int, FetchPhase)}，差別在使用 {@link FetchPhase#FirstFetch} 作為結果取得的演算方式
	 *
	 * @param resultSize 第一次查詢後，要設定結果筆數，不得小於 0
	 *
	 * @see #setResultSize(int, FetchPhase)
	 */
	public void setResultSize(int resultSize)
	{
		setResultSize(resultSize, FetchPhase.FirstFetch);
	}

	/**
	 * 設定結果的分頁資料，依指定的 {@link FetchPhase}，會產生不同的結果分頁資料。<p>
	 *
	 * 若 resultMode 為 {@link FetchPhase#FirstFetch}(第一種策略)，代表從要求的位置開始的查詢<p>
	 * 若 resultMode 為 {@link FetchPhase#LastFetch}(第二種策略)，代表從第 0 資料到要求的位置的查詢<p>
	 *
	 * 若請求分頁為 {@link PagingRequestBean#LAST_PAGE}(最後一頁)，會以 {@link FetchPhase#LastFetch} 為策略
	 *
	 * @param resultSize 結果的資料筆數，不得小於 0
	 * @param resultMode 目前查詢的階段(第一次還是最後一次查詢)
	 *
	 * @see #setResultSize(int)
	 */
	public void setResultSize(int resultSize, FetchPhase resultMode)
	{
		isTrue(
			resultSize >= 0,
			"Result size must be >= 0, current: ", String.valueOf(resultSize)
		);

		if (pagingRequestBean == null) {
			throw new IllegalStateException("Paging request is null");
		}

		/**
		 * 要求最後一頁
		 */
		if (pagingRequestBean.getPageNumberOfTarget() == PagingRequestBean.LAST_PAGE) {
			setResultPagingDataOfLastFetch(resultSize);
			return;
		}
		// :~)

		switch (resultMode) {
			case FirstFetch:
				setResultPagingDataOfFirstFetch(resultSize);
				break;
			case LastFetch:
				setResultPagingDataOfLastFetch(resultSize);
				break;
			default:
				throw new IllegalArgumentException("Unknown result mode: " + resultMode.toString());
		}
	}

	/**
	 * 依查詢結果取得「目前結果頁碼」。<p>
	 *
	 * 若尚未呼叫 {@link #setResultSize(int)}，會傳回 {@link #UNKNOWN_RESULT}<p>
	 *
	 * 本回傳值最小為 0，代表找不到任何資料，{@link #getResultStatus()} 會取得 {@link ResultStatus#EmptyData}
	 *
	 * @return 最小為 0 的值
	 *
	 * @see #getResultPageNumberOfTarget()
	 */
	public int getResultPageNumberOfTarget()
	{
		return resultPageNumberOfTarget;
	}
	/**
	 * 依查詢結果取得「全部資料頁數」
	 *
	 * @return 最小為 0 的資料頁數
	 */
	public int getResultTotalPageNumber()
	{
		return resultTotalPageNumber;
	}
	/**
	 * 依查詢結果取得查詢的狀態。<p>
	 *
	 * 若尚未查詢，會傳回 {@link ResultStatus#NotReady}
	 *
	 * @return 查詢結果的狀態
	 *
	 * @see ResultStatus
	 */
	public ResultStatus getResultStatus()
	{
		return resultStatus;
	}

	private void setResultPageNumberOfTarget(int newResultPageNumberOfTarget)
	{
		isTrue(
			newResultPageNumberOfTarget >= 0,
			"Target page number of result must be >= 0, current: ", String.valueOf(newResultPageNumberOfTarget)
		);

		resultPageNumberOfTarget = newResultPageNumberOfTarget;
	}
	private void setResultTotalPageNumber(int newResultTotalPageNumber)
	{
		isTrue(
			newResultTotalPageNumber >= 0,
			"Page number after target must be >= 0, current: ", String.valueOf(newResultTotalPageNumber)
		);

		resultTotalPageNumber = newResultTotalPageNumber;
	}

	/**
	 * 第一次查詢結果的設定，代表從第 Start 查到 End 的結果。
	 *
	 * 0 ---------- Start ---------- End
	 */
	private void setResultPagingDataOfFirstFetch(int resultSize)
	{
		/**
		 * 沒有任何資料(筆數為 0)
		 */
		if (resultSize == 0) {
			setupEmptyData();
			return;
		}
		// :~)

		/**
		 * 資料筆數不滿或剛好一頁
		 */
		if (resultSize <= pagingRequestBean.getPageSize()) {
			resultStatus = ResultStatus.ReachLastPage;
			setResultPageNumberOfTarget(pagingRequestBean.getPageNumberOfTarget());
			setResultTotalPageNumber(0);
			return;
		}
		// :~)

		/**
		 * 資料超過一頁(沒有超過本頁後顯示頁數)
		 */
		if (resultSize < getLastRecordNumber()) {
			resultStatus = ResultStatus.ReachLastPage;
			setResultPageNumberOfTarget(pagingRequestBean.getPageNumberOfTarget());
			setResultTotalPageNumber(
				(resultSize / pagingRequestBean.getPageSize() - 1) +
				((resultSize % pagingRequestBean.getPageSize() == 0) ? 0 : 1)
			);
			return;
		}
		// :~)

		/**
		 * 資料超過一頁(超過本頁後顯示頁數)
		 */
		resultStatus = ResultStatus.HasMorePage;
		setResultPageNumberOfTarget(pagingRequestBean.getPageNumberOfTarget());
		setResultTotalPageNumber(pagingRequestBean.getPageNumberAfterTarget());
		// :~)
	}
	/**
	 * 第一次查詢結果的設定，代表從 0 查到 Start 的結果。
	 *
	 * 0 ---------- Start ---------- End
	 */
	private void setResultPagingDataOfLastFetch(int resultSize)
	{
		/**
		 * 沒有任何資料(筆數為 0)
		 */
		if (resultSize == 0) {
			setupEmptyData();
			return;
		}
		// :~)

		/**
		 * 資料筆數 / 每頁筆數(代表在最近的那一頁)
		 */
		resultStatus = ResultStatus.ReachLastPage;
		setResultPageNumberOfTarget(
			(resultSize / pagingRequestBean.getPageSize()) +
			(resultSize % pagingRequestBean.getPageSize() > 0 ? 1 : 0)
		);
		setResultTotalPageNumber(0);
		// :~)
	}

	private void setupEmptyData()
	{
		resultStatus = ResultStatus.EmptyData;
		setResultPageNumberOfTarget(0);
		setResultTotalPageNumber(0);
	}
}
