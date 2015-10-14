package guru.mikelue.jpa.test.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainedActionTest {
    public ChainedActionTest() {}

    /**
     * <p>Test the chained execution for {@link Action}.</p>
     */
    @Test
    public void executeAction()
    {
        final List<Integer> record = new ArrayList<Integer>(2);

        new ChainedAction(
            new NumberedAction(record, 2),
            new NumberedAction(record, 1)
        ).executeAction();

        Assert.assertEquals(
            record, Arrays.asList(2, 1)
        );
    }
}

class NumberedAction implements Action {
    private int id;
    private List<Integer> record;

    NumberedAction(List<Integer> newRecord, int newId)
    {
        this.id = newId;
        this.record = newRecord;
    }

    @Override
    public void executeAction() throws ExecuteActionException
    {
        record.add(id);
    }
}
