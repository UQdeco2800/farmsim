package farmsim.buildings;

import farmsim.tasks.SeeLeggyTask;
import farmsim.tasks.TaskManager;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * This duck shrine is dedicated to leggy and all his hard work over the
 * semester with DecoFarm.
 * Created by ShardeNel on 13/10/2015.
 */
public class LeggyShrine extends AbstractBuilding {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 4;
    public static final String SPRITE_LOCATION = "/buildings/duckstatue.png";


    public LeggyShrine(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }


    /**
     * when double clicked create a new see Leggy Task.
     * note: chance to use the same worker multiple times but task manager
     * is supposed to allow player to set different workers to the task.
     */
    @Override
    public void onDoubleClick(Point location) {
        TaskManager.getInstance().addTask(new SeeLeggyTask(this));
    }
}

