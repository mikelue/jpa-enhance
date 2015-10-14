package guru.mikelue.jpa.test.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>The container class which has chained action to be executed in order.</p>
 */
public class ChainedAction implements Action {
    private Logger logger = LoggerFactory.getLogger(ChainedAction.class);
    private Action[] chainedActions;

    /**
     * <p>Initialize this object by providing array of {@link Action}.</p>
     *
     * @param newChainedActions The ordered {@link Action}s are going to be executed
     */
    public ChainedAction(Action... newChainedActions)
    {
        chainedActions = newChainedActions;
        logger.debug("Initialize chained actions: [{}] actions", chainedActions.length);
    }

    /**
     * <p>Execute actions in initialized order.</p>
     *
     * @throws ExecuteActionException The wrapper exception thrown from executor
     */
    @Override
    public void executeAction() throws ExecuteActionException
    {
        logger.debug("Execute chained action: [{}] actions", chainedActions.length);

        for (int i = 0; i < chainedActions.length; i++) {
            try {
                chainedActions[i].executeAction();
            } catch (ExecuteActionException e) {
                logger.error("Execute [#{}] action error", i);
                throw e;
            }
        }

        logger.debug("Execute chained action: [{}] actions. Finished", chainedActions.length);
    }
}
