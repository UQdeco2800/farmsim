package farmsim.tasks.idle;

import farmsim.tasks.MoveToAndWaitTask;
import farmsim.tiles.Tile;
import farmsim.world.World;

/**
 * IdleTask is a placeholder task to keep Agents busy while there are no tasks
 * for the TaskManager to serve. THIS TASK IS ONLY TO BE INSTANSIATED BY THE
 * AGENT MANAGER.
 * 
 * @author Leggy
 *
 */
public abstract class IdleTask extends MoveToAndWaitTask {
    private static final int BASE_DURATION = 20000;

    /**
     * Tells the Agent to idle at a specific location. THIS TASK IS ONLY TO BE
     * INSTANSIATED BY THE AGENT MANAGER.
     *
     * @param x The {@link Tile} x coordinate of the task.
     * @param y The {@link Tile} y coordinate of the task.
     * @param world The {@link World} for which the task need to be completed.
     */
    public IdleTask(int x, int y, World world) {
        super(x, y, BASE_DURATION, world);
    }

    /**
     * Tells the Agent to idle at a specific location. THIS TASK IS ONLY TO BE
     * INSTANSIATED BY THE AGENT MANAGER.
     *
     * @param x The {@link Tile} x coordinate of the task.
     * @param y The {@link Tile} y coordinate of the task.
     * @param duration The length of time to wait at location.
     * @param world The {@link World} for which the task need to be completed.
     */
    public IdleTask(int x, int y, int duration, World world) {
        super(x, y, duration, world);
    }
}
