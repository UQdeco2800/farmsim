package farmsim.util.console.handler;

import farmsim.entities.animals.Cow;
import farmsim.entities.animals.FarmAnimal;
import farmsim.entities.animals.FarmAnimalManager;
import farmsim.util.console.Console;
import farmsim.world.WorldManager;

import java.util.List;

/**
 * Peons Command Handler.
 */
public class FarmAnimals extends BaseHandler implements BaseHandlerInterface {

    /**
     * Handler for all building console commands.
     */
    public FarmAnimals() {
        super();
        this.addCommands();
        this.setName("Farm Animals");
    }

    private void addCommands() {
        addSingleCommand("farmAnimal",
                "list - lists all Farm Animals in the world\n"
                        + "new [type] [x] [y] - creates a new Farm Animal at x,y");
    }

    /**
     * Handles the incoming command parameters.
     * 
     * @param parameters the incoming command to be processed.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "farmAnimal":
                    farmAnimal(parameters);
                    break;
                default:
                    break;
            }
        }
    }

    private void farmAnimal(String[] commandParameters) {
        switch (commandParameters[1]) {
            case "new":
                newFarmAnimal(commandParameters);
                break;
            case "list":
                list();
                break;
            default:
                break;
        }
    }

    private void list() {
        List<FarmAnimal> farmAnimals =
                FarmAnimalManager.getInstance().getFarmAnimals();
        for (FarmAnimal farmAnimal : farmAnimals) {
            Console.getInstance()
                    .println(farmAnimal.getType() + " "
                            + farmAnimal.getLocation() + " Food Level: "
                            + farmAnimal.getFoodLevel() + " Thirst Level: "
                            + farmAnimal.getThirstLevel() + " Rest Level: "
                            + farmAnimal.getRestLevel() + " Health: "
                            + farmAnimal.getHealth());
        }
    }

    private void newFarmAnimal(String[] commandParameters) {
        switch (commandParameters[2]) {
            case "cow":
                Cow newCow = new Cow(WorldManager.getInstance().getWorld(),
                        Integer.parseInt(commandParameters[3]),
                        Integer.parseInt(commandParameters[4]), 0.1, 0, 'f', 1);
                FarmAnimalManager.getInstance().addFarmAnimal(newCow);

                Cow newCow2 = new Cow(WorldManager.getInstance().getWorld(),
                        Integer.parseInt(commandParameters[3]) + 5,
                        Integer.parseInt(commandParameters[4]), 0.1, 0, 'm', 1);
                newCow2.setReadyToMate(true);
                FarmAnimalManager.getInstance().addFarmAnimal(newCow2);
                break;
            default:
                break;
        }
    }
}
