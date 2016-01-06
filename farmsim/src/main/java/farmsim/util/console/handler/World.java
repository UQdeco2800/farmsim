package farmsim.util.console.handler;

import farmsim.util.console.Console;
import farmsim.world.WorldManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * World Command Handler.
 */
public class World extends BaseHandler implements BaseHandlerInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(World.class);

    /**
     * Handler for world console commands.
     */
    public World() {
        super();
        this.addCommands();
        this.setName("World");
    }

    private void addCommands() {
        addSingleCommand("dimensions", "dimensions x y, I will adjust the "
                + "dimensions of the world");
        addSingleCommand("size", "Gets the size of the world width, height");
    }

    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "dimensions":
                    dimensions(parameters);
                    break;
                case "size":
                    size();
                    break;
                default:
                    break;
            }
        }
    }

    private void dimensions(String[] parameters) {
        if (parameters.length == 3) {
            farmsim.world.World world = WorldManager.getInstance().getWorld();
            int dimensionX = world.getWidth();
            int dimensionY = world.getHeight();
            try {
                dimensionX = Integer.parseInt(parameters[1]);
                dimensionY = Integer.parseInt(parameters[2]);
            } catch (NumberFormatException e) {
                LOGGER.error("Unable to set world size");
            }
            world.setDimensions(dimensionX, dimensionY);
        }
    }

    private void size() {
        farmsim.world.World world = WorldManager.getInstance().getWorld();
        Console.getInstance().println(
                "World size: " + world.getWidth() + ", " + world.getHeight());
    }
}
