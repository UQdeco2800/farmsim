package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * ClearLandTask allows for unusable land to be cleared upon purchase
 * 
 * @author BlueDragon23, rachelcatchpoole
 *
 */
public class ClearLandTask extends AgentRoleTask {

    /* Could potentially include variable duration for land types */
    private static final int BASE_DURATION = 5000;
    private static final String NAME = "Clear land";
    private static final String ID = "clear";

    public ClearLandTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME, ID);
    }
    
    public ClearLandTask(Point point, int duration, World world, String name) {
        super(point, duration, world, name, ID);
        // Adjust duration depending on agent level
        this.duration *= defaultRoleLevelTimeModifier();
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.BUILDER;
    }

    @Override
    public int experienceGained() {
        return 2;
    }
    
    @Override
    public AbstractTask copy() {
    	return new ClearLandTask(location, world);
    }

    @Override
    public void preTask() {

    }

    @Override
    public void postTask() {
        Tile tile = world.getTile((int) location.getX(), (int) location.getY());
        tile.setTileEntity(null);
        /* Should make dependent on current tile */
        tile.setTileType(TileRegister.getInstance().getTileType("dirt"));
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        TileEntity entity = tile.getTileEntity();
        return entity instanceof BaseObject
                && ((BaseObject) entity).isClearable();
    }

}
