package org.no_ip.mikelue.jpa.test.testng;

import static org.no_ip.mikelue.jpa.test.testng.Action.NULL_ACTION;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The listener controller for executing {@link Action}.<p>
 *
 * This controller is needed to be inherited and the sub-class should
 * override {@link #getBeforeAction()} or {@link #getAfterAction()} to setup
 * the {@link Action} for running by this controller.<p>
 *
 * This type is a general case for different running scopes of listeners in <a href="http://testng.org/doc/documentation-main.html#annotations">TestNG</a>.
 */
public abstract class ActionListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ActionListener() {}

    /**
     * Execute the {@link Action} which comes from {@link #getBeforeAction()}.<p>
     *
     * @throws ExecuteActionException The exception thrown from {@link Action#executeAction()}
     */
    public final void executeBeforeAction() throws ExecuteActionException
    {
        Action action = getBeforeAction();

        if (action == null || NULL_ACTION.equals(action)) {
            checkForWarning(action, getAfterAction());
            getLogger().debug("Nothing to run in before action");
            return;
        }

        getLogger().info("Run before action");
        action.executeAction();
    }
    /**
     * Execute the {@link Action} which comes from {@link #getAfterAction()}.<p>
     *
     * @throws ExecuteActionException The exception thrown from {@link Action#executeAction()}
     */
    public final void executeAfterAction() throws ExecuteActionException
    {
        Action action = getAfterAction();

        if (action == null || NULL_ACTION.equals(action)) {
            checkForWarning(getBeforeAction(), action);
            getLogger().debug("Nothing to run in after action");
            return;
        }

        getLogger().info("Run after action");
        action.executeAction();
    }

    /**
     * Get the logger.
     *
     * @return The logger with name of type of instance
     */
    public Logger getLogger()
    {
        return logger;
    }

    /**
     * This method should be overrided by sub-class if it wants to execute action before something.<p>
     *
     * @return the initialized {@link Action}, or nothing get executed by return null
     */
    public Action getBeforeAction()
    {
        return null;
    }
    /**
     * This method should be overrided by sub-class if it wants to execute action after something.<p>
     *
     * @return the initialized {@link Action}, or nothing get executed by return null
     */
    public Action getAfterAction()
    {
        return null;
    }

    private void checkForWarning(Action beforeAction, Action afterAction)
    {
        if (
            (beforeAction == null || NULL_ACTION.equals(beforeAction))
            &&
            (afterAction == null || NULL_ACTION.equals(afterAction))
        ) {
            getLogger().warn("Neither getBeforeAction() nor getAfterAction() has instance.");
        }
    }
}
