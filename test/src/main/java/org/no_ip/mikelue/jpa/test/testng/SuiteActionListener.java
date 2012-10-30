package org.no_ip.mikelue.jpa.test.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * The {@link ISuiteListener} for executing {@link ActionListener#executeBeforeAction} and
 * {@link ActionListener#executeAfterAction} in {@link #onStart} and {@link #onFinish}.<p>
 *
 * This listener should be inherited by sub-class.<p>
 * The sub-class should implement {@link #buildActionListener} to provide {@link ActionListener} used in
 * this listener.<p>
 *
 * @see ActionListener
 */
public abstract class SuiteActionListener extends TestNGActionListenerBase<ISuite> implements ISuiteListener {
    private static final ThreadLocal<ISuite> suiteContext = new ThreadLocal<ISuite>() {};

    protected SuiteActionListener() {}

    /**
     * This method would execute {@link ActionListener#executeBeforeAction} comes from {@link #buildActionListener()}.<p>
     *
     * @param suite The containing test suite
     */
    @Override
    public void onStart(ISuite suite)
    {
        getLogger().debug("Execute before action in suite: [{}]", suite.getName());
        suiteContext.set(suite);
        buildActionListener().executeBeforeAction();
    }

    /**
     * This method would execute {@link ActionListener#executeAfterAction} comes from {@link #buildActionListener()}.<p>
     *
     * @param suite The containing test suite
     */
    @Override
    public void onFinish(ISuite suite)
    {
        getLogger().debug("Execute after action in suite: [{}]", suite.getName());
        suiteContext.set(suite);
        buildActionListener().executeAfterAction();
    }

    /**
     * Implements the retrieving for {@link ISuite} object in TestNG context.<p>
     *
     * @return Current {@link ISuite} object in thread.
     */
    @Override
    protected ISuite getContextObject()
    {
        return suiteContext.get();
    }
}
