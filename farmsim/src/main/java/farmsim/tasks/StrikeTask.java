package farmsim.tasks;

import farmsim.util.Point;
import farmsim.world.World;

public class StrikeTask extends AbstractTask {

    public StrikeTask(int x, int y, World world, long duration) {
        super(new Point(x, y), (int) duration, world, "Strike", "strike");
    }
    
    public StrikeTask(Point point, World world, long duration) {
        super(point, (int) duration, world, "Strike", "strike");
    }
    
    public StrikeTask copy() {
    	return new StrikeTask(location, world, duration);
    }

    @Override
    public void preTask() {}

    @Override
    public void postTask() {}

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        // TODO Auto-generated method stub
        return false;
    }
}
