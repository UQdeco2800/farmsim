package farmsim.tasks;

import farmsim.entities.machines.MachineType;
import farmsim.entities.tileentities.objects.Tree;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.entities.tools.ToolType;
import farmsim.util.Point;
import farmsim.util.Sound;
import farmsim.world.World;

/**
 * ClearTreeTask allows an Agent to turn a tree tile into a blank tile. Agents
 * must have an axe equipped to clear a tree.
 * 
 * @author rachelcatchpoole
 *
 */
public class ClearTreeTask extends ClearLandTask {
    private static final int BASE_DURATION = 2000;
    private static final String NAME = "Clear tree";
    private static final ToolType REQUIRED_TOOL = ToolType.AXE;
    private static final MachineType REQUIRED_MACHINE = MachineType.CHAINSAW;
    private Sound sound = new Sound("shovel.mp3");

    public ClearTreeTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME);
        setRequiredTool(REQUIRED_TOOL);
        setRequiredMachine(REQUIRED_MACHINE);
    }

    /**
     * ClearTreeTask is valid so long as the tile is a tree, the tile is
     * clearable, and the peon is equipped with an axe.
     */
    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        TileEntity entity = tile.getTileEntity();
        return entity instanceof Tree && ((BaseObject) entity).isClearable();
        // TODO: Check if peon has an axe equipped. If not, automatically wield
        // peon's axe if they have one. If no axe, invalid.
    }
    
    @Override
    public AbstractTask copy() {
        return new ClearTreeTask(location, world);
    }

    @Override
    public void preTask() {
        sound.play();
    }

    @Override
    public void postTask() {
        sound.stop();
    }
}
