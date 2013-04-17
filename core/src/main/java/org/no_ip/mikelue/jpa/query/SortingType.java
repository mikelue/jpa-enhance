package org.no_ip.mikelue.jpa.query;

import org.apache.commons.lang3.StringUtils;

/**
 * 排序的常數，可用來產生在 JPQL 中的 ORDER BY xxx.xxx DESC
 */
public enum SortingType {
	/**
	 * 遞增排序
	 */
	ASC,
	/**
	 * 遞減排序
	 */
	DESC;

	/**
	 * 依字串取得排序的常數，字串可不區分大小寫
	 *
	 * @param sortingType 要取出的字串
	 *
	 * @return 排序常數物件
	 *
	 * @see #getSortingType(String, SortingType)
	 */
	public static SortingType getSortingType(String sortingType)
	{
		sortingType = sortingType.toUpperCase();
		return valueOf(sortingType);
	}
	/**
	 * 依字串取得排序的常數，字串可不區分大小寫
	 *
	 * @param sortingType 要取出的字串
	 * @param defaultSortingType 若 sortingType 為 null，預設的排序方式
	 *
	 * @return 排序常數物件
	 *
	 * @see #getSortingType(String)
	 */
	public static SortingType getSortingType(String sortingType, SortingType defaultSortingType)
	{
		if (StringUtils.trimToNull(sortingType) == null) {
			return defaultSortingType;
		}

		return getSortingType(sortingType);
	}
}
