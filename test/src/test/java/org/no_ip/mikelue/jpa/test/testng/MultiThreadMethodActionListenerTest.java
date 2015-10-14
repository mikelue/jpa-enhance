package org.no_ip.mikelue.jpa.test.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Tests whether the context object is thread-safe or not.</p>
 *
 * This test would be run in two-thread environment; the {@link MultiThreadMethodActionListenerTest#MultiThreadMethodListener} would
 * setup a semaphore(by incrementing reached threads) which perform output data until all threads has reached.
 * If the context object is not a thread-local object, the output data would have single row only.
 */
@Test(groups="MultiThread")
@Listeners(MultiThreadMethodActionListenerTest.MultiThreadMethodListener.class)
public class MultiThreadMethodActionListenerTest {
    private Logger logger = LoggerFactory.getLogger(MultiThreadMethodActionListenerTest.class);

    /**
     * Uses non thread-safe to record the thread-safe output.
     */
    private static Set<String> calledMethodNames = new HashSet<String>(4);

    public MultiThreadMethodActionListenerTest() {}

    /**
     * Triggered methods which are executed by multi-threads
     */
    @Test(groups="thread-method-1")
    public void thread1()
    {
        logger.info("Thead 1 get called");
    }
    @Test(groups="thread-method-2")
    public void thread2()
    {
        logger.info("Thead 2 get called");
    }
    // :~)

    @Test(dependsOnGroups={"thread-method-1", "thread-method-2"})
    public void lastCheckThread()
    {
        Assert.assertEquals(
            calledMethodNames.size(), 2
        );
    }

    /**
     * <p>The listener used to process "MultiThread" events.</p>
     */
    public static class MultiThreadMethodListener extends MethodActionListener {
        private ActionListener actionListener = new ActionListener() {
            private AtomicInteger reachedThreads = new AtomicInteger(0);
            private Action beforeAction = new Action() {
                @Override
                public void executeAction()
                {
                    /**
                     * Waits for two threads has reached, there would be an error if
                     * the context object has been overwrittena by latter thread.
                     */
                    while (reachedThreads.incrementAndGet() < 2) {
                        getLogger().info("Waiting. Reached thread: {}", reachedThreads);

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // :~)

                    String methodName = getContextObject().getMethod().getMethodName();
                    getLogger().debug("[Before] Triggered at method: {}", methodName);
                    calledMethodNames.add(methodName);
                }
            };

            @Override
            public Action getBeforeAction()
            {
                return beforeAction;
            }
        };

        public MultiThreadMethodListener() {}

        @Override
        public ActionListener buildActionListener()
        {
            return actionListener;
        }

        @Override
        public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context)
        {
            if (isMethodTriggerListener(method)) {
                super.beforeInvocation(method, testResult, context);
            }
        }
        @Override
        public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context)
        {
            if (isMethodTriggerListener(method)) {
                super.afterInvocation(method, testResult, context);
            }
        }

        private boolean isMethodTriggerListener(IInvokedMethod method)
        {
            return
                !method.getTestMethod().getMethodName().equals("lastCheckThread") &&
                TestNGUtil.isMethodBelongGroups(method.getTestMethod(), "MultiThread");
        }
    }
}
