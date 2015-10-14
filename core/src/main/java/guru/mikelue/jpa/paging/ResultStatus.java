package guru.mikelue.jpa.paging;

/**
 * 查詢結果的狀態常數
 */
public enum ResultStatus {
	/**
	 * 查詢尚未執行
	 */
	NotReady,
	/**
	 * 沒有任何資料(EOF)
	 */
	EmptyData,
	/**
	 * 已到最後一頁
	 */
	ReachLastPage,
	/**
	 * 有更多的分頁
	 */
	HasMorePage;
}
