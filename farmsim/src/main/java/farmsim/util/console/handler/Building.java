package farmsim.util.console.handler;

import farmsim.buildings.AbstractBuilding;
import farmsim.buildings.BuildingPlacer;
import farmsim.buildings.BuildingPlacerEventHandler;
import farmsim.buildings.StaffHouse;

import farmsim.util.console.Console;
import farmsim.world.WorldManager;

/**
 * Building Command Handler.
 */
public class Building extends BaseHandler implements BaseHandlerInterface {

    /**
     * Handler for all building console commands.
     */
    public Building() {
        super();
        this.addCommands();
        this.setName("Building");
    }

    /**
     * Adds commands to the handler.
     */
    private void addCommands() {
        addSingleCommand("buildings", "I will list the in game buildings");
    }

    /**
     * Handles the user input for the command Handler.
     * 
     * @param parameters the incoming command to be processed.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "buildings":
                    if (parameters.length >= 2
                            && "place".equals(parameters[1])) {
                        place(parameters);
                    } else {
                        list();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Lists all buildings into console.
     */
    private void list() {
        Console.getInstance().println(WorldManager.getInstance().getWorld()
                .getBuildings().toString());
    }

    /**
     * Places a staff house into the world.
     */
    private void place(String[] parameters) {
        StaffHouse building =
                new StaffHouse(WorldManager.getInstance().getWorld());

        final double pollution = tryParsePollution(parameters);

        BuildingPlacerEventHandler handler = new BuildingPlacerEventHandler() {
            @Override
            public void onPlace(AbstractBuilding building) {
                Console.getInstance().println("Building ("
                        + building.toString() + ") placed successfully.");
                if (pollution > 0.0) {
                    building.setPollutionSource(pollution);
                    Console.getInstance().println("Set pollution rate: "
                            + pollution);

                }
            }

            @Override
            public void onCancel(AbstractBuilding building) {
                Console.getInstance().println("Building ("
                        + building.toString() + ") placement cancelled.");
            }
        };
        WorldManager.getInstance().getWorld().getBuildingPlacer()
                .startPlacingBuilding(building, handler);
    }

    private double tryParsePollution(String[] parameters) {
        if (parameters.length >= 3) {
            try {
                return Float.parseFloat(parameters[2]);
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }
}
