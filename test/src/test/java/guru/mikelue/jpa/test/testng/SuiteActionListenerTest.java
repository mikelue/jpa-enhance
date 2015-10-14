package guru.mikelue.jpa.test.testng;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.ISuite;

public class SuiteActionListenerTest {
    @Mocked
    private ISuite mockSuite;

    public SuiteActionListenerTest() {}

    /**
     * <p>Test the {@link ISuite#onStart} method, which should call the {@link ActionListener#executeBeforeAction()} method.</p>
     */
    @Test
    public void onStart()
    {
        CheckSuiteListener listener = new CheckSuiteListener();
        listener.onStart(buildMockSuite());

        Assert.assertTrue(
            listener.beforeGetCalled
        );
    }
    /**
     * <p>Test the {@link ISuite#onFinish} method, which should call the {@link ActionListener#executeAfterAction()} method.</p>
     */
    @Test
    public void onFinish()
    {
        CheckSuiteListener listener = new CheckSuiteListener();
        listener.onStart(buildMockSuite());

        Assert.assertTrue(
            listener.afterGetCalled
        );
    }

    private ISuite buildMockSuite()
    {
        new NonStrictExpectations() {{
            mockSuite.getName();
            result = "Mocked Suite Name";
        }};

        return mockSuite;
    }
}

class CheckSuiteListener extends SuiteActionListener {
    boolean beforeGetCalled = false;
    boolean afterGetCalled = false;

    CheckSuiteListener() {}

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
