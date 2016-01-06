package farmsim.tasks;

import farmsim.tiles.Tile;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * MoveToAndWaitTask is a basic implementation of {@link AbstractTask} to
 * demonstrate Agents' ability to complete tasks.
 * 
 * @author Leggy
 *
 */
public class MoveToAndWaitTask extends AbstractTask {

    private static final int BASE_DURATION = 1000;
    private static final String NAME = "Move";
    private static final String ID = "move";

    /**
     * Creates a new MoveToAndWaitTask, with a configurable duration.
     * 
     * @param x The {@link Tile} x coordinate of the task.
     * @param y The {@link Tile} y coordinate of the task.
     * @param duration The time (in milliseconds) this task takes to complete.
     * @param world The {@link World} for which the task need to be completed.
     */
    public MoveToAndWaitTask(int x, int y, int duration, World world) {
        super(new Point(x, y), duration, world, NAME, ID);
    }

    /**
     * Creates a new MoveToAndWaitTask, with the default duration of 1000ms.
     *
     * @param x The {@link Tile} x coordinate of the task.
     * @param y The {@link Tile} y coordinate of the task.
     * @param world The {@link World} for which the task need to be completed.
     */
    public MoveToAndWaitTask(int x, int y, World world) {
        super(new Point(x, y), BASE_DURATION, world, NAME, ID);
    }
    
    public MoveToAndWaitTask copy() {
    	return new MoveToAndWaitTask((int)location.getX(), (int)location.getY(), world);
    }

    @Override
    public void preTask() {
        // Do nothing
    }

    @Override
    public void postTask() {
        // Do nothing
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        /*
         * This task is invalid for BaseObject, as workers should not move
         * through impassable terrain. Need to convert point to TileEntity, then
         * check that it isn't BaseObject.
         */
        return true;
    }

}
