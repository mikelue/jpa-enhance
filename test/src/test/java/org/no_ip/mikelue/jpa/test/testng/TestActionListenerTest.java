package org.no_ip.mikelue.jpa.test.testng;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;

public class TestActionListenerTest {
    @Mocked
    private ITestContext mockContext;

    public TestActionListenerTest() {}

    /**
     * Test the {@link ITestListener#onStart} method, which should call the {@link ActionListener#executeBeforeAction()} method.<p>
     */
    @Test
    public void onStart()
    {
        CheckTestListener listener = new CheckTestListener();
        listener.onStart(buildMockContext());

        Assert.assertTrue(
            listener.beforeGetCalled
        );
    }
    /**
     * Test the {@link ITestListener#onFinish} method, which should call the {@link ActionListener#executeAfterAction()} method.<p>
     */
    @Test
    public void onFinish()
    {
        CheckTestListener listener = new CheckTestListener();
        listener.onStart(buildMockContext());

        Assert.assertTrue(
            listener.afterGetCalled
        );
    }

    private ITestContext buildMockContext()
    {
        new NonStrictExpectations() {{
            mockContext.getName();
            result = "Mocked Test Name";
        }};

        return mockContext;
    }
}

class CheckTestListener extends TestActionListener {
    boolean beforeGetCalled = false;
    boolean afterGetCalled = false;

    CheckTestListener() {}

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
