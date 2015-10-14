package org.no_ip.mikelue.jpa.query;

import java.util.List;
import javax.persistence.NonUniqueResultException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Improvement of {@link Query}'s or {@link TypedQuery}'s methods.
 */
public class QueryUtil {
    private static Logger logger = LoggerFactory.getLogger(QueryUtil.class);

    private QueryUtil() {}

    /**
     * This method is almost as same as {@link Query#getSingleResult} besides
     * won't throw {@link NoResultException} if there is nothing returned.
     *
     * @param query query object
     *
     * @return return null if nothing in result list.
     *
     * @throws NonUniqueResultException if more than one rows are retrived
     *
     * @see #getSingleResult(TypedQuery)
     */
    public static Object getSingleResult(Query query)
    {
        List<?> resultList = query.getResultList();

        if (resultList.size() > 1) {
            throw new NonUniqueResultException("Query returns more than one rows");
        }

        if (resultList.size() == 0) {
            return null;
        }

        return resultList.get(0);
    }

    /**
     * This method is almost as same as {@link TypedQuery#getSingleResult} besides
     * won't throw {@link NoResultException} if there is nothing returned.
     *
	 * @param <T> The type of result for query
     * @param typedQuery Type-Safe query object
     *
     * @return return null if nothing in result list.
     *
     * @throws NonUniqueResultException if more than one rows are retrived
     *
     * @see #getSingleResult(Query)
     */
    public static <T> T getSingleResult(TypedQuery<T> typedQuery)
    {
        List<T> resultList = typedQuery.getResultList();

        if (resultList.size() > 1) {
            throw new NonUniqueResultException("Query returns more than one rows");
        }

        if (resultList.size() == 0) {
            return null;
        }

        return resultList.get(0);
    }

    /**
     * Gets data by multiple {@link SingleResultQueryAction}s with exhausting every provided query action
     * to retrive data until there is any of queries has viable data(not null).
     *
     * <p>The sequence of execution agrees with the parameters of method.</p>
     *
     * @param firstAction The 1st action to retrieve data
     * @param secondAction The 2ed action to retrieve data
     * @param actions The remaining actions to retrieve data
     *
     * @see SingleResultQueryAction
     * @see #getSingleResultByIncrementalQuery(TypedSingleResultQueryAction, TypedSingleResultQueryAction, TypedSingleResultQueryAction...)
     * @see #getSingleResult(Query)
	 *
	 * @return The result of query
     */
    public static Object getSingleResultByIncrementalQuery(
        SingleResultQueryAction firstAction, SingleResultQueryAction secondAction, SingleResultQueryAction... actions
    ) {
		return getSingleResultByIncrementalQueryImpl(firstAction, secondAction, actions);
    }

    /**
     * Gets data by multiple {@link SingleResultQueryAction}s with exhausting every provided query action
     * to retrive data until there is any of queries has viable data(not null).
     *
     * <p>The sequence of execution agrees with the parameters of method.</p>
     *
	 * @param <T> The type of result for query
     * @param firstAction The 1st action to retrieve data
     * @param secondAction The 2ed action to retrieve data
     * @param actions The remaining actions to retrieve data
     *
     * @see TypedSingleResultQueryAction
     * @see #getSingleResultByIncrementalQuery(SingleResultQueryAction, SingleResultQueryAction, SingleResultQueryAction...)
     * @see #getSingleResult(TypedQuery)
	 *
	 * @return The result of query
     */
    public static <T> T getSingleResultByIncrementalQuery(
        TypedSingleResultQueryAction<T> firstAction, TypedSingleResultQueryAction<T> secondAction, TypedSingleResultQueryAction<T>... actions
    ) {
		return getSingleResultByIncrementalQueryImpl(firstAction, secondAction, actions);
    }

    /**
     * Gets data by multiple {@link ListResultQueryAction}s with exhausting every provided query action
     * to retrive data until there is any of queries has viable data(the size of result list is greater than 1).
     *
     * <p>The sequence of execution agrees with the parameters of method.</p>
     *
     * @param firstAction The 1st action to retrieve data
     * @param secondAction The 2ed action to retrieve data
     * @param actions The remaining actions to retrieve data
     *
     * @see ListResultQueryAction
     * @see #getListResultByIncrementalQuery(TypedListResultQueryAction, TypedListResultQueryAction, TypedListResultQueryAction...)
	 *
	 * @return The result of query
     */
    public static List<Object> getListResultByIncrementalQuery(
        ListResultQueryAction firstAction, ListResultQueryAction secondAction, ListResultQueryAction... actions
    ) {
        return QueryUtil.<Object>getListResultByIncrementalQueryImpl(firstAction, secondAction, actions);
    }

    /**
     * Gets data by multiple {@link ListResultQueryAction}s with exhausting every provided query action
     * to retrive data until there is any of queries has viable data(the size of result list is greater than 1).
     *
     * <p>The sequence of execution agrees with the parameters of method.</p>
     *
	 * @param <T> The type of result for query
     * @param firstAction The 1st action to retrieve data
     * @param secondAction The 2ed action to retrieve data
     * @param actions The remaining actions to retrieve data
     *
     * @see TypedListResultQueryAction
     * @see #getListResultByIncrementalQuery(ListResultQueryAction, ListResultQueryAction, ListResultQueryAction...)
	 *
	 * @return The result of query
     */
    public static <T> List<T> getListResultByIncrementalQuery(
        TypedListResultQueryAction<T> firstAction, TypedListResultQueryAction<T> secondAction,
		TypedListResultQueryAction<T>... actions
    ) {
		return getListResultByIncrementalQueryImpl(firstAction, secondAction, actions);
    }

	/**
	 * This method as alias name of calling method to
	 * prevent recursive calling of related methods.
	 *
	 * @param <T> The type of result for query
	 * @param firstAction The first action
	 * @param secondAction The second action
	 * @param actions following actions
	 *
	 * @return The result of query
	 */
	private static <T> T getSingleResultByIncrementalQueryImpl(
        TypedSingleResultQueryAction<T> firstAction, TypedSingleResultQueryAction<T> secondAction,
		TypedSingleResultQueryAction<T>... actions
	) {
		T result = firstAction.getSingleResult();
		if (result != null) {
			return result;
		}
		logger.debug("1st query[{}] for single result is null", firstAction.getClass().getSimpleName());

		result = secondAction.getSingleResult();
		if (result != null) {
			return result;
		}
		logger.debug("2nd query[{}] for single result is null", secondAction.getClass().getSimpleName());

		/**
		 * Exhaust other actions until any of the queries retrieve viable data
		 */
		for (TypedSingleResultQueryAction<T> action: actions) {
			result = action.getSingleResult();
			if (result != null) {
				break;
			}
			logger.debug("Query[{}] for single result is null", action.getClass().getSimpleName());
		}
		// :~)

		return result;
	}

	/**
	 * This method as alias name of calling method to
	 * prevent recursive calling of related methods
	 *
	 * @param <T> The type of result for query
	 * @param firstAction The first action
	 * @param secondAction The second action
	 * @param actions following actions
	 *
	 * @return The result of query
	 */
	private static <T> List<T> getListResultByIncrementalQueryImpl(
        TypedListResultQueryAction<T> firstAction, TypedListResultQueryAction<T> secondAction,
		TypedListResultQueryAction<T>... actions
	) {
        List<T> result = firstAction.getListResult();
        if (result.size() > 0) {
            return result;
        }
        logger.debug("1st query[{}] for single result is null", firstAction.getClass().getSimpleName());

        result = secondAction.getListResult();
        if (result.size() > 0) {
            return result;
        }
        logger.debug("2nd query[{}] for single result is null", secondAction.getClass().getSimpleName());

        /**
         * Exhaust other actions until any of the queries retrieve viable data
         */
        for (TypedListResultQueryAction<T> action: actions) {
            result = action.getListResult();
            if (result.size() > 0) {
                break;
            }
            logger.debug("Query[{}] for single result is null", action.getClass().getSimpleName());
        }
        // :~)

        return result;
	}
}
