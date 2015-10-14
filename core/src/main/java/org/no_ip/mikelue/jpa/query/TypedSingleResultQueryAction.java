package org.no_ip.mikelue.jpa.query;

/**
 * Client implements this interface to execute incremental query processing.
 *
 * @see QueryUtil#getSingleResultByIncrementalQuery(TypedSingleResultQueryAction, TypedSingleResultQueryAction, TypedSingleResultQueryAction...)
 * @see SingleResultQueryAction
 */
public interface TypedSingleResultQueryAction<T> {
    /**
     * Gets the result or null if there is no data.
     *
     * @return null if there is no data
     */
    public T getSingleResult();
}
