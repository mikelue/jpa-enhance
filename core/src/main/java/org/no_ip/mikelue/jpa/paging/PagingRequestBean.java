package org.no_ip.mikelue.jpa.paging;

import static org.apache.commons.lang.Validate.isTrue;

/**
 * 介面程式要求分頁所需提供的基本資料。<p>
 *
 * 本物件主要需要下列三項分頁需求資料
 * <ol>
 * 		<li>目前要求頁數(預設為 1)</li>
 * 		<li>每頁筆數</li>
 * 		<li>本頁後顯示頁數(預設為 0)</li>
 * </ol>
 *
 * 本物件需要提供「本頁後顯示頁數」，目的地提供資料庫查詢，
 * 可在 SQL 依此設定「最大回傳筆數」，讓資料庫有機會最佳化查詢。<p>
 *
 * 因為有提供空白建構子 {@link #PagingRequestBean()}，每一個取得數字的方法，
 * 都可能傳回 -1，相依的類別要負責檢查，可用 {@link #checkValidPagingBean(PagingRequestBean)}。<p>
 */
public class PagingRequestBean {
	/**
	 * 要求最後一頁的常數值
	 */
	public final static int LAST_PAGE = -1;

	/**
	 * 檢查 {@link PagingRequestBean} 所有的資料是否在合法值內，否則會產生 {@link IllegalArgumentException}
	 *
	 * @param checkBean 要檢查的 Bean
	 */
	public static void checkValidPagingBean(PagingRequestBean checkBean)
	{
		checkBean.setPageNumberOfTarget(checkBean.getPageNumberOfTarget());
		checkBean.setPageSize(checkBean.getPageSize());
		checkBean.setPageNumberAfterTarget(checkBean.getPageNumberAfterTarget());
	}

	private int pageNumberOfTarget = -1;
	private int pageSize = -1;
	private int pageNumberAfterTarget = -1;

	/**
	 * 空白建構子，所有參數值皆為 -1
	 */
	public PagingRequestBean() {}
	/**
	 * 提供所有可設定的資料，初始化本物件
	 *
	 * @param newPageNumberOfTarget 目前要求的頁數
	 * @param newPageSize 每頁的資料筆數
	 * @param newPageNumberAfterTarget 本頁後顯示頁數
	 */
	public PagingRequestBean(int newPageNumberOfTarget, int newPageSize, int newPageNumberAfterTarget)
	{
		setPageNumberOfTarget(newPageNumberOfTarget);
		setPageSize(newPageSize);
		setPageNumberAfterTarget(newPageNumberAfterTarget);
	}
	/**
	 * 提供每頁的資料筆數，初始化本物件，目前要求的頁數為 1，本頁後顯示頁數為 0
	 *
	 * @param newPageSize 每頁的資料筆數
	 */
	public PagingRequestBean(int newPageSize)
	{
		this(1, newPageSize, 0);
	}

	/**
	 * 複製用的建構子
	 *
	 * @param anotherBean 要複製的來源
	 */
	public PagingRequestBean(PagingRequestBean anotherBean)
	{
		this(
			anotherBean.getPageNumberOfTarget(),
			anotherBean.getPageSize(),
			anotherBean.getPageNumberAfterTarget()
		);
	}

	/**
	 * 設定目前要求頁數(預設為 1)，至少為 1
	 *
	 * @param newPageNumberOfTarget 大於等於 1 的值
	 *
	 * @see #getPageNumberOfTarget()
	 * @see #LAST_PAGE
	 */
	public void setPageNumberOfTarget(int newPageNumberOfTarget)
	{
		isTrue(
			newPageNumberOfTarget > 0 || newPageNumberOfTarget == LAST_PAGE,
			"Target page number must be >= 1, current: ", newPageNumberOfTarget
		);

		this.pageNumberOfTarget = newPageNumberOfTarget;
	}
	/**
	 * 取得目前要求頁數，可能為常數值 {@link #LAST_PAGE}
	 *
	 * @return 可能為 -1(未正確初始化)
	 *
	 * @see #setPageNumberOfTarget(int)
	 */
	public int getPageNumberOfTarget() { return this.pageNumberOfTarget; }

	/**
	 * 設定目前每頁筆數，至少為 1
	 *
	 * @param newPageSize 每頁筆數
	 *
	 * @see #getPageSize()
	 */
	public void setPageSize(int newPageSize)
	{
		isTrue(
			newPageSize > 0,
			"Page size must be >= 1, current: ", String.valueOf(newPageSize)
		);

		this.pageSize = newPageSize;
	}
	/**
	 * 取得目前每頁筆數，至少為 1
	 *
	 * @return 可能為 -1(未正確初始化)
	 *
	 * @see #setPageSize(int)
	 */
	public int getPageSize() { return this.pageSize; }

	/**
	 * 設定本頁後顯示頁數，至少為 0
	 *
	 * @param newPageNumberAfterTarget 大於等於 0 的值
	 *
	 * @see #getPageNumberAfterTarget()
	 */
	public void setPageNumberAfterTarget(int newPageNumberAfterTarget)
	{
		isTrue(
			newPageNumberAfterTarget >= 0,
			"Page number after target page must be >= 0, current: ", String.valueOf(newPageNumberAfterTarget)
		);

		this.pageNumberAfterTarget = newPageNumberAfterTarget;
	}
	/**
	 * 取得本頁後顯示頁數，至少為 0
	 *
	 * @return 可能為 -1(未正確初始化)
	 *
	 * @see #setPageNumberAfterTarget(int)
	 */
	public int getPageNumberAfterTarget() { return this.pageNumberAfterTarget; }

	/**
	 * 判斷請求分頁是否還有前一頁，用來判斷是否需要執行第二次查詢。<p>
	 *
	 * {@link #getPageNumberOfTarget()} 是否大於 1
	 *
	 * @return 若有，傳回 true
	 */
	public boolean hasPreviousPage()
	{
		checkValidPagingBean(this);
		return getPageNumberOfTarget() > 1;
	}

	@Override
	public String toString()
	{
		return String.format(
			"Target Page: %s, Page Size: %s, Page Number After Target: %s",
			getPageNumberOfTarget(), getPageSize(), getPageNumberAfterTarget()
		);
	}
}
