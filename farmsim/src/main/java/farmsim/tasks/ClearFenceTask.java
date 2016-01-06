package farmsim.tasks;

import farmsim.entities.tileentities.objects.Fence;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.entities.tools.ToolType;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * ClearFenceTask allows an agent to turn a Fence tile into a blank tile. Agents
 * must have an axe equipped to clear fences.
 *
 * @author hoyland6
 *
 */
public class ClearFenceTask extends ClearLandTask {

    /* Could potentially include variable duration for land types */
    private static final int BASE_DURATION = 2000;
    private static final String NAME = "Clear fence";


    public ClearFenceTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME);
    }

    /**
     * ClearFenceTask is valid so long as the tile is of type "fence", the tile
     * is clearable, and the peon is equipped with an axe.
     */
    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        TileEntity entity = tile.getTileEntity();
        return entity instanceof Fence && ((BaseObject) entity).isClearable();
        // TODO: Check if peon has an axe equipped. If not, automatically
        // wield peon's axe if they have one. If no axe, invalid.
    }

    @Override
    public AbstractTask copy() {
        return new ClearFenceTask(location, world);
    }

}
