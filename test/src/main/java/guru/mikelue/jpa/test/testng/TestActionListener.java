package guru.mikelue.jpa.test.testng;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * The {@link ITestListener} for executing {@link ActionListener#executeBeforeAction} and
 * <p>{@link ActionListener#executeAfterAction} in {@link #onStart} and {@link #onFinish}.</p>
 *
 * <p>This listener should be inherited by sub-class.</p>
 * The sub-class should implement {@link #buildActionListener} to provide {@link ActionListener} used in
 * <p>this listener.</p>
 *
 * <p>The {@link ITestListener} has defined test and method scope of listener; hence this class just implements the test scope of listener.</p>
 *
 * @see MethodActionListener
 */
public abstract class TestActionListener extends TestNGActionListenerBase<ITestContext> implements ITestListener {
    private static final ThreadLocal<ITestContext> testContext = new ThreadLocal<ITestContext>() {};

    public TestActionListener() {}

    /**
     * <p>This method would execute {@link ActionListener#executeBeforeAction} comes from {@link #buildActionListener()}.</p>
     *
     * @param context The containing test context
     */
    @Override
    public void onStart(ITestContext context)
    {
        getLogger().debug("Execute before action in Test: [{}]", context.getName());
        testContext.set(context);
        buildActionListener().executeBeforeAction();
    }
    /**
     * <p>This method would execute {@link ActionListener#executeAfterAction} comes from {@link #buildActionListener()}.</p>
     *
     * @param context The containing test context
     */
    @Override
    public void onFinish(ITestContext context)
    {
        getLogger().debug("Execute after action in Test: [{}]", context.getName());
        testContext.set(context);
        buildActionListener().executeAfterAction();
    }
    /**
     * <p>Implements the retrieving for {@link ITestContext} object in TestNG context.</p>
     *
     * @return Current {@link ITestContext} object in thread.
     */
    @Override
    protected ITestContext getContextObject()
    {
        return testContext.get();
    }

    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void onTestFailure(ITestResult result) {}
    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void onTestSkipped(ITestResult result) {}
    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void onTestStart(ITestResult result) {}
    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void onTestSuccess(ITestResult result) {}
}
