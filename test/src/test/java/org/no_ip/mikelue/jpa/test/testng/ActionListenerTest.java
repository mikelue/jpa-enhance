package org.no_ip.mikelue.jpa.test.testng;

import static org.no_ip.mikelue.jpa.test.testng.Action.NULL_ACTION;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ActionListenerTest {
    public ActionListenerTest() {}

    /**
     * Test the running for nothing.
     */
    @Test
    public void runNothing()
    {
        ActionListener nativeNullListener = new RunNothingListener();
        nativeNullListener.executeBeforeAction();
        nativeNullListener.executeAfterAction();

        ActionListener nullObjectListener = new RunNullObjectListener();
        nullObjectListener.executeBeforeAction();
        nullObjectListener.executeAfterAction();
    }
    /**
     * Test the running before action.
     */
    @Test
    public void runBeforeAction()
    {
        RunBeforeListener listener = new RunBeforeListener();
        listener.executeBeforeAction();

        Assert.assertTrue(
            listener.action.executed
        );
    }
    /**
     * Test the running after action.
     */
    @Test
    public void runAfterAction()
    {
        RunAfterListener listener = new RunAfterListener();
        listener.executeAfterAction();

        Assert.assertTrue(
            listener.action.executed
        );
    }
}

class FakeAction implements Action {
    boolean executed = false;

    FakeAction() {}

    @Override
    public void executeAction() throws ExecuteActionException
    {
        executed = true;
    }
}
class RunNothingListener extends ActionListener {
    RunNothingListener() {}
}
class RunNullObjectListener extends ActionListener {
    RunNullObjectListener() {}

    @Override
    public Action getBeforeAction()
    {
        return NULL_ACTION;
    }
    @Override
    public Action getAfterAction()
    {
        return NULL_ACTION;
    }
}
class RunBeforeListener extends ActionListener {
    FakeAction action = new FakeAction();

    RunBeforeListener() {}

    @Override
    public Action getBeforeAction()
    {
        return action;
    }
}
class RunAfterListener extends ActionListener {
    FakeAction action = new FakeAction();

    RunAfterListener() {}

    @Override
    public Action getAfterAction()
    {
        return action;
    }
}
