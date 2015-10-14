package guru.mikelue.jpa.paging;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * 分頁相關工具方法.
 *
 * <p>目前支援 {@link TwoPhasePagingRunner} 所提供的兩階段分頁策略</p>
 */
public class PagingUtil {
	private PagingUtil() {}

	/**
	 * 將 List 資料分頁成所設定的內容。
	 *
	 * @param <T> 回傳的 List Generic Type
	 * @param srcList 來源資料
	 * @param pagingResultBean 分頁設定
	 *
	 * @return 分頁的內容，若 srcList 為 null，會傳回 null。
	 * 若分頁所在的資料不存在，會傳回空白的 List
	 *
	 * @see #filterDataToPage(List, PagingResultBean, FetchPhase)
	 */
	public static <T> List<T> filterDataToPage(List<T> srcList, PagingResultBean pagingResultBean)
	{
		return TwoPhasePagingRunner.<T>runTwoPhasePagingFilter(
			new ListPagingFilter<T>(srcList), pagingResultBean
		);
	}
	/**
	 * 將 List 資料分頁成所設定的內容。
	 *
	 * @param <T> 回傳的 List Generic Type
	 * @param srcList 來源資料
	 * @param pagingResultBean 分頁設定
	 * @param fetchPhase 查詢的策略，若為 null，預設策略為 {@link FetchPhase#FirstFetch}
	 *
	 * @return 分頁的內容，若 srcList 為 null，會傳回 null。
	 * 若分頁所在的資料不存在，會傳回空白的 List
	 *
	 * @see #filterDataToPage(List, PagingResultBean)
	 */
	public static <T> List<T> filterDataToPage(List<T> srcList, PagingResultBean pagingResultBean, FetchPhase fetchPhase)
	{
		return TwoPhasePagingRunner.<T>runTwoPhasePagingFilter(
			new ListPagingFilter<T>(srcList), pagingResultBean, fetchPhase
		);
	}

	/**
	 * 執行查詢，並取得分頁設定的資料，只會執行一次查詢.
	 *
	 * @param query 要執行的查詢 JPA 物件，不得為 null
	 * @param pagingResultBean 分頁設定，不得為 null
	 *
	 * @return 符合的資料，若沒有符合的資料，或是分頁的資料不存在，會傳回 {@link List#size()} 為 0 的 {@link List}
	 *
	 * @see #queryAndFilterDataToPage(Query, PagingResultBean, FetchPhase)
	 * @see #queryAndFilterDataToPage(TypedQuery, PagingResultBean)
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> queryAndFilterDataToPage(Query query, PagingResultBean pagingResultBean)
	{
		return TwoPhasePagingRunner.runTwoPhasePagingFilter(
			new QueryPagingFilter(query), pagingResultBean
		);
	}
	/**
	 * 執行查詢，並取得分頁設定的資料.
	 *
	 * <p>參數 fetchPhase 若為 {@link FetchPhase#LastFetch}，若第一次查詢不到資料，會執行第二次查詢(從第一筆到原要求頁碼的前一筆)</p>
	 *
	 * @param query 要執行的查詢 JPA 物件，不得為 null
	 * @param pagingResultBean 分頁設定，不得為 null
	 * @param fetchPhase 查詢的策略，若為 null，預設策略為 {@link FetchPhase#FirstFetch}
	 *
	 * @return 符合的資料，若沒有任何資料，會傳回 {@link List#size()} 為 0 的 {@link List}
	 *
	 * @see #queryAndFilterDataToPage(Query, PagingResultBean)
	 * @see #queryAndFilterDataToPage(TypedQuery, PagingResultBean, FetchPhase)
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> queryAndFilterDataToPage(Query query, PagingResultBean pagingResultBean, FetchPhase fetchPhase)
	{
		return TwoPhasePagingRunner.runTwoPhasePagingFilter(
			new QueryPagingFilter(query), pagingResultBean, fetchPhase
		);
	}
	/**
	 * 執行查詢，並取得分頁設定的資料，只會執行一次查詢.
	 *
	 * @param <T> 回傳的 List Generic Type
	 * @param typedQuery 要執行的查詢 JPA 物件，不得為 null
	 * @param pagingResultBean 分頁設定，不得為 null
	 *
	 * @return 符合的資料，若沒有符合的資料，或是分頁的資料不存在，會傳回 {@link List#size()} 為 0 的 {@link List}
	 *
	 * @see #queryAndFilterDataToPage(TypedQuery, PagingResultBean, FetchPhase)
	 * @see #queryAndFilterDataToPage(Query, PagingResultBean)
	 */
	public static <T> List<T> queryAndFilterDataToPage(TypedQuery<T> typedQuery, PagingResultBean pagingResultBean)
	{
		return TwoPhasePagingRunner.runTwoPhasePagingFilter(
			new TypedQueryPagingFilter<T>(typedQuery), pagingResultBean
		);
	}
	/**
	 * 執行查詢，並取得分頁設定的資料.
	 *
	 * <p>參數 fetchPhase 若為 {@link FetchPhase#LastFetch}，若第一次查詢不到資料，會執行第二次查詢(從第一筆到原要求頁碼的前一筆)</p>
	 *
	 * @param <T> 回傳的 List Generic Type
	 * @param typedQuery 要執行的查詢 JPA 物件，不得為 null
	 * @param pagingResultBean 分頁設定，不得為 null
	 * @param fetchPhase 查詢的策略，若為 null，預設策略為 {@link FetchPhase#FirstFetch}
	 *
	 * @return 符合的資料，若沒有任何資料，會傳回 {@link List#size()} 為 0 的 {@link List}
	 *
	 * @see #queryAndFilterDataToPage(TypedQuery, PagingResultBean)
	 * @see #queryAndFilterDataToPage(Query, PagingResultBean, FetchPhase)
	 */
	public static <T> List<T> queryAndFilterDataToPage(TypedQuery<T> typedQuery, PagingResultBean pagingResultBean, FetchPhase fetchPhase)
	{
		return TwoPhasePagingRunner.runTwoPhasePagingFilter(
			new TypedQueryPagingFilter<T>(typedQuery), pagingResultBean, fetchPhase
		);
	}
}

/**
 * {@link Query} 分頁查詢物件.
 */
class QueryPagingFilter implements TwoPhasePagingFilter<Object> {
	private Query query;

	QueryPagingFilter(Query newQuery)
	{
		if (newQuery == null) {
			throw new IllegalArgumentException("Query is null");
		}

		query = newQuery;
	}

	@Override
	public List<Object> firstPhasePaging(PagingResultBean resultBean)
	{
		return firstPhasePagingImpl(query, resultBean);
	}
	@Override
	public List<Object> lastPhasePaging(PagingResultBean resultBean)
	{
		return lastPhasePagingImpl(query, resultBean);
	}

	@SuppressWarnings("unchecked")
	static List<Object> firstPhasePagingImpl(Query query, PagingResultBean resultBean)
	{
		query.setFirstResult(resultBean.getFirstRecordNumber());
		query.setMaxResults(resultBean.getLastRecordNumber());
		return (List<Object>)query.getResultList();
	}
	@SuppressWarnings("unchecked")
	static List<Object> lastPhasePagingImpl(Query query, PagingResultBean resultBean)
	{
		query.setFirstResult(0);
		query.setMaxResults(resultBean.getLastRecordNumber());
		return (List<Object>)query.getResultList();
	}
}

/**
 * {@link TypedQuery} 分頁查詢物件
 */
class TypedQueryPagingFilter<T> implements TwoPhasePagingFilter<T> {
	private TypedQuery<T> typedQuery;

	TypedQueryPagingFilter(TypedQuery<T> newQuery)
	{
		if (newQuery == null) {
			throw new IllegalArgumentException("Query is null");
		}
		typedQuery = newQuery;
	}

	@Override @SuppressWarnings("unchecked")
	public List<T> firstPhasePaging(PagingResultBean resultBean)
	{
		return (List<T>)QueryPagingFilter.firstPhasePagingImpl(typedQuery, resultBean);
	}
	@Override @SuppressWarnings("unchecked")
	public List<T> lastPhasePaging(PagingResultBean resultBean)
	{
		return (List<T>)QueryPagingFilter.lastPhasePagingImpl(typedQuery, resultBean);
	}
}

/**
 * {@link List} 資料查詢物件，若分頁所在的資料不存在，會傳回空 List，
 * 否則傳回複製後(Non-Deep Clone)的 List
 */
class ListPagingFilter<T> implements TwoPhasePagingFilter<T> {
	private List<T> srcData;

	ListPagingFilter(List<T> newSrcData)
	{
		srcData = newSrcData;
	}

	@Override
	public List<T> firstPhasePaging(PagingResultBean resultBean)
	{
		/**
		 * 若要求的第一筆資料，超出來源資料的筆數，開始筆數為總筆數
		 */
		return new ArrayList<T>(srcData.subList(
			Math.min(srcData.size(), resultBean.getFirstRecordNumber()),
			srcData.size()
		));
		// :~)
	}
	@Override
	public List<T> lastPhasePaging(PagingResultBean resultBean)
	{
		/**
		 * 第二次為全部的資料查詢
		 */
		return new ArrayList<T>(srcData);
		// :~)
	}
}
