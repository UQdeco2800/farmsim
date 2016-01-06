package farmsim.world;

import common.client.FarmClient;
import farmsim.TechTree.TechTree;
import farmsim.buildings.AbstractBuilding;
import farmsim.buildings.StaffHouse;
import farmsim.buildings.BuildingPlacer;
import farmsim.entities.fire.FireManager;
import farmsim.entities.predators.PredatorManager;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.entities.tileentities.objects.Rock;
import farmsim.entities.tileentities.objects.Water;
import farmsim.inventory.StorageManager;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.ui.MarketplaceController;
import farmsim.util.Array2D;
import farmsim.util.Leveler;
import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.world.generators.BasicWorldGenerator;
import farmsim.world.modifiers.ModifierManager;
import farmsim.world.weather.Season;
import farmsim.world.weather.SeasonManager;
import farmsim.world.weather.SeasonName;
import farmsim.world.weather.WeatherManager;
import farmsim.world.weather.WeatherQueue;
import farmsim.world.weather.WeatherType;
import farmsim.contracts.*;
import farmsim.money.*;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

/**
 * Models the game's physical environment.
 * 
 * @author Leggy
 *
 */
public class World implements Tickable {
    private Array2D<Tile> tiles;
    private Set<AbstractBuilding> buildings;
    private String name;
    private WorldManager worldManager;
    private DayNight timeManager;
    private PredatorManager predatorManager;
    private ContractGenerator contractGenerator;
    private ContractHandler activeContracts;
    private ContractHandler availableContracts;
    private Money moneyHandler;
    private FarmClient farmClient;
    private BuildingPlacer buildingPlacer;
    
    private static TileRegister tileRegister = TileRegister.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(World.class);

    private ReadOnlyIntegerWrapper width;
    private ReadOnlyIntegerWrapper height;

    private WeatherManager weatherManager;
    private SeasonManager seasonManager;
    private ModifierManager modifierManager;
    private long seed;

    public static final int BASE_USABLE_SIZE = 20;
    public static final int MIN_ELEVATION = -10;
    public static final int MAX_ELEVATION = 9;
    public static final int LAKE_ELEVATION_CUTOFF = -6;
    public static final double MIN_MOISTURE = 0.0;
    public static final double MAX_MOISTURE = 20.0;
    private FireManager fireManager;
    private StorageManager storageManager;
    private Leveler leveler;
    private TechTree techTree;

    private boolean initialised = false;

    /**
     * Instantiates a World object with the specified parameters.
     *
     * @param name
     *            The name of the World
     * @param width
     *            The width of the World
     * @param height
     *            The height of the World
     * @param baseTileType
     *            The initial tile type to set every tile to
     */
    public World(String name, int width, int height,
                    int baseTileType, long seed) {

        tiles = new Array2D<>(width, height);

        this.seed = seed;
        this.height = new ReadOnlyIntegerWrapper(height);
        this.width = new ReadOnlyIntegerWrapper(width);
        initialise();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                tiles.set(x, y, new Tile(x, y, baseTileType));
            }
        }

        buildings = new HashSet<>();

        this.name = name;
        this.modifierManager = new ModifierManager();
        this.seasonManager = new SeasonManager();
        this.weatherManager = new WeatherManager();
        this.fireManager = new FireManager(this);
        this.leveler = new Leveler();
        this.timeManager = new DayNight();
        this.contractGenerator = new ContractGenerator();
        this.activeContracts = new ContractHandler();
        this.availableContracts = new ContractHandler();
        this.storageManager = new StorageManager();
        this.predatorManager = new PredatorManager();
        this.buildingPlacer = new BuildingPlacer();
        this.farmClient = new FarmClient(
                MarketplaceController.HOST,
                MarketplaceController.PORT);
        this.moneyHandler = new Money(this.farmClient);
        this.techTree = new TechTree(leveler);
    }

    /**
     * Instantiates a World object with the specified parameters, with the tile
     * type defaulting to Tile.EMPTY. and a default seed
     *
     * @param name
     *            The name of the World
     * @param width
     *            The width of the World
     * @param height
     *            The height of the World
     */
    public World(String name, int width, int height) {
        this(name, width, height, tileRegister.getTileType("empty"), 1);
    }

    /**
     * @return The name of the world.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The seed of the world.
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Retrieves a Tile at the specified coordinates.
     *
     * @param x
     *            The x-coordinate of the Tile to retrieve
     * @param y
     *            The y-coordinate of the Tile to retrieve
     * @return The Tile at the specified coordinates
     */
    public Tile getTile(int x, int y) {
        return tiles.get(x, y);
    }

    /**
     * Retrieves a Tile at the specified points
     * 
     * @param point
     *            the point of the Tile
     * @return the Tile at the specified coordinates
     */
    public Tile getTile(Point point) {
        return tiles.get((int) point.getX(), (int) point.getY());
    }

    /**
     * Retrieves a list of buildings currently in the world.
     *
     * @return A list of buildings.
     */
    public Set<AbstractBuilding> getBuildings() {
        return new HashSet<>(buildings);
    }

    /**
     * @return The width of the World
     */
    public int getWidth() {
        return width.get();
    }

    /**
     * @return The height of the World
     */
    public int getHeight() {
        return height.get();
    }

    /**
     * Gets the width of the world
     * @return number of tiles of width.
     */
    public ReadOnlyIntegerProperty widthProperty() {
        return width.getReadOnlyProperty();
    }

    /**
     * Gets the height of the world
     * @return number of tiles of the height.
     */
    public ReadOnlyIntegerProperty heightProperty() {
        return height.getReadOnlyProperty();
    }

    /**
     * Ticks the entities in the world (fire, buildings, and so on).
     */
    @Override
    public void tick() {
        if (!initialised) {
            initialised = true;
            seasonManager.seasonStart();
            initWeather();
        }
        for (AbstractBuilding building : buildings) {
            building.tick();
        }
        modifierManager.tick();
        fireManager.tick();
    }

    /**
     * Initialise all tiles to a default value. Required to run before other
     * methods
     */
    public void initialise() {
        for (int y = 0; y < tiles.getHeight(); y++) {
            for (int x = 0; x < tiles.getWidth(); x++) {
                tiles.initialise(x, y, new Tile(x, y, 0));
            }
        }
    }
    
    /**
     * Initialise all tiles to a default value. Required to run before other
     * methods
     */
    public void initialise(int minX, int minY, int maxX, int maxY) {
        for (int y = minY; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                tiles.initialise(x, y, new Tile(x, y, 0));
            }
        }
    }

    /**
     * Set the tile at position x, y to have tileType
     * 
     * @param x
     *            x coordinate of the tile
     * @param y
     *            y coordinate of the tile
     * @param tileType
     *            the tileType to set
     */
    public void setTile(int x, int y, int tileType) {
        getTile(x, y).setTileType(tileType);
    }

    /**
     * Set the tile at point to have tileType
     * 
     * @param point
     *            the point to set
     * @param tileType
     *            the tileType to set
     */
    public void setTile(Point point, int tileType) {
        setTile((int) point.getX(), (int) point.getY(), tileType);
    }

    /**
     * Set the tile at the coordinates given.
     * @param x x position.
     * @param y y position.
     * @param tileEntity to set to the tile at the location.
     */
    public void setTileEntity(int x, int y, TileEntity tileEntity) {
        getTile(x, y).setTileEntity(tileEntity);
    }

    /**
     * Set the dimensions of the world to be width by height
     * 
     * @param width
     *            the new width of the world
     * @param height
     *            the new height of the world
     */
    public void setDimensions(int width, int height) {
        int oldWidth = getWidth();
        int oldHeight = getHeight();

        this.width.set(width);
        this.height.set(height);

        /* Adjust the size of the underlying array */
        tiles.setDimensions(width, height);
        BasicWorldGenerator generator = new BasicWorldGenerator();
        /*
         * Set the generator to fill between the end of the old world and the
         * new maximum
         */
        try {
            generator.configureWorld(this);
            generator.setExtensionSize(oldWidth - 1, oldHeight - 1, width, height);
            int[] tiles = {tileRegister.getTileType("grass"),
                            tileRegister.getTileType("grass2"),
                            tileRegister.getTileType("grass3")};
            generator.setBaseTileTypes(tiles);
            generator.setElevation();
            generator.setMoisture();
            generator.addLakes();
            generator.updateMoisture();
            generator.setTileWaterLevel();
            generator.setBiomes();
            generator.addBiomeEntities();
            generator.createRivers();
            this.setPassibility();
            worldManager.setWorld(generator.build());
        } catch (Exception e) {
            LOGGER.error("Extending world", e.getMessage());
        }
    }

    /**
     * Update the world's lakes, cutting them off at the given height
     * 
     * @param height
     *            height below which lakes form
     */
    public void updateLakes(int height) throws Exception {
        if ((height < MIN_ELEVATION) || (height > MAX_ELEVATION)) {
            throw new Exception("height out of range");
        }

        for (int y = 0; y < this.getWidth(); y++) {
            for (int x = 0; x < this.getHeight(); x++) {
                Tile tile = this.getTile(x, y);
                if (!(tile.getTileEntity() instanceof BaseObject)
                        && (int) tile.getProperty(TileProperty.ELEVATION) < height) {
                    tile.setTileEntity(new Water(tile));
                } else if (tile.getTileEntity() instanceof Water
                                && (int) tile.getProperty(TileProperty.ELEVATION) >= height) {
                    // ToDo change this when rivers are implemented so it
                    // doesn't kill them
                    tile.setTileEntity(null);
                }
            }
        }
    }

    /**
     * Set the passable property of all tiles
     */
    public void setPassibility() {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                Tile tile = this.getTile(x, y);

                if (tile.getTileEntity() != null) {
                    TileEntity tileEntity = tile.getTileEntity();

                    if (tileEntity.getClass() == Water.class ||
                        tileEntity.getClass() == Rock.class) {
                        if (tile.getProperty(TileProperty.IS_BUILDING) != null) {
                            if ((boolean) tile.getProperty(TileProperty.IS_BUILDING)) {
                                tile.setProperty(TileProperty.PASSABLE, true);
                                continue;
                            }
                        }
                        tile.setProperty(TileProperty.PASSABLE, false);
                        continue;
                    }
                }

                tile.setProperty(TileProperty.PASSABLE, true);
            }
        }
    }

    /**
     * Adjust the moisture level of each tile by a given amount
     * 
     * @param change
     *            amount of moisture to add
     */
    public void adjustMoisture(int change) throws Exception {
        if ((change < MIN_MOISTURE) || (change > MAX_MOISTURE)) {
            throw new Exception("change out of range");
        }

        for (int y = 0; y < this.getWidth(); y++) {
            for (int x = 0; x < this.getHeight(); x++) {
                Tile tile = this.getTile(x, y);
                double moisture = (double) tile
                                .getProperty(TileProperty.MOISTURE);
                moisture += change;
                if (moisture > MAX_MOISTURE) {
                    moisture = MAX_MOISTURE;
                } else if (moisture < MIN_MOISTURE) {
                    moisture = MIN_MOISTURE;
                }
                tile.setProperty(TileProperty.MOISTURE, moisture);
            }
        }
    }

    /**
     * Adds the given building to the world's building list.
     *
     * @require building.getWorld() == this && The building is in a valid
     *          location.
     */
    public void addBuilding(AbstractBuilding building) {
        buildings.add(building);
    }

    /**
     * Removes the given building from the world's building list.
     */
    public void removeBuilding(AbstractBuilding building) {
        buildings.remove(building);
    }

    /**
     * @return True iff the world's building list contains the given building.
     */
    public boolean containsBuilding(AbstractBuilding building) {
        return buildings.contains(building);
    }

    /* Modifier Controls */
    /**
     * Get the modifier controller to add and remove modifiers.
     */
    public ModifierManager getModifierManager() {
        return modifierManager;
    }

    /* Weather Controls - Abstracts logic away from the world class */
    /**
     * Get the currently active weather type.
     *
     * @return WeatherType of the active weather
     */
    public WeatherType getWeatherType() {
        return weatherManager.getWeather();
    }

    /**
     * Gets the human readable representation of the current weather.
     *
     * @return Name of the active weather
     */
    public String getWeatherName() {
        return weatherManager.getWeatherName();
    }

    /**
     * Gets the components in the weather.
     * @return Components and the levels in the weather.
     */
    public Map<String, Integer> getWeatherComponents() {
        return weatherManager.getWeatherComponents();
    }

    /**
     * Attempts to load the selected weather, if it fails to load the weather a
     * the default weather is loaded.
     * 
     * @param weather
     *            the weather type to load.
     * @return true when weather is applied successfully, false otherwise.
     */
    public Boolean setWeather(WeatherType weather) {
        return weatherManager.switchWeather(weather);
    }

    /**
     * Gets the name of the currently active season.
     * @return String name of the current season (converted from SeasonName)
     */
    public String getSeason() {
        return seasonManager.getSeason();
    }

    /**
     * An observer.
     * 
     * @param observer
     *            observer.
     */
    public void setTileObserver(Observer observer) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                this.getTile(x, y).addObserver(observer);
            }
        }
    }

    /**
     * Register an observer on the season manager.
     * Notifies on change of season.
     * 
     * @param observer a class that implements Observer interface.
     */
    public void setSeasonObserver(Observer observer) {
        seasonManager.addObserver(observer);
    }
    
    /**
     * Register an observer on the weather manager. 
     * Notifies on change of weather.
     * 
     * @param observer a class that implements Observer interface.
     */
    public void setWeatherObserver(Observer observer) {
        weatherManager.addObserver(observer);
    }

    /**
     * Returns the currently active Season object.
     * @return currently active season object
     */
    public Season getSeasonObject() {
        return seasonManager.getSeasonObject();
    }
    
    /**
     * Call to SeasonManager.getIconStyle.
     * Gets the JavaFX icon style string for the current season. 
     * @param season The season to get style for
     * @param large Flag whether to return the 64x64 or 16x16 variant
     * @return the style string.
     */
    public String getSeasonIconStyle(SeasonName season, Boolean large) {
        return seasonManager.getIconStyle(season, large);
    }
    
    /**
     * Call to WeatherManager.getWeatherIconStyle.
     * Gets the style class for the current weather icon.
     * @return the style string.
     */
    public String getWeatherIconStyle(WeatherType weather) {
        return weatherManager.getWeatherIconStyle(weather);
    }
    
    /**
     * Call to WeatherManager.getForecastIconStyle.
     * Gets the style class for weather's forecast icon.
     * @param weather a WeatherType to get style class for.
     */
    public String getForecastIconStyle(WeatherType weather) {
        return weatherManager.getForecastIconStyle(weather);
    }
    
    /**
     * Gets the season's end day. Call to SeasonManager.getSeasonEndDay.
     * @return day the current season started
     */
    public int getSeasonEndDay() {
        return seasonManager.getSeasonEndDay();
    }
    
    /**
     * Build the initial weather queue. Must be called exactly once, AFTER the 
     * world constructor has completed.
     */
    private void initWeather() {
        weatherManager.buildQueue();
        weatherManager.registerHourlyTask();
        seasonManager.registerMonthlyTask();
    }
    
    /**
     * Updates the forecast.
     */
    public void updateForecast() {
        weatherManager.updateForecast();
    }
    
    /**
     * Gets forecast.
     * @return the forecast list from weatherQueue.
     */
    public List<WeatherQueue.Element> getForecast() {
        return weatherManager.getForecast();
    }

    /**
     * Gets the current fire manager.
     * @return the fire manager.
     */
    public FireManager getFireManager() {
        return fireManager;
    }

    /**
     * Gets the leveler.
     * @return the current leveler.
     */
    public Leveler getLeveler(){
        return leveler;
    }

    /**
     * Gets the current time manager.
     * @return the current time manager.
     */
    public DayNight getTimeManager(){
    	return timeManager;
    }

    /**
     * Gets the current weather manager.
     * @return the current weather manager.
     */
    public WeatherManager getWeatherManager() {
        return weatherManager;
    }

    /**
     * Gets the current season manager.
     * @return the current season manager.
     */
    public SeasonManager getSeasonManager() {
        return seasonManager;
    }

    /**
     * Gets the current money handler.
     * @return the current money handler.
     */
    public Money getMoneyHandler() {
    	return moneyHandler;
    }

    /**
     * Gets the current building placer.
     * @return the current building placer.
     */
    public BuildingPlacer getBuildingPlacer() {
        return buildingPlacer;
    }

    /**
     * Activates the bulldozing tool.
     */
    public void startBulldozing() {
        buildingPlacer.startBulldozing(this);
    }

    /**
     * Gets the current contract handler.
     * @return the current contract handler.
     */
    public ContractHandler getContractHandler() {
    	return activeContracts;
    }

    /**
     * Gets the current contract generator.
     * @return the current contract generator.
     */
    public ContractGenerator getContractGenerator() {
    	return contractGenerator;
    }

    /**
     * Gets the current Storage manager.
     * @return the current storage manager.
     */
    public StorageManager getStorageManager() {
    	return storageManager;
    }
    
    /**
     * Gets the predator manager
     * Used to remove the predator manager's dependence on singletons
     * @return The PredatorManager
     */
    public PredatorManager getPredatorManager() {
    	return predatorManager;
    }

    /**
     * Gets the current Active Contracts.
     * @return the current active contracts.
     */
    public ContractHandler getActiveContracts() {
    	return activeContracts;
    }

    /**
     * Gets the available contracts.
     * @return the contract handler with the available contracts.
     */
    public ContractHandler getAvailableContracts() {
    	return availableContracts;
    }

    public void setAvailableContracts(ContractHandler availableContracts) {
    	this.availableContracts = availableContracts;
    }

    /**
     * Clears the available contracts and gets a new set.
     */
    public void resetAvailableContracts() {
    	this.availableContracts = new ContractHandler();
    	for (int i = 0; i < 3; i++) {
            this.availableContracts.addContract(
                    contractGenerator.generatePreMadeContract());
        }
    }

    /**
     * Returns the current market place client.
     * @return the marketplace client.
     */
    public FarmClient getFarmClient() {
        return farmClient;
    }

    /** 
     * Returns location of the staff house.
     * @return
     *      Location of a staff house in the world, null if one doesn't exist.
     */
    public Point getStaffHouseLocation() {
        for (AbstractBuilding building : getBuildings()) {
            if (building instanceof StaffHouse) {
                return building.getLocation();
            }
        }
        // staff house does not exist
        return null;
    }

    public TechTree getTechTree(){
        return techTree;
    }
}
