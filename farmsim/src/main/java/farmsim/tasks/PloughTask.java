package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.tools.ToolType;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.ui.Notification;
import farmsim.util.Point;
import farmsim.util.Sound;
import farmsim.world.World;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * PloughTask requires an Agent turn a tile into ploughed dirt.
 * 
 * @author Leggy
 *
 */

public class PloughTask extends AgentRoleTask {
    private static final int BASE_DURATION = 1000;
    private static final String NAME = "Plough";
    private static final String ID = "plough";
    private Sound plowing = new Sound("plow.mp3");
    private static final ToolType BONUS_TOOL = ToolType.HOE;

    private TextArea output = null;
    private static final int PLOUGHED_DIRT =
            TileRegister.getInstance().getTileType("ploughed_dirt");

    public PloughTask(int x, int y, World world) {
        super(new Point(x, y), BASE_DURATION, world, NAME, ID);
        // Adjust duration depending on agent's level in required role
        this.duration *= defaultRoleLevelTimeModifier();
        setBonusTool(BONUS_TOOL);
    }

    public PloughTask(Point point, World world, TextArea output) {
        super(point, BASE_DURATION, world, NAME, ID);
        this.output = output;
        setBonusTool(BONUS_TOOL);
    }
    
    public PloughTask copy() {
    	return new PloughTask(location, world, output);
    }

    @Override
    public void preTask() {
        plowing.play();
    }

    @Override
    public void postTask() {
        world.setTile(location,
                TileRegister.getInstance().getTileType("ploughed_dirt"));
        if (world.getTile(location).hasProperty(TileProperty.HAS_SECRET_GOLD)) {
            if ((boolean) world.getTile(location)
                    .getProperty(TileProperty.HAS_SECRET_GOLD)) {
                world.getTile(location)
                        .removeProperty(TileProperty.HAS_SECRET_GOLD);
            }
        }
        if(output != null) {
            Platform.runLater(() -> { 
                output.appendText("Ploughed a field" + 
                        System.getProperty("line.separator"));
            });
        }
        plowing.stop();
    }

    @Override
    public boolean isValid() {
    	TileRegister tileRegister = TileRegister.getInstance();
    	int grass = tileRegister.getTileType("grass");
        int grass2 = tileRegister.getTileType("grass2");
        int grass3 = tileRegister.getTileType("grass3");
        int dirt = tileRegister.getTileType("dirt");
    	int tileType = tile.getTileType();
        if (!super.isValid()) {
            return false;
        }
        /*
         * For every point in our selection, we get the tile located in that
         * position, and if the tile type is either grass or dirt, and there
         * is not a tile entity in that position, we plough the tile.
         */

        return ((tileType == dirt || tileType == grass || tileType == grass2 || 
        		tileType == grass3) && tile.getTileEntity() == null);
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.FARMER;
    }

    @Override
    public int experienceGained() {
        return 2;
    }
}
