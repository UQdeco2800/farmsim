package farmsim.buildings;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.Water;
import farmsim.pollution.PollutionManager;
import farmsim.render.Drawable;
import farmsim.tasks.BuildTask;
import farmsim.tasks.TaskManager;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.ui.AnimalProcessingPopUp2;
import farmsim.ui.PopUpWindowManager;
import farmsim.util.Clickable;
import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.world.World;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * Represents a building in the world.
 *
 * @author llste
 */
public abstract class AbstractBuilding implements Clickable, Drawable,
        Tickable {
    /**
     * Time between clicks to register a double click.
     */
    public static final int DOUBLE_CLICK_TIME = 300;

    /**
     * The default value for the building's maximum health.
     */
    public static final int DEFAULT_MAX_HEALTH = 1000;

    /**
     * The default value for the time it takes to build the building.
     */
    public static final int DEFAULT_BUILD_TIME = 5 * 1000;

    /**
     * The last time the building was clicked.
     */
    private long lastClickTime;

    /**
     * The world the building is in.
     */
    private World world;

    /**
     * The location of the building. i.e. it's top left corner.
     */
    private Point location;

    /**
     * The number of tiles the building is wide.
     */
    private int width;

    /**
     * The number of tiles the building is high.
     */
    private int height;

    /**
     * The building's sprite.
     */
    private Image sprite;

    /**
     * The sprite's vertical offset.
     */
    private int spriteOffset;

    /**
     * The building's max health.
     */
    private long maxHealth;

    /**
     * The building's current health.
     */
    protected long currentHealth;

    /**
     * The rate the building is emitting pollution.
     */
    private double pollutionRate;

    /**
     * Whether or not the building is built.
     */
    private boolean isBuilt;

    /**
     * How long the building takes to build.
     */
    private int buildTime;

    /**
     * The agent task for building this building.
     */
    private BuildTask buildTask;
    
    /**
     * The list of original tiles the building was covering
     */
    private ArrayList<Tile> originalTiles = new ArrayList<Tile>();

    /**
     * Initializes the building's size, health and sprite.
     * Health always starts at max health.
     *
     * @param world The world the building is placed in.
     * @param width The number of tiles the building is wide.
     * @param height The number of tiles the building is high.
     * @param imageName The location of the building's sprite.
     */
    public AbstractBuilding(World world, int width, int height,
            String imageName) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.maxHealth = DEFAULT_MAX_HEALTH;
        this.currentHealth = maxHealth;
        this.pollutionRate = 0;
        this.isBuilt = false;
        this.buildTime = DEFAULT_BUILD_TIME;
        this.setSprite(imageName);
    }

    /**
     * @return The world the building is in.
     */
    public World getWorld() {
        return world;
    }

    /**
     * @return The location of the building's top left corner.
     * @require The building's location has been set.
     */
    public Point getLocation() {
        return new Point(location);
    }

    /**
     * @ensure The building's location is set if the building has not been added
     *         to the world.
     */
    public void setLocation(Point location) {
        if (!world.containsBuilding(this)) {
            this.location = new Point(location);
        }
    }

    /**
     * @return True iff the building is in a valid location (i.e. inside the
     *         world and not on top of any tile entities).
     */
    public boolean isLocationValid() {
        for (Tile tile : getTiles()) {
            if (tile == null || tile.getTileEntity() != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the building to the world if the building's location is valid.
     *
     * @return True if the building is in a valid location and was added to the
     *         world, otherwise false.
     * @require The building's location has been set.
     * @ensure The building is added to the world if the building is in a valid
     *         location.
     */
    public boolean addToWorld() {
        if (isLocationValid() && !world.containsBuilding(this)) {
            this.handlePlace();
            // add the building tile entity
            for (Tile tile : getTiles()) {
                Tile original = new Tile((int)tile.getWorldX(), 
                        (int)tile.getWorldY(), tile.getTileType());
                original.setTileEntity(tile.getTileEntity());
                originalTiles.add(original);
                BuildingTileEntity entity = new BuildingTileEntity(this, tile);
                tile.setProperty(TileProperty.IS_BUILDING, true);
                tile.setTileEntity(entity);
            }
            world.addBuilding(this);
            if (buildTime > 0) {
                buildTask = new BuildTask(this);
                TaskManager.getInstance().addTask(buildTask);
            } else {
                completeBuilding();
            }
            return true;
        }
        return false;
    }

    /**
     * Removes the building from the world and sets the tiles underneath the
     * building to dirt.
     */
    public void removeFromWorld() {
        if (world.containsBuilding(this)) {
            if (buildTask != null) {
                TaskManager.getInstance().removeTask(buildTask);
                buildTask = null;
            }
            this.handleRemove();
            // remove the building tile entity
            for (int i = 0; i < getTiles().size(); i++) {
                if (getTiles().get(i) == null) {
                    continue;
                }
                getTiles().get(i).removeProperty(TileProperty.IS_BUILDING);
                try {
                    getTiles().get(i).setTileEntity(originalTiles.get(i).getTileEntity());
                } catch (IndexOutOfBoundsException e) {
                    getTiles().get(i).setTileEntity(null);
                }
            }
            world.removeBuilding(this);
        }
    }

    /**
     * @return The tiles that are underneath the building. If tile is outside
     *         the world, null will be added to the list.
     * @require the building's location has been set.
     */
    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();

        int buildingX = (int) location.getX();
        int buildingY = (int) location.getY();
        for (int y = buildingY; y < buildingY + height; y++) {
            for (int x = buildingX; x < buildingX + width; x++) {
                // if x & y are outside the world, add null.
                if (x < 0 || x >= world.getWidth() || y < 0
                        || y >= world.getHeight()) {
                    tiles.add(null);
                } else {
                    tiles.add(world.getTile(x, y));
                }
            }
        }

        return tiles;
    }

    /**
     * @return The number of tiles the building is wide.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The number of tiles the building is high.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The building's sprite.
     */
    public Image getSprite() {
        return sprite;
    }

    /**
     * @return The sprite's vertical offset.
     */
    public int getSpriteOffset() {
        return spriteOffset;
    }

    /**
     * Sets the building's sprite.
     */
    public void setSprite(String imageName) {
        sprite = new Image(getClass().getResource(imageName).toString());
        spriteOffset =
                (int) sprite.getHeight() - (height * TileRegister.TILE_SIZE);
    }

    /**
     * @return The buildings current Health.
     */
    public long getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the building's current health.
     */
    public void setHealth(long health) {
        if (onChangeHealth(health)) {
            currentHealth = health;
        }
    }

    /**
     * @return The buildings current Health.
     */
    public long getMaxHealth() {
        return maxHealth;
    }

    /**
     * Set the building's maximum health.
     */
    public void setMaxHealth(long maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Damages the building's health.
     *
     * @return The building's current health.
     * @require damage >= 0
     * @ensure currentHealth = \old(currentHealth) - damage
     */
    public long damageHealth(long damage) {
        setHealth(currentHealth - damage);
        return currentHealth;
    }

    /**
     * Heals the building's health.
     *
     * @return The building's current health.
     * @require heal >= 0
     * @ensure currentHealth = \old(currentHealth) + heal
     */
    public long healHealth(long heal) {
        setHealth(currentHealth + heal);
        return currentHealth;
    }

    /**
     * Fully restore the building's health.
     */
    public void fullyRestoreHealth() {
        setHealth(maxHealth);
    }

    /**
     * Called before the building's health is changed.
     *
     * @return false to stop the building's health from changing, otherwise
     *         return true.
     */
    public boolean onChangeHealth(long newHealth) {
        return true;
    }

    /**
     * Sets the rate at which the building is emitting pollution.
     *
     * @param rate the pollution rate between 0 and 1.
     * @require the building has been added to the world.
     */
    public void setPollutionSource(double rate) {
        PollutionManager pollutionManager = PollutionManager.getInstance();
        for (Tile tile : getTiles()) {
            if (tile == null) {
                continue;
            }
            if (!tile.hasProperty(TileProperty.IS_POLLUTION_SOURCE)) {
                pollutionManager.placePollutionSource(tile, rate);
            } else {
                pollutionManager.changeSourceRate(tile, rate);
            }
        }
        pollutionRate = rate;
    }

    /**
     * Remove this building as a pollution source.
     */
    public void removePollutionSource() {
        PollutionManager pollutionManager = PollutionManager.getInstance();
        for (Tile tile : getTiles()) {
            if (tile != null) {
                pollutionManager.removePollutionSource(tile);
            }
        }
        pollutionRate = 0;
    }

    /**
     * @return The rate the building is emitting pollution.
     */
    public double getPollutionRate() {
        return pollutionRate;
    }

    /**
     * @return true iff the building is built.
     */
    public boolean isBuilt() {
        return isBuilt;
    }

    /**
     * Handle the completion of the building's construction.
     */
    public void completeBuilding() {
        isBuilt = true;
        buildTask = null;
        GameManager.getInstance().registerClickable(this);
        this.onBuild();
    }

    /**
     * Set how long the building takes to build.
     */
    public void setBuildTime(int buildTime) {
        this.buildTime = buildTime;
    }

    /**
     * Get how long the building takes to build.
     */
    public int getBuildTime() {
        return buildTime;
    }

    @Override
    public void draw(Viewport viewport, GraphicsContext ctx) {
        ctx.drawImage(
                sprite,
                (getWorldX() - viewport.getX()) * TileRegister.TILE_SIZE,
                (getWorldY() - viewport.getY()) * TileRegister.TILE_SIZE
                        - getSpriteOffset()
        );
    }

    @Override
    public double getWorldX() {
        return location.getX();
    }

    @Override
    public double getWorldY() {
        return location.getY();
    }

    /**
     * Handles the onPlace event.
     */
    private void handlePlace() {
        if (isBuilt()) {
            GameManager.getInstance().registerClickable(this);
        }
        onPlace();
    }

    /**
     * Called when the building is placed in the world.
     */
    public void onPlace() {
    }

    /**
     * Called when the building is built.
     */
    public void onBuild() {

    }

    /**
     * Handles the remove onRemove event.
     */
    private void handleRemove() {
        GameManager.getInstance().unregisterClickable(this);
        onRemove();
    }

    /**
     * Called when the building is removed from the world.
     */
    public void onRemove() {
    }

    /**
     * @return true iff the specified point is inside the boundary of this
     *         building.
     */
    @Override
    public boolean containsPoint(Point point) {
        return point.getX() >= this.location.getX()
                && point.getX() <= this.location.getX() + this.getWidth()
                && point.getY() >= this.location.getY()
                && point.getY() <= this.location.getY() + this.getHeight();
    }

    @Override
    public void click(Point location) {
        this.onClick(location);
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastClickTime) <= DOUBLE_CLICK_TIME) {
            this.onDoubleClick(location);
        } else {
            lastClickTime = currentTime;
        }
    }

    public void onClick(Point location) {
    }

    public void onDoubleClick(Point location) {
    }

    @Override
    public void tick() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": "
                + (location != null ? location.toString() : "null");
    }


}
