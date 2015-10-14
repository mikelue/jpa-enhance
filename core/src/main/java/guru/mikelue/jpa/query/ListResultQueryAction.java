package guru.mikelue.jpa.query;

import java.util.List;

/**
 * Client implements this interface to execute incremental query processing.
 *
 * @see QueryUtil#getListResultByIncrementalQuery(ListResultQueryAction, ListResultQueryAction, ListResultQueryAction...)
 * @see TypedListResultQueryAction
 */
public interface ListResultQueryAction extends TypedListResultQueryAction<Object> {
    /**
     * Gets the list of result. <b>This method should return empty list if there is no data.</b>
     *
     * @return The list of data
     */
    @Override
    public List<Object> getListResult();
}
