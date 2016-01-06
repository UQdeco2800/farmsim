package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.tileentities.objects.Water;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.util.Point;
import farmsim.util.Sound;
import farmsim.world.World;
import javafx.scene.control.TextArea;

/**
 * RaiseLandTask increases the elevation of a tile by 1
 *
 */
public class RaiseLandTask extends AgentRoleTask {
    private static final int BASE_DURATION = 2000;
    private static final String NAME = "Raise land";
    private static final String ID = "land";
    private TextArea output = null;
    private Sound sound = new Sound("shovel.mp3");

    public RaiseLandTask(int x, int y, World world) {
        super(new Point(x, y), BASE_DURATION, world, NAME, ID);
    }

    public RaiseLandTask(Point point, World world, TextArea output) {
        super(point, BASE_DURATION, world, NAME, ID);
        // Adjust duration depending on agent's level in required role
        this.duration *= defaultRoleLevelTimeModifier();
        this.output = output;
    }

    @Override
    public void preTask() {
        sound.play();
    }

    @Override
    public void postTask() {
        Tile tile = world.getTile((int) location.getX(), (int) location.getY());
        if (tile.getProperty(TileProperty.ELEVATION) == null) {
            return;
        }

        int elevation = (int) tile.getProperty(TileProperty.ELEVATION);
        if (elevation == World.MAX_ELEVATION) {
            return;
        }

        tile.setProperty(TileProperty.ELEVATION, elevation + 1);
        if (output != null) {
            output.appendText("Raised elevation of land"
                    + System.getProperty("line.separator"));
        }
        sound.stop();
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        if (tile.getTileEntity() == null) {
            return true;
        }
        return tile.getTileEntity().getClass() != Water.class;
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
        return new RaiseLandTask((int)location.getX(), (int)location.getY(), world);
    }

}
