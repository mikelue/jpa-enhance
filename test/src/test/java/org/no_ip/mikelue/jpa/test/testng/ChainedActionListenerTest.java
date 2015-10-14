package org.no_ip.mikelue.jpa.test.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainedActionListenerTest {
    public ChainedActionListenerTest() {}

    /**
     * <p>Test the running sequence for before action.</p>
     */
    @Test
    public void beforeActions()
    {
        final List<Integer> beforeRecord = new ArrayList<Integer>();

        /**
         * Initialize before actions
         */
        ActionListener testListener = new ChainedActionListener(
            new ActionListener() {
                @Override
                public Action getBeforeAction()
                {
                    return new NumberedAction(beforeRecord, 1);
                }
            },
            new ActionListener() {
                @Override
                public Action getBeforeAction()
                {
                    return new NumberedAction(beforeRecord, 2);
                }
            }
        );
        // :~)

        testListener.executeBeforeAction();

        Assert.assertEquals(
            beforeRecord, Arrays.asList(1, 2)
        );
    }

    /**
     * <p>Test the running sequence for after actions.</p>
     */
    @Test
    public void afterActions()
    {
        final List<Integer> afterRecord = new ArrayList<Integer>(2);

        /**
         * Initialize actions
         */
        ActionListener testListener = new ChainedActionListener(
            new ActionListener() {
                @Override
                public Action getAfterAction()
                {
                    return new NumberedAction(afterRecord, 1);
                }
            },
            new ActionListener() {
                @Override
                public Action getAfterAction()
                {
                    return new NumberedAction(afterRecord, 2);
                }
            }
        );

        testListener.executeAfterAction();

        Assert.assertEquals(
            afterRecord, Arrays.asList(2, 1)
        );
    }
}
