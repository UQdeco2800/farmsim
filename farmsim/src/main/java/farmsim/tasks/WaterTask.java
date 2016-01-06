package farmsim.tasks;

import farmsim.entities.tools.ToolType;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.scene.control.TextArea;

public class WaterTask extends AbstractTask {

    private static final int BASE_DURATION = 1000;
    private static final String NAME = "Water";
    private static final String ID = "water";

    private TextArea output;
    
    private static final ToolType BONUS_TOOL = ToolType.WATERING_CAN;

    public WaterTask(Point point, World world, TextArea output) {
        super(point, BASE_DURATION, world, NAME, ID);
        this.output = output;
        setBonusTool(BONUS_TOOL);
    }
    
    public WaterTask copy() {
    	return new WaterTask(location, world, output);
    }

    @Override
    public void preTask() {
        // Do nothing
    }

    @Override
    public void postTask() {
        WorldManager.getInstance().getWorld().getTile(location)
                .increaseWaterLevel();
        output.appendText("Watered a field" + 
                System.getProperty("line.separator"));
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }
        /*
         * TODO: will have to change this so buildings can't be watered
         */
        return true;
    }
}
