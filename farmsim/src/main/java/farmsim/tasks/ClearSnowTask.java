package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.entities.tileentities.objects.Snow;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * ClearSnowTask allows an Agent to turn a snow tile into a blank tile. 
 * 
 * 
 * @author rachelcatchpoole for Team Adleman
 *
 */
public class ClearSnowTask extends ClearLandTask {
    private static final int BASE_DURATION = 2000;
    private static final int BASE_PRIORITY = 25;
    private static final String NAME = "Clear snow";

    public ClearSnowTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME);
    }
    
    /**
     * ClearSnowTask is valid so long as the tile is Snow and clearable.
     */
    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        TileEntity entity = tile.getTileEntity();
        return entity instanceof Snow && ((BaseObject) entity).isClearable();
    }
    
    @Override
    public AbstractTask copy() {
        return new ClearSnowTask(location, world);
    }
}
