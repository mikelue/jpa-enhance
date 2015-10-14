package guru.mikelue.jpa.test.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * The {@link ISuiteListener} for executing {@link ActionListener#executeBeforeAction} and
 * <p>{@link ActionListener#executeAfterAction} in {@link #onStart} and {@link #onFinish}.</p>
 *
 * <p>This listener should be inherited by sub-class.</p>
 * The sub-class should implement {@link #buildActionListener} to provide {@link ActionListener} used in
 * <p>this listener.</p>
 *
 * @see ActionListener
 */
public abstract class SuiteActionListener extends TestNGActionListenerBase<ISuite> implements ISuiteListener {
    private static final ThreadLocal<ISuite> suiteContext = new ThreadLocal<ISuite>() {};

    protected SuiteActionListener() {}

    /**
     * <p>This method would execute {@link ActionListener#executeBeforeAction} comes from {@link #buildActionListener()}.</p>
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
     * <p>This method would execute {@link ActionListener#executeAfterAction} comes from {@link #buildActionListener()}.</p>
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
     * <p>Implements the retrieving for {@link ISuite} object in TestNG context.</p>
     *
     * @return Current {@link ISuite} object in thread.
     */
    @Override
    protected ISuite getContextObject()
    {
        return suiteContext.get();
    }
}
