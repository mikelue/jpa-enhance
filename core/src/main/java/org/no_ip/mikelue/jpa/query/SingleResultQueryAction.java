package org.no_ip.mikelue.jpa.query;

/**
 * Client implements this interface to execute incremental query processing.<p>
 *
 * @see QueryUtil#getSingleResultByIncrementalQuery(SingleResultQueryAction, SingleResultQueryAction, SingleResultQueryAction...)
 * @see TypedSingleResultQueryAction
 */
public interface SingleResultQueryAction extends TypedSingleResultQueryAction<Object> {
    /**
     * Gets the result or null if there is no data.<p>
     *
     * @return null if there is no data
     */
    @Override
    public Object getSingleResult();
}
