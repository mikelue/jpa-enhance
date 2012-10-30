package org.no_ip.mikelue.jpa.query;

/**
 * Client implements this interface to execute incremental query processing.<p>
 *
 * @see QueryUtil#getSingleResultByIncrementalQuery(TypedSingleResultQueryAction, TypedSingleResultQueryAction, TypedSingleResultQueryAction...)
 * @see SingleResultQueryAction
 */
public interface TypedSingleResultQueryAction<T> {
    /**
     * Gets the result or null if there is no data.<p>
     *
     * @return null if there is no data
     */
    public T getSingleResult();
}
