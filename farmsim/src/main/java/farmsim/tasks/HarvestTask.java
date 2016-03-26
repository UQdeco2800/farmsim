package farmsim.tasks;

import farmsim.resource.SimpleResource;
import farmsim.entities.agents.Agent;
import farmsim.entities.tileentities.crops.Crop;
import farmsim.entities.tools.ToolType;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * PlantTask requires an Agent to plant a seed on plowed dirt.
 * 
 * @author Leggy
 *
 */
public class HarvestTask extends AgentRoleTask {

    private static final int BASE_DURATION = 1000;
    private static final String NAME = "Harvest";
    private static final String ID = "harvest";
    private TextArea output;
    private static final ToolType BONUS_TOOL = ToolType.SICKLE;

    public HarvestTask(Point point, World world, TextArea output) {
        super(point, BASE_DURATION, world, NAME, ID);
        this.output = output;
        // Adjust duration depending on agent level
        this.duration *= defaultRoleLevelTimeModifier();
        setBonusTool(BONUS_TOOL);
    }
    
    public HarvestTask copy() {
    	return new HarvestTask(location, world, output);
    }

    @Override
    public void preTask() {
    }

    @Override
    public void postTask() {
        Tile tile = world.getTile((int) location.getX(), (int) location.getY());
        String c;
        ArrayList<String> deadStages = new ArrayList<>();
        deadStages.add("seed");
        deadStages.add("Apple1dead");
        deadStages.add("Apple2dead");
        deadStages.add("Apple3dead");
        deadStages.add("Banana1dead");
        deadStages.add("Banana2dead");
        deadStages.add("Banana3dead");
        deadStages.add("Corn1dead");
        deadStages.add("Corn2dead");
        deadStages.add("Corn3dead");
        deadStages.add("Cotton1dead");
        deadStages.add("Cotton2dead");
        deadStages.add("Cotton3dead");
        deadStages.add("Lemon1dead");
        deadStages.add("Lemon2dead");
        deadStages.add("Lemon3dead");
        deadStages.add("Lettuce1dead");
        deadStages.add("Lettuce2dead");
        deadStages.add("Lettuce3dead");
        deadStages.add("Mango1dead");
        deadStages.add("Mango2dead");
        deadStages.add("Mango3dead");
        deadStages.add("Pear1dead");
        deadStages.add("Pear2dead");
        deadStages.add("Pear3dead");
        deadStages.add("Strawberry1dead");
        deadStages.add("Strawberry2dead");
        deadStages.add("Strawberry3dead");
        deadStages.add("Sugarcane1dead");
        deadStages.add("Sugarcane2dead");
        deadStages.add("Sugarcane3dead");
        int cH;
        if (tile.getTileEntity() instanceof Crop) {
            Crop crop = (Crop) tile.getTileEntity();
            c = crop.getName();
            cH = crop.getQuantity();
            if(!deadStages.contains(crop.getTileType())) {
                HashMap<String, String> attribs = new HashMap<String, String>();
                attribs.put("name", c);
                world.getStorageManager().addItem(new SimpleResource(crop.getName(), attribs));
                Platform.runLater(() -> { 
                    output.appendText("Harvested " + cH + " " + c 
                            + "s" + System.getProperty("line.separator"));
                });
            }
        }
        tile.setTileEntity(null);
        tile.setTileType(TileRegister.getInstance().getTileType("dirt"));
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        /*
         * TODO: Fix this so that it ensures that it does not "harvest trees"
         * (pull them out) instead of just picking them.
         */
		//Leveler.getInstance().addExperience(tile.getTileEntity().getTileType(), 50);
        boolean validity = tile.getTileEntity() instanceof Crop;
        if(!validity && output != null) {
            Platform.runLater(() -> { 
                output.appendText("Can only harvest a crop" + 
                        System.getProperty("line.separator"));
            });
        }
        return validity;
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
