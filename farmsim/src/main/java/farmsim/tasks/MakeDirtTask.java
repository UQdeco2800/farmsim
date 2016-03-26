package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.tools.ToolType;
import farmsim.tiles.TileRegister;
import farmsim.ui.Notification;
import farmsim.util.Point;
import farmsim.world.World;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * MakeDirtTask requires an Agent turn a tile into dirt.
 * 
 * @author Leggy
 *
 */
public class MakeDirtTask extends AgentRoleTask {
    private static final int BASE_DURATION = 1000;
    private static final String NAME = "Make dirt";
    private static final String ID = "make-dirt";
    private TextArea output;

    private static final ToolType BONUS_TOOL = ToolType.SHOVEL;

    private static final int DIRT =
            TileRegister.getInstance().getTileType("dirt");

    public MakeDirtTask(int x, int y, World world) {
        super(new Point(x, y), BASE_DURATION, world, NAME, ID);
        // Adjust duration depending on agent's level in required role
        this.duration *= defaultRoleLevelTimeModifier();
        setBonusTool(BONUS_TOOL);
    }

    public MakeDirtTask(Point point, World world, TextArea output) {
        super(point, BASE_DURATION, world, NAME, ID);
        this.output = output;
        setBonusTool(BONUS_TOOL);
    }
    
    public MakeDirtTask copy() {
    	return new MakeDirtTask(location, world, output);
    }

    @Override
    public void preTask() {

    }

    @Override
    public void postTask() {
        world.setTile((int) location.getX(), (int) location.getY(),
                TileRegister.getInstance().getTileType("dirt"));
        if(output != null) {
            Platform.runLater(() -> { 
                output.appendText("Made a dirt field" + System.
                        getProperty("line.separator"));
            });
        }
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
        return 1;
    }

}
