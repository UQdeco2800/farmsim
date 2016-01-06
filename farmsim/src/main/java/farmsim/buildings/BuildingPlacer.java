package farmsim.buildings;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.entities.tileentities.TileEntity;
import farmsim.render.Drawable;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Clickable;
import farmsim.util.Point;
import farmsim.world.World;

import javafx.scene.ImageCursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BuildingPlacer implements Clickable, Drawable {
    /**
     * The building being placed by the user.
     */
    private AbstractBuilding buildingToPlace;

    /**
     * Event handler for the building being placed.
     */
    private BuildingPlacerEventHandler eventHandler;

    private World worldToBulldoze;
    private ImageCursor bulldozeCursor;
    private boolean hasSetCursor;

    public BuildingPlacer() {
        hasSetCursor = false;
        GameManager.getInstance().registerClickable(this);
    }

    /**
     * Starts the placement of a building.
     *
     * @param building The building to place.
     */
    public void startPlacingBuilding(AbstractBuilding building) {
        startPlacingBuilding(building, null);
    }

    /**
     * Starts the placement of a building.
     *
     * @param building The building to place.
     * @param handler An event handler for place/cancel events.
     */
    public void startPlacingBuilding(AbstractBuilding building,
                                     BuildingPlacerEventHandler handler) {
        this.buildingToPlace = building;
        this.eventHandler = handler;
    }

    /**
     * Starts bulldozing mode in the given world.
     * If a building is clicked it will be removed.
     */
    public void startBulldozing(World world) {
        if (bulldozeCursor == null) {
            bulldozeCursor = new ImageCursor(
                    new Image("/buildings/bulldozeCursor.png"));
        }
        stopPlacingBuilding();
        worldToBulldoze = world;
    }

    /**
     * Stop bulldozing mode.
     */
    public void stopBulldozing() {
        worldToBulldoze = null;
    }

    /**
     * @return true iff the player is in bulldozing mode.
     */
    public boolean isBulldozing() {
        return worldToBulldoze != null;
    }

    /**
     * Cancels the building placement process.
     */
    public void stopPlacingBuilding() {
        if (eventHandler != null) {
            eventHandler.onCancel(buildingToPlace);
        }
        buildingToPlace = null;
        eventHandler = null;
    }

    /**
     * @return True iff the user is in the process of placing a building.
     */
    public boolean isPlacingBuilding() {
        return buildingToPlace != null;
    }

    /**
     * @return The building being placed by the user.
     */
    public AbstractBuilding getBuilding() {
        return buildingToPlace;
    }

    @Override
    public boolean containsPoint(Point point) {
        return true;
    }

    /**
     * Attempt to place the building.
     */
    @Override
    public void click(Point location) {
        if (isPlacingBuilding()) {
            buildingToPlace.setLocation(
                    new Point((int) location.getX(), (int) location.getY()));
            if (buildingToPlace.addToWorld()) {
                if (eventHandler != null) {
                    eventHandler.onPlace(buildingToPlace);
                }
                buildingToPlace = null;
                eventHandler = null;
            }
        } else if (isBulldozing()) {
            Tile tile = worldToBulldoze.getTile((int)location.getX(),
                    (int)location.getY());
            TileEntity tileEntity = tile.getTileEntity();
            if (tileEntity != null
                    && tileEntity instanceof BuildingTileEntity) {
                AbstractBuilding building =
                        ((BuildingTileEntity) tileEntity).getBuilding();
                building.removeFromWorld();
            }
            worldToBulldoze = null;
        }
    }

    @Override
    public void draw(Viewport viewport, GraphicsContext ctx) {
        if (isPlacingBuilding()) {
            Point mouseLocation = GameManager.getInstance().getMouseLocation();
            mouseLocation = new Point((int) mouseLocation.getX(),
                    (int) mouseLocation.getY());
            buildingToPlace.setLocation(mouseLocation);
            buildingToPlace.draw(viewport, ctx);

            if (!buildingToPlace.isLocationValid()) {
                Image sprite = buildingToPlace.getSprite();
                ctx.setFill(new Color(1, 0, 0, 0.5));
                ctx.fillRect(
                        (mouseLocation.getX() - viewport.getX())
                                * TileRegister.TILE_SIZE,
                        (mouseLocation.getY() - viewport.getY())
                                * TileRegister.TILE_SIZE
                                - buildingToPlace.getSpriteOffset(),
                        sprite.getWidth(),
                        sprite.getHeight()
                );
            }
        } else if (isBulldozing() && !hasSetCursor) {
            ctx.getCanvas().getScene().setCursor(bulldozeCursor);
            hasSetCursor = true;
        } else if (!isBulldozing() && hasSetCursor) {
            ctx.getCanvas().getScene().setCursor(null);
            hasSetCursor = false;
        }
    }

    @Override
    public double getWorldX() {
        return (int) GameManager.getInstance().getMouseLocation().getX();
    }

    @Override
    public double getWorldY() {
        return (int) GameManager.getInstance().getMouseLocation().getY();
    }
}
