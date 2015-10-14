package org.no_ip.mikelue.jpa.test.testng;

import mockit.Cascading;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class MethodActionListenerTest {
    @Mocked
    private ITestResult mockTestResult;
    @Mocked
    private ITestContext mockTestContext;
    @Mocked @Cascading
    private IInvokedMethod mockInvokedMethod;

    public MethodActionListenerTest() {}

    /**
     * <p>Test the {@link IInvokedMethodListener2#beforeInvocation} method, which should call the {@link ActionListener#executeBeforeAction()} method.</p>
     */
    @Test
    public void beforeInvocation()
    {
        CheckMethodListener listener = new CheckMethodListener();
        listener.beforeInvocation(buildMockInvokedMethod(), mockTestResult, mockTestContext);

        Assert.assertTrue(
            listener.beforeGetCalled
        );
    }
    /**
     * <p>Test the {@link IInvokedMethodListener2#afterInvocation} method, which should call the {@link ActionListener#executeAfterAction()} method.</p>
     */
    @Test
    public void afterInvocation()
    {
        CheckMethodListener listener = new CheckMethodListener();
        listener.afterInvocation(buildMockInvokedMethod(), mockTestResult, mockTestContext);

        Assert.assertTrue(
            listener.afterGetCalled
        );
    }

    private IInvokedMethod buildMockInvokedMethod()
    {
        new NonStrictExpectations() {{
            mockInvokedMethod.isTestMethod();
            result = true;
            mockInvokedMethod.getTestMethod().getMethodName();
            result = "Mock Method Name";
        }};

        return mockInvokedMethod;
    }
}

class CheckMethodListener extends MethodActionListener {
    boolean beforeGetCalled = false;
    boolean afterGetCalled = false;

    CheckMethodListener() {}

    @Override
    public ActionListener buildActionListener()
    {
        return new CheckActionListener();
    }

    private class CheckActionListener extends ActionListener {
        private CheckActionListener() {}

        @Override
        public Action getBeforeAction()
        {
            beforeGetCalled = true;
            return null;
        }
        @Override
        public Action getAfterAction()
        {
            afterGetCalled = true;
            return null;
        }
    }
}
