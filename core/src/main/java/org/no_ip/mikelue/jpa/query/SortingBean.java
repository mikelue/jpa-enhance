package org.no_ip.mikelue.jpa.query;

import org.apache.commons.lang3.StringUtils;

/**
 * 排序的資料物件，包含
 * <ol>
 * 		<li>排序的欄位名稱</li>
 * 		<li>排序的順序</li>
 * </ol>
 *
 * @see SortingType
 */
public class SortingBean {
	private SortingType sortingType;
	private String sortingKey;

	/**
	 * null 資料建構子
	 *
	 * @see #SortingBean(String, SortingType)
	 */
	public SortingBean()
	{
		this(null, null);
	}
	/**
	 * 基本資料建構子
	 *
	 * @param newSortingKey 排序的欄位名稱
	 * @param newSortingType 排序的順序
	 */
	public SortingBean(String newSortingKey, SortingType newSortingType)
	{
		setSortingKey(newSortingKey);
		setSortingType(newSortingType);
	}

	/**
	 * 複製用建構子
	 *
	 * @param anotherSortingBean 要複製的來源
	 */
	public SortingBean(SortingBean anotherSortingBean)
	{
		this(anotherSortingBean.getSortingKey(), anotherSortingBean.getSortingType());
	}

	/**
	 * 設定排序的欄位名稱
	 *
	 * @param newSortingKey 欄位名稱，會自動 trim
	 *
	 * @see #getSortingKey()
	 */
	public void setSortingKey(String newSortingKey) { this.sortingKey = StringUtils.trimToNull(newSortingKey); }
	/**
	 * 取得排序的欄位名稱
	 *
	 * @return 可能是 null
	 *
	 * @see #setSortingKey(String)
	 */
	public String getSortingKey() { return this.sortingKey; }

	/**
	 * 設定排序的順序
	 *
	 * @param newSortingType 排序順序
	 *
	 * @see #getSortingType()
	 */
	public void setSortingType(SortingType newSortingType) { this.sortingType = newSortingType; }
	/**
	 * 取得排序的順序
	 *
	 * @return 可能是 null
	 *
	 * @see #setSortingType(SortingType)
	 */
	public SortingType getSortingType() { return this.sortingType; }
}
