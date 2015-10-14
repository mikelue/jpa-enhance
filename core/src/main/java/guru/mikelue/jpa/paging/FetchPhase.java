package guru.mikelue.jpa.paging;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 查詢取得結果時，用來處理取不到資料時，兩階段查詢的階段常數.
 *
 * @see PagingUtil
 */
public enum FetchPhase {
	/**
	 * 第一次查詢
	 */
	FirstFetch {
		@Override
		public <T> List<T> filterToPage(List<T> srcData, int pageSize)
		{
			validatePageSize(pageSize);
			return new ArrayList<T>(srcData.subList(
				0,
				Math.min(srcData.size(), pageSize)
			));
		}
	},
	/**
	 * 最後一次查詢(查詢第一筆到所要求的前一頁筆數)
	 */
	LastFetch {
		@Override
		public <T> List<T> filterToPage(List<T> srcData, int pageSize)
		{
			validatePageSize(pageSize);
			return new ArrayList<T>(srcData.subList(
				Math.max(0, srcData.size() - pageSize),
				srcData.size()
			));
		}
	};

	/**
	 * 依階段不同，取得所在的資料
	 *
	 * <p>若為 {@link FetchPhase#FirstFetch}，會傳回「第 0 筆至第 pageSize 的資料」</p>
	 * <p>若為 {@link FetchPhase#LastFetch}，會傳回「(總筆數 - pageSize) 至最後一筆的資料」</p>
	 *
	 * @param <T> 資料的型態
	 * @param srcData 來源資料
	 * @param pageSize 每頁筆數
	 *
	 * @return The result data
	 */
	public abstract <T> List<T> filterToPage(List<T> srcData, int pageSize);

	private static void validatePageSize(int srcPageSize)
	{
		isTrue(srcPageSize > 0, "Page size need >= 0", srcPageSize);
	}
}
