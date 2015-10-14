package guru.mikelue.jpa.test.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * <p>The base class for listener in any type of TestNG listener.</p>
 *
 * This class also defines {@link #getContextObject()} to retrieve current context object for test framework.
 * For example, in TestNG, there are {@link ISuite}, {@link ITestContext}, or {@link ITestResult} are sent from TestNG.
 * Such object would be bound to {@link ThreadLocal thread} which is safe in multi-threaded testing.
 * <p><b>Sub-class is responsible for implementing {@link #getContextObject()} method.</b></p>
 *
 * @param <T> The type of context object
 */
public abstract class TestNGActionListenerBase<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected TestNGActionListenerBase() {}

    /**
     * <p>Get the logger.</p>
     *
     * @return the logger with the name of class of instance
     */
    public Logger getLogger()
    {
        return logger;
    }

    /**
     * <p>Gets the context object from thread-safe container.</p>
     *
     * @return The context object sent from TestNG
     */
    protected abstract T getContextObject();

    /**
     * Implemented by sub-class to build the {@link ActionListener}
     *
     * @return The initialized {@link ActionListener}
     */
    public abstract ActionListener buildActionListener();
}
