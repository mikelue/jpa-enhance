package guru.mikelue.jpa.query;

import java.util.List;

/**
 * Client implements this interface to execute incremental query processing.
 *
 * @see QueryUtil#getListResultByIncrementalQuery(TypedListResultQueryAction, TypedListResultQueryAction, TypedListResultQueryAction...)
 * @see ListResultQueryAction
 */
public interface TypedListResultQueryAction<T> {
    /**
     * Gets the list of result. <strong>This method should return empty list if there is no data.</strong>
     *
     * @return The list of data
     */
    public List<T> getListResult();
}
