package org.no_ip.mikelue.jpa.query;

import javax.persistence.TypedQuery;

/**
 * 支援 TypedQuery 的參數用工具類別, 包含一個 TypedQuery 物件<p>
 *
 * 本類別為 Not Thread-Safe
 */
public class SetTypedQueryParameterProxy<T> extends SetQueryParameterProxy {
	/**
	 * 所包含的 TypedQuery 物件
	 */
	private final TypedQuery<T> enclosedTypedQuery;

	/**
	 * 提供被 Delegate 的 TypedQuery 物件
	 */
	public SetTypedQueryParameterProxy(TypedQuery<T> newEnclosedTypedQuery)
	{
		super(newEnclosedTypedQuery);
		enclosedTypedQuery = newEnclosedTypedQuery;
	}

	/**
	 * 取得所包含的 TypedQuery 物件
	 */
	public TypedQuery<T> getEnclosedTypedQuery()
	{
		return enclosedTypedQuery;
	}

	/**
	 * 若 value 不是 Null，才會設定 enclosedQuery 的 Parameter
	 */
	@Override
	public SetTypedQueryParameterProxy<T> setParameterIfNotNull(String name, Object value)
	{
		super.setParameterIfNotNull(name, value);
		return this;
	}
	/**
	 * 若 value 不是 Null，設定 enclosedQuery 的 Positional Parameter，
	 * 會自動增加 Parameter 序號
	 */
	@Override
	public SetTypedQueryParameterProxy<T> setAutoPositionParameterIfNotNull(Object value)
	{
		super.setAutoPositionParameterIfNotNull(value);
		return this;
	}
}
