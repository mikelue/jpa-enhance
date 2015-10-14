package guru.mikelue.jpa.test.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * The {@link IInvokedMethodListener2} which is for executing {@link ActionListener#executeBeforeAction} and
 * <p>{@link ActionListener#executeAfterAction} in {@link #beforeInvocation} and {@link #afterInvocation}.</p>
 *
 * <p>This listener should be inherited by sub-class.</p>
 * The sub-class should implement {@link #buildActionListener} to provide {@link ActionListener} used in
 * <p>this listener.</p>
 *
 * <p>There is an additional {@link #getTestContext} method to retrieve {@link ITestContext} in current thread.</p>
 *
 * @see TestActionListener
 */
public abstract class MethodActionListener extends TestNGActionListenerBase<ITestResult> implements IInvokedMethodListener2 {
    private static final ThreadLocal<ITestResult> testResultOfCurrentThread = new ThreadLocal<ITestResult>() {};
    private static final ThreadLocal<ITestContext> testContextOfCurrentThread = new ThreadLocal<ITestContext>() {};

    public MethodActionListener() {}

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context)
    {
        if (!method.isTestMethod()) {
            return;
        }

        getLogger().debug("Execute before action in method: [{}]", method.getTestMethod().getMethodName());
        testContextOfCurrentThread.set(context);
        testResultOfCurrentThread.set(testResult);
        buildActionListener().executeBeforeAction();
    }
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context)
    {
        if (!method.isTestMethod()) {
            return;
        }

        getLogger().debug("Execute after action in method: [{}]", method.getTestMethod().getMethodName());
        testContextOfCurrentThread.set(context);
        testResultOfCurrentThread.set(testResult);
        buildActionListener().executeAfterAction();
    }

    /**
     * <p>Implements the retrieving for {@link ITestContext} object in TestNG context.</p>
     *
     * @return Current {@link ITestResult} object in thread.
     *
     * @see #getTestContext
     */
    @Override
    protected ITestResult getContextObject()
    {
        return testResultOfCurrentThread.get();
    }
    /**
     * <p>Implements the retrieving for {@link ITestContext} object in TestNG context.</p>
     *
     * @return Current {@link ITestResult} object in thread.
     *
     * @see #getContextObject
     */
    protected ITestContext getTestContext()
    {
        return testContextOfCurrentThread.get();
    }

    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResultOfCurrentThread) {}
    /**
     * <p>Nothing implemented.</p>
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResultOfCurrentThread) {}
}
