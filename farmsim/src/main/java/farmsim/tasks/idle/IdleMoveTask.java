package farmsim.tasks.idle;

import farmsim.world.World;

/**
 * Idle task that is used to move the agent around.
 */
public class IdleMoveTask extends IdleTask {
    /**
     * Tells the Agent to idle at a specific location. THIS TASK IS ONLY TO BE
     * INSTANSIATED BY THE AGENT MANAGER.
     *
     * @param x     The {@link Tile} x coordinate of the task.
     * @param y     The {@link Tile} y coordinate of the task.
     * @param world The {@link World} for which the task need to be completed.
     */
    public IdleMoveTask(int x, int y, World world) {
        super(x, y, world);
    }
}
