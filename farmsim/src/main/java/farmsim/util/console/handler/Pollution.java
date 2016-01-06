package farmsim.util.console.handler;

import farmsim.GameManager;
import farmsim.pollution.PollutionManager;
import farmsim.tasks.ScrubTask;
import farmsim.tasks.TaskManager;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.List;

/**
 * Event and Modifier Command Handler.
 */
public class Pollution extends BaseHandler implements BaseHandlerInterface {
    /**
     * Handler for all building console commands.
     */
    public Pollution() {
        super();
        this.addCommands();
        this.setName("POLLUTION");
    }

    private void addCommands() {
        addSingleCommand("pollution",
                "put [x] [y] [rate]- Place a pollution source\n"
        				+ "drain [x] [y]- Place a pollution drain\n"
                        + "scrub - Scrubs the selected area");
    }

    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "pollution":
                    pollution(parameters);
                    break;
                default:
                    break;
            }
        }
    }

    private void pollution(String[] commandParameters) {
        switch (commandParameters[1]) {
            case "put":
                pollutionPut(commandParameters);
                break;
            case "drain":
            	pollutionDrain(commandParameters);
            	break;
            case "scrub":
                pollutionScrub(commandParameters);
                break;
            default:
                break;
        }
    }

    private void pollutionPut(String[] commandParameters) {
        PollutionManager pollutionManager = PollutionManager.getInstance();
        pollutionManager.placePollutionSource(
                Integer.parseInt(commandParameters[2]),
                Integer.parseInt(commandParameters[3]),
                Double.parseDouble(commandParameters[4]));
    }
    
    private void pollutionDrain(String[] commandParameters) {
        PollutionManager pollutionManager = PollutionManager.getInstance();
        pollutionManager.placePollutionDrain(
                Integer.parseInt(commandParameters[2]),
                Integer.parseInt(commandParameters[3]));
    }

    private void pollutionScrub(String[] commandparameters) {
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = WorldManager.getInstance().getWorld();

        for (Point point : selection) {
            TaskManager.getInstance().addTask(new ScrubTask(point, world));
        }
    }
}

