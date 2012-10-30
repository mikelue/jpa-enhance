package org.no_ip.mikelue.jpa.query;

import mockit.NonStrictExpectations;
import mockit.Mocked;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class QueryUtilTest {
	public QueryUtilTest() {}

	@Mocked
	private Query query;
	@Mocked
	private TypedQuery<Integer> typedQuery;

	/**
	 * <ol>
	 * 		<li>Test single result</li>
	 * 		<li>Test nothing returned</li>
	 * </ol>
	 */
	@Test
	public void getSingleResult()
	{
        final Integer testValue = 2456;

		new NonStrictExpectations()
		{{
			query.getResultList();
			result = Arrays.asList(testValue);
			result = Collections.EMPTY_LIST;
		}};

        Object r = QueryUtil.getSingleResult(query);
		Assert.assertNotNull(r);
		Assert.assertEquals(r, testValue);
		Assert.assertNull(QueryUtil.getSingleResult(query));
	}
	/**
	 * <ol>
	 * 		<li>Test single result</li>
	 * 		<li>Test nothing returned</li>
	 * </ol>
	 */
	@Test
	public void getTypedSingleResult()
	{
        final Integer testValue = 456;

		new NonStrictExpectations()
		{{
			typedQuery.getResultList();
			result = Arrays.asList(testValue);
			result = Collections.<Integer>emptyList();
		}};

        Integer r = QueryUtil.getSingleResult(typedQuery);
		Assert.assertNotNull(r);
		Assert.assertEquals(r, testValue);
		Assert.assertNull(QueryUtil.getSingleResult(typedQuery));
	}

	/**
	 * Test if {@link NonUniqueResultException} thrown correctly
	 */
	@Test(expectedExceptions=PersistenceException.class)
	public void getSingleResultWithException()
	{
		new NonStrictExpectations()
		{{
			query.getResultList();
			result = Arrays.asList(1, 2);
		}};

		QueryUtil.getSingleResult(query);
	}

	/**
	 * Test if {@link NonUniqueResultException} thrown correctly
	 */
	@Test(expectedExceptions=PersistenceException.class)
	public void getTypedSingleResultWithException()
	{
		new NonStrictExpectations()
		{{
			typedQuery.getResultList();
			result = Arrays.asList(1, 2);
		}};

		QueryUtil.getSingleResult(typedQuery);
	}

    /**
     * Tests the incremental query for single result
     */
    @Test
    public void getSingleResultByIncrementalQuery()
    {
        SingleResultQueryAction nullAction = new FakeSingleQueryAction();

        /**
         * Asserts the null result
         */
        Assert.assertNull(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, nullAction
            )
        );

        Assert.assertNull(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, nullAction, nullAction
            )
        );
        // :~)

        final String testResult_1 = "test value 1",
              testResult_2 = "test value 2",
              testResult_3 = "test value 3";

        /**
         * Assert the viable data from 1st query
         */
        Assert.assertEquals(
            QueryUtil.getSingleResultByIncrementalQuery(
                new FakeSingleQueryAction(testResult_1), nullAction, nullAction
            ),
            testResult_1
        );
        // :~)

        /**
         * Assert the viable data from 2ed query
         */
        Assert.assertEquals(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, new FakeSingleQueryAction(testResult_2), nullAction
            ),
            testResult_2
        );
        // :~)

        /**
         * Assert the viable data from 4th query
         */
        Assert.assertEquals(
            QueryUtil.getSingleResultByIncrementalQuery(
                nullAction, nullAction, nullAction, new FakeSingleQueryAction(testResult_3)
            ),
            testResult_3
        );
        // :~)
    }

    /**
     * Tests the incremental query for list result
     */
    @Test
    public void getListResultByIncrementalQuery()
    {
        ListResultQueryAction emptyAction = new FakeListQueryAction();

        /**
         * Asserts the empty result
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, emptyAction
            ).size(),
            0
        );

        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, emptyAction
            ).size(),
            0
        );
        // :~)
        //
        final int testResult_1 = 3,
              testResult_2 = 5,
              testResult_3 = 7;

        /**
         * Assert the viable data from 1st query
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                new FakeListQueryAction(testResult_1), emptyAction, emptyAction
            ).size(),
            testResult_1
        );
        // :~)

        /**
         * Assert the viable data from 2ed query
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, new FakeListQueryAction(testResult_2), emptyAction
            ).size(),
            testResult_2
        );
        // :~)

        /**
         * Assert the viable data from 4th query
         */
        Assert.assertEquals(
            QueryUtil.getListResultByIncrementalQuery(
                emptyAction, emptyAction, emptyAction, new FakeListQueryAction(testResult_3)
            ).size(),
            testResult_3
        );
        // :~)
    }
}

class FakeSingleQueryAction implements SingleResultQueryAction {
    private Object expectedResult;

    FakeSingleQueryAction()
    {
        this(null);
    }
    FakeSingleQueryAction(Object newExpectedResult)
    {
        expectedResult = newExpectedResult;
    }

    @Override
    public Object getSingleResult()
    {
        return expectedResult;
    }
}

class FakeListQueryAction implements ListResultQueryAction {
    private List<Object> resultList;

    FakeListQueryAction()
    {
        this(0);
    }
    FakeListQueryAction(int elements)
    {
        resultList = new ArrayList(elements);
        for (int i = 1; i <= elements; i++) {
            resultList.add(i);
        }
    }

    @Override
    public List<Object> getListResult()
    {
        return resultList;
    }
}
