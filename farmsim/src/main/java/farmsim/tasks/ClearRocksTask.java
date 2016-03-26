package farmsim.tasks;

import farmsim.entities.tileentities.objects.Rock;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.entities.tools.ToolType;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * ClearRockTask allows an agent to turn a Rock tile into a blank tile. Agents
 * must have a pickaxe equipped to clear rocks.
 * 
 * @author rachelcatchpoole
 *
 */
public class ClearRocksTask extends ClearLandTask {

    /* Could potentially include variable duration for land types */
    private static final int BASE_DURATION = 2000;
    private static final String NAME = "Clear rocks";
    private static final ToolType REQUIRED_TOOL = ToolType.PICKAXE;

    public ClearRocksTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME);
        setRequiredTool(REQUIRED_TOOL);
    }

    /**
     * ClearRockTask is valid so long as the tile is of type "rocks", the tile
     * is clearable, and the peon is equipped with an axe.
     */
    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        TileEntity entity = tile.getTileEntity();
        return entity instanceof Rock && ((BaseObject) entity).isClearable();
        // TODO: Check if peon has a pickaxe equipped. If not, automatically
        // wield peon's pickaxe if they have one. If no pickaxe, invalid.
    }
    
    @Override
    public AbstractTask copy() {
        return new ClearRocksTask(location, world);
    }

    @Override
    public void preTask() {
    }

    @Override
    public void postTask() {
    }
}
