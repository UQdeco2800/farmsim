package farmsim.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;

public class PloughTaskTest {

    private PloughTask task;
    private World mockWorld;

    @Before
    public void createInstance() {
        mockWorld = mock(World.class);
        when(mockWorld.getWidth()).thenReturn(10);
        when(mockWorld.getHeight()).thenReturn(10);
        TileRegister mockRegister = mock(TileRegister.class);
        when(mockRegister.getTileType(null)).thenReturn(10);
        TileRegister.setInstance(mockRegister);

        task = new PloughTask(1, 2, mockWorld);
        assertNotEquals(task, null);
    }

    @Test
    public void isAdjacentTest() {
        assertEquals("Adjacent on left.",
                task.isAdjacent(new PloughTask(2, 2, mockWorld)), true);
        assertEquals("Adjacent on right.",
                task.isAdjacent(new PloughTask(0, 2, mockWorld)), true);
        assertEquals("Adjacent above.",
                task.isAdjacent(new PloughTask(1, 1, mockWorld)), true);
        assertEquals("Adjacent below.",
                task.isAdjacent(new PloughTask(1, 3, mockWorld)), true);

        assertEquals("Not Adjacent.",
                task.isAdjacent(new PloughTask(0, 0, mockWorld)), false);
        assertEquals("Not Adjacent, same Y Plane",
                task.isAdjacent(new PloughTask(5, 2, mockWorld)), false);
        assertEquals("Not Adjacent, same X plane",
                task.isAdjacent(new PloughTask(1, 5, mockWorld)), false);
    }

    /**
     * Tests the inProgress() method for both {@link MoveToAndWaitTask} and
     * {@link AbstractTask}.
     */
    @Test
    public void inProgressTest() {
        assertEquals("Not in progress since hasn't been started.",
                task.isInProgress(), false);
        task.startTask();
        assertEquals("someone has started the task, so it is now in progress.",
                task.isInProgress(), true);

    }

    @Test
    public void getNameTest() {
        assertEquals("Name should be Plough.", task.getName(), "Plough");
    }

    public void getDurationTest() {
        PloughTask task2 = new PloughTask(5, 5, mockWorld);
        assertEquals("Plough task default duration should equal any other"
                + "plough task's duration.", task.getDuration(),
                task2.getDuration());
        assertNotEquals("Task duration should not be null.",
                task.getDuration(), null);
    }

    /**
     * Tests the getLocation() method for both {@link MoveToAndWaitTask} and
     * {@link AbstractTask}.
     */
    @Test
    public void getLocationTest() {
        assertEquals("Location mismatch.", new Point(1, 2), task.getLocation());
    }

}
