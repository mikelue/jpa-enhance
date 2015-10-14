package org.no_ip.mikelue.jpa.query;

import javax.persistence.TypedQuery;

/**
 * 支援 TypedQuery 的參數用工具類別, 包含一個 TypedQuery 物件.
 *
 * 本類別為 Not Thread-Safe.
 */
public class SetTypedQueryParameterProxy<T> extends SetQueryParameterProxy {
	/**
	 * 所包含的 TypedQuery 物件
	 */
	private final TypedQuery<T> enclosedTypedQuery;

	/**
	 * 提供被 Delegate 的 TypedQuery 物件
	 *
	 * @param newEnclosedTypedQuery The query to be enclosed
	 */
	public SetTypedQueryParameterProxy(TypedQuery<T> newEnclosedTypedQuery)
	{
		super(newEnclosedTypedQuery);
		enclosedTypedQuery = newEnclosedTypedQuery;
	}

	/**
	 * {@inheritDoc}
	 */
	public TypedQuery<T> getEnclosedTypedQuery()
	{
		return enclosedTypedQuery;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SetTypedQueryParameterProxy<T> setParameterIfNotNull(String name, Object value)
	{
		super.setParameterIfNotNull(name, value);
		return this;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SetTypedQueryParameterProxy<T> setAutoPositionParameterIfNotNull(Object value)
	{
		super.setAutoPositionParameterIfNotNull(value);
		return this;
	}
}
