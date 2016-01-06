package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * Removes all pollution from a tile
 *
 */
public class ScrubTask extends AgentRoleTask {
    private static final int BASE_DURATION = 1000;
    private static final String NAME = "Scrub";
    private static final String ID = "scrub";

    private static final int DIRT =
            TileRegister.getInstance().getTileType("dirt");

    public ScrubTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME, ID);
        // Adjust duration depending on agent's level in required role
        this.duration *= defaultRoleLevelTimeModifier();
    }
    
    public ScrubTask copy() {
    	return new ScrubTask(location, world);
    }

    @Override
    public void preTask() {
        double pollutionValue = Double.valueOf(
                world.getTile((int) location.getX(), (int) location.getY())
                        .getPollution());
        if (pollutionValue == 0) {
            duration = 0;
        }
    }

    @Override
    public void postTask() {
        world.getTile((int) location.getX(), (int) location.getY())
                .setPollution(0);
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        /*
         * MakeDirtTask is valid so long as the tile is not dirt, and there is
         * not an entity on the tile.
         */
        return tile.getTileType() != DIRT && tile.getTileEntity() == null;
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.FARMER;
    }

    @Override
    public int experienceGained() {
        return 0;
    }

}
