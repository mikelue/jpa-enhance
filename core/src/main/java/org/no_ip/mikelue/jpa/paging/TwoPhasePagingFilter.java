package org.no_ip.mikelue.jpa.paging;

import java.util.List;

/**
 * 兩階段的分頁 Closure 介面，主要用途在定義兩階段查詢的機制，
 * 讓實作資料取得的邏輯與分頁演算策略分離.
 *
 * <p>實作的類別，若在 {@link #firstPhasePaging(PagingResultBean)} 傳回 Empty List，
 * 會視情況，呼叫 {@link #lastPhasePaging(PagingResultBean)}</p>
 *
 * <p>實作的方法不得傳回 null 值</p>
 *
 * @param <T> 回傳的 List Generic Type
 */
public interface TwoPhasePagingFilter<T> {
	/**
	 * 第一次分頁取得資料的 Closure
	 *
	 * @param resultBean 分頁資料物件
	 *
	 * @return 若無資料，傳回 {@link java.util.Collections#EMPTY_LIST}
	 *
	 * @see #lastPhasePaging(PagingResultBean)
	 */
	public List<T> firstPhasePaging(PagingResultBean resultBean);
	/**
	 * 第二次分頁取得資料的 Closure
	 *
	 * @param resultBean 分頁資料物件
	 *
	 * @return 若無資料，傳回 {@link java.util.Collections#EMPTY_LIST}
	 *
	 * @see #lastPhasePaging(PagingResultBean)
	 */
	public List<T> lastPhasePaging(PagingResultBean resultBean);
}
