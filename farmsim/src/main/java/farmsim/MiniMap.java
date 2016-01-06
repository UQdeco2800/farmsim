package farmsim;

import farmsim.entities.WorldEntity;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.animals.Animal;
import farmsim.entities.animals.FarmAnimalManager;
import farmsim.entities.tileentities.TileEntity;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Tickable;
import farmsim.world.World;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * MiniMap renders the {@link World} and all {@link WorldEntity} instances.
 *
 * @author lightandlight
 */
public class MiniMap extends Canvas implements Tickable {
    private static final int MAX_MINIMAP_TILE_SIZE = 8;
    private static final int MIN_MINIMAP_TILE_SIZE = 1;
    private static final int MAX_MINIMAP_SIZE = 400;
    private int currentMinimapTileSize;

    private World world;
    private Viewport viewport;

    private boolean visible = true;
    private boolean agentsVisible = true;
    private boolean entitiesVisible = true;

    /**
     * Creates a new MiniMap.
     *
     * @param viewport
     *            The {@link Viewport} in use
     */
    public MiniMap(World world, Viewport viewport) {
        super();

        this.world = world;
        this.viewport = viewport;
        this.currentMinimapTileSize = MAX_MINIMAP_TILE_SIZE;

        this.setWidth(world.getWidth() * currentMinimapTileSize);
        this.setHeight(world.getHeight() * currentMinimapTileSize);

        world.widthProperty().addListener((observable, oldValue, newValue) ->
                        this.setWidth(newValue.intValue() * currentMinimapTileSize)
        );

        world.heightProperty().addListener((observable, oldValue, newValue) ->
                        this.setHeight(newValue.intValue() * currentMinimapTileSize)
        );
    }

    @Override
    public void tick() {
            if ((getWidth() > MAX_MINIMAP_SIZE || getHeight() > MAX_MINIMAP_SIZE)
                    && currentMinimapTileSize > MIN_MINIMAP_TILE_SIZE) {
                currentMinimapTileSize--;
                setWidth(world.getWidth() * currentMinimapTileSize);
                setHeight(world.getHeight() * currentMinimapTileSize);
            }

            if (visible) {
                Platform.runLater(() -> {
                    getGraphicsContext2D().clearRect(
                            0, 0,
                            getWidth(),
                            getWidth()
                    );

                    renderMap();

                    getGraphicsContext2D().setStroke(Color.BLACK);
                    getGraphicsContext2D().setLineWidth(1.0);
                    getGraphicsContext2D().strokeRect(
                            0, 0,
                            getWidth(),
                            getHeight());
                });
            }
    }

    /**
     * Toggle whether the entire minimap is visible
     */
    public void toggleVisibility() {
        visible = !visible;
        this.setVisible(visible);
    }

    /**
     * Toggle whether agents will be drawn on the minimap
     */
    public void toggleAgentVisibility() {
        agentsVisible = !agentsVisible;
    }

    /**
     * Get the current size of tile representations on the minimap
     * 
     * @return integer between MIN_MINIMAP_TILE_SIZE and MAX_MINIMAP_TILE_SIZE
     */
    public int getTileSize() {
        return currentMinimapTileSize;
    }

    /**
     * Renders the world to the canvas.
     */
    private void renderMap() {
        renderTiles();

        if (agentsVisible) {
            renderAgents();
        }

        renderAnimals();

        drawViewport();
    }

    private void renderTiles() {
        TileRegister tileRegister = TileRegister.getInstance();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Tile tile = world.getTile(x, y);
                TileEntity tileEntity = tile.getTileEntity();

                Color pixelColor;
                if (tileEntity != null && entitiesVisible) {
                    pixelColor = tileRegister.getTileColour(tileEntity
                                    .getTileType());
                } else {
                    pixelColor = tileRegister.getTileColour(tileRegister
                                    .getTileName(tile.getTileType()));
                }

                getGraphicsContext2D().setFill(pixelColor);
                getGraphicsContext2D().fillRect(x * currentMinimapTileSize, y
                                * currentMinimapTileSize,
                        currentMinimapTileSize, currentMinimapTileSize);
            }
        }
    }

    /**
     * Draw agents to the minimap
     */
    private void renderAgents() {
        TileRegister tileRegister = TileRegister.getInstance();
        for (Agent agent : AgentManager.getInstance().getAgents()) {
            Color pixelColor;
            String role = agent.getCurrentRoleType().displayName();

            if (role.equals("Egg handler") || role.equals("Butcher") || role.equals("Shearer") || role.equals("Milker")) {
                pixelColor = tileRegister.getTileColour("animalhandler"
                                + agent.getGender());
            } else {
                pixelColor = tileRegister.getTileColour(agent.getCurrentRoleType()
                                .displayName().toLowerCase()
                                + agent.getGender()); // Doesn't handle disease
                                                      // yet
            }

            getGraphicsContext2D().setFill(pixelColor);

            getGraphicsContext2D().fillRect(agent.getLocation().getX()
                            * currentMinimapTileSize, agent.getLocation()
                            .getY() * currentMinimapTileSize,
                    currentMinimapTileSize, currentMinimapTileSize);
        }
    }
    
    private void renderAnimals() {
        TileRegister tileRegister = TileRegister.getInstance();
        for (Animal animal : FarmAnimalManager.getInstance().getFarmAnimals()) {
            /* A little bit hacky, if the animals use a different naming scheme you'll notice */
            Color pixelColor = tileRegister.getTileColour(animal.getType() + "DownRight0");
            getGraphicsContext2D().setFill(pixelColor);

            getGraphicsContext2D().fillRect(animal.getLocation().getX()
                            * currentMinimapTileSize, animal.getLocation()
                            .getY() * currentMinimapTileSize,
                    currentMinimapTileSize, currentMinimapTileSize);
        }
    }

    /**
     * Draw a rectangle indicating where the user is currently looking on the
     * main screen
     */
    private void drawViewport() {
        getGraphicsContext2D().setStroke(Color.YELLOW);
        getGraphicsContext2D().setLineWidth(2.0);
        getGraphicsContext2D().strokeRect(viewport.getX() * currentMinimapTileSize,
                viewport.getY() * currentMinimapTileSize,
                viewport.getWidthTiles() * currentMinimapTileSize,
                viewport.getHeightTiles() * currentMinimapTileSize);
    }
}
