package org.no_ip.mikelue.jpa.query;

import javax.persistence.Query;

/**
 * 設定 Query 相關參數用的工具類別，包含一個 Query 物件，將相關方法，
 * 依情況呼叫 Query 的 setParameter 方法。<p>
 *
 * 本類別為 Not Thread-Safe
 */
public class SetQueryParameterProxy {
	/**
	 * 所包含的 Query 物件
	 */
	private final Query enclosedQuery;
	private int position = 1;

	/**
	 * 提供被 Delegate 的 Query 物件
	 */
	public SetQueryParameterProxy(Query newEnclosedQuery)
	{
		enclosedQuery = newEnclosedQuery;
	}

	/**
	 * 若 value 不是 Null，才會設定 enclosedQuery 的 Parameter
	 */
	public SetQueryParameterProxy setParameterIfNotNull(String name, Object value)
	{
		if (value == null)
			return this;

		enclosedQuery.setParameter(name, value);
		return this;
	}
	/**
	 * 若 value 不是 Null，設定 enclosedQuery 的 Positional Parameter，
	 * 會自動增加 Parameter 序號
	 */
	public SetQueryParameterProxy setAutoPositionParameterIfNotNull(Object value)
	{
		if (value == null)
			return this;

		enclosedQuery.setParameter(position++, value);
		return this;
	}

	/**
	 * 取得所包含的 Query 物件
	 */
	public Query getEnclosedQuery()
	{
		return enclosedQuery;
	}
}
