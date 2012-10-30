package org.no_ip.mikelue.jpa.test.testng;

import static org.no_ip.mikelue.jpa.test.testng.Action.NULL_ACTION;

import java.util.ArrayList;
import java.util.List;

/**
 * This listener is a chained version of {@link ActionListener}, as {@link ChainedAction} do.<p>
 *
 * This is a container type which contains multiple {@link ChainedActionListener}s
 * for generating of {@link ChainedActionListener#getBeforeAction} in intialized order
 * and of {@link ChainedActionListener#getAfterAction} in reversed order.<p>
 *
 * This class would build {@link ChainedAction} lazily and as singleton instance.<p>
 */
public class ChainedActionListener extends ActionListener {
    private ActionListener[] chainedActionListeners;
    private Action beforeActions = null;
    private Action afterActions = null;

    /**
     * Initialized ordered {@link ActionListener}s.<p>
     *
     * @param newChainedListeners The executed
     */
    public ChainedActionListener(ActionListener... newChainedListeners)
    {
        chainedActionListeners = newChainedListeners;
    }

    /**
     * Generates an {@link ChainedAction} which has order as same as initialized listeners.<p>
     *
     * @return the initialized {@link Action}, or nothing get executed by return null
     */
    @Override
    public Action getBeforeAction()
    {
        if (beforeActions == null) {
            /**
             * Initialize before actions
             */
            List<Action> actions = new ArrayList<Action>(chainedActionListeners.length);
            for (int i = 0; i < chainedActionListeners.length; i++) {
                addActionToList(actions, chainedActionListeners[i].getBeforeAction());
            }
            getLogger().debug("Chained listeners for before action: [{}]", actions.size());
            beforeActions = buildChainedActionFromList(actions);
            // :~)
        }

        return beforeActions;
    }
    /**
     * Generates an {@link ChainedAction} which has reversed order coming from initialied listeners.<p>
     *
     * @return the initialized {@link Action}, or nothing get executed by return null
     */
    @Override
    public Action getAfterAction()
    {
        if (afterActions == null) {
            /**
             * Initialize after actions(reversed)
             */
            List<Action> actions = new ArrayList<Action>(chainedActionListeners.length);
            for (int i = chainedActionListeners.length - 1; i >= 0; i--) {
                addActionToList(actions, chainedActionListeners[i].getAfterAction());
            }
            getLogger().debug("Chained listeners for after action: [{}]", actions.size());
            afterActions = buildChainedActionFromList(actions);
            // :~)
        }

        return afterActions;
    }

    private void addActionToList(List<Action> holderList, Action action)
    {
        if (action != null || !NULL_ACTION.equals(action)) {
            holderList.add(action);
        }
    }
    private Action buildChainedActionFromList(List<Action> chainedActions)
    {
        return chainedActions.size() == 0 ?
            NULL_ACTION :
            new ChainedAction(
                chainedActions.toArray(new Action[0])
            );
    }
}
