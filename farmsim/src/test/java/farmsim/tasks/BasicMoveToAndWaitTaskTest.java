package farmsim.tasks;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import farmsim.util.Point;
import farmsim.world.World;

public class BasicMoveToAndWaitTaskTest {

    private MoveToAndWaitTask task;

    @Before
    public void createInstance() {
        // TODO fix the null world.
        // task = new BasicMoveToAndWaitTask(1, 2, null);
    }

    /**
     * Tests the inProgress() method for both {@link MoveToAndWaitTask} and
     * {@link AbstractTask}.
     */
    @Test
    public void inProgressTest() {
        // assertEquals("Task should not be in progress.", false,
        // task.isInProgress());
        /*
         * TODO: This is a blatant hack, needs to be fixed.
         */
        // task.startTask();
        // assertEquals("Task should be in progress.", true,
        // task.isInProgress());
    }

    /**
     * Tests the getLocation() method for both {@link MoveToAndWaitTask} and
     * {@link AbstractTask}.
     */
    @Test
    public void getLocationTest() {
        // assertEquals("Location mismatch.", new Point(1, 2),
        // task.getLocation());
    }

}
