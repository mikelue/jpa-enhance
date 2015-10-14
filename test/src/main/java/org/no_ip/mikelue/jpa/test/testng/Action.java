package org.no_ip.mikelue.jpa.test.testng;

import org.testng.ITestNGListener;

/**
 * This type is the generic type to descibe the action
 * <p>which could be executed in {@link ITestNGListener} or beforeXXX/afterXXX annotation.</p>
 */
public interface Action {
    /**
     * This instance is for null action which is used for
     * initialization activities.
     */
    public static final Action NULL_ACTION = new Action() {
        @Override
        public void executeAction() throws ExecuteActionException {}
    };

    /**
     * <p>The main method for executing action.</p>
     *
     * @throws ExecuteActionException The wrapper exception thrown from executor
     */
    public void executeAction() throws ExecuteActionException;
}
