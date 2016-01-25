package farmsim.world.generators;

import java.util.ArrayList;
import java.util.Random;

import farmsim.buildings.StaffHouse;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.Rock;
import farmsim.entities.tileentities.objects.Tree;
import farmsim.entities.tileentities.objects.Water;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.util.Perlin;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.terrain.Biomes;

import static java.lang.Math.random;

/**
 * An example implementation of the WorldGenerator interface.
 *
 * Note that this class _is not a real builder pattern_ as the World is created
 * instantly at configureWorld() and is returned by build(). In a real builder
 * pattern, all the setter/configure methods should simply make the Generator
 * remember the configuration and the World is not instantiated until the
 * build() call itself.
 *
 * @author Anonymousthing
 */
public class BasicWorldGenerator implements WorldGenerator {
    private static final String NO_WORLD_EXCEPTION_MESSAGE =
            "You must configure a world through the method configureWorld first or provide a world to build upon.";
    private static final String WORLD_ALREADY_CONFIGURED_EXCEPTION_MESSAGE =
            "World is already configured.";

    World currentWorld;
    long seed;
    
    private int minX = 0;
    private int minY = 0;
    private int maxX;
    private int maxY;

    public BasicWorldGenerator() {
        currentWorld = null;
    }

    /**
     * Initialises a World based on an already existing one. This allows Worlds
     * to be configured multiple times in different generators; as such, you
     * could have a "BuildingGenerator" which could add buildings to a World
     * created in a BasicWorldGenerator.
     *
     * @param world The world to build upon
     * @throws Exception
     */
    @Override
    public void configureWorld(World world) throws Exception {
        if (currentWorld != null) {
            throw new Exception(WORLD_ALREADY_CONFIGURED_EXCEPTION_MESSAGE);
        }

        currentWorld = world;
    }

    /**
     * Initialises a new World to be built.
     *
     * @param name Name of the World
     * @param width Width of the World
     * @param height Height of the World
     * @throws Exception
     */
    public void configureWorld(String name, int width, int height, long seed)
            throws Exception {
        if (currentWorld != null) {
            throw new Exception(WORLD_ALREADY_CONFIGURED_EXCEPTION_MESSAGE);
        }
        TileRegister tileRegister = TileRegister.getInstance();
        
        maxX = width;
        maxY = height;

        this.seed = seed;
        currentWorld = new World(name, width, height,
                tileRegister.getTileType("grass"), seed);
    }
    
    /**
     * Set the bounds for the shape to add to the world. This is a union, so the
     * area between minX and maxX as well as minY and maxY will be filled. 
     * This method doesn't need to be called when creating a new world
     * 
     * @param minX
     *            the smallest x value
     * @param minY
     *            the smallest y value
     * @param maxX
     *            the largest x value
     * @param maxY
     *            the largest y value
     */
    public void setExtensionSize(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        currentWorld.initialise(minX, minY, maxX, maxY);
    }

    /**
     * Runs the creation of rivers.
     *
     * @throws Exception
     */
    public void createRivers() throws Exception {
        if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);

        for(int i = 0; i < 10; i++) {
        	riverPoints(i);
        }
    }
    
    /**
     * Finds the edge of a lake, finds a higher altitude close by.
     * creates points for the river to be drawn, and draws the river
     *
     * @param seed
     *              the seed for the random number generation
     * @throws Exception
     */
    public void riverPoints(long seed) throws Exception {
    	if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);
    	
    	int x1, x2, y1, y2, dir;
        ArrayList<Tile> water = new ArrayList<>();
    	
    	for (int y = 0; y < maxY; y++) {
    		for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
    			Tile tile = currentWorld.getTile(x, y);
                if ((int) tile.getProperty(TileProperty.ELEVATION) < World.LAKE_ELEVATION_CUTOFF) {
                    water.add(tile);
                }
    		}
    	}

        if (water.isEmpty()) {
            return;
        }

    	Random randomGenerator = new Random(seed);
        int randomInt = randomGenerator.nextInt(water.size());
        x2 = (int) water.get(randomInt).getWorldX();
        y2 = (int) water.get(randomInt).getWorldY();
        
        int xMin = x2 - 100;
        if (xMin < 0) {
        	xMin = 0;
        }
       
        int xMax = x2 + 100;
        if (xMax > currentWorld.getWidth()) {
        	xMax = currentWorld.getWidth();
        }
        
        int yMin = y2 - 100;
        if (yMin < 0) {
        	yMin = 0;
        }
        
        int yMax = y2 + 100;
        if (yMax > currentWorld.getHeight()) {
        	yMax = currentWorld.getHeight();
        }

        ArrayList<Tile> sources = new ArrayList<>();

        for (int x = xMin; x < xMax; x++) {
    		for (int y = yMin; y < yMax; y++) {
    			Tile tile = currentWorld.getTile(x, y);
                if ((int) tile.getProperty(TileProperty.ELEVATION) > 1) {
                    sources.add(tile);
                }
    		}
    	}

        if (sources.isEmpty()) {
            return;
        }

        Tile source = getNearestSource(sources, x2, y2);
        x1 = (int) source.getWorldX();
        y1 = (int) source.getWorldY();

        addRiver(x1, y1, x2, y2);
    }

    /**
     * Return the nearest tile in sources to point (x,y)
     * @param sources
     *              a list of possible source tiles
     * @param x
     *              x-coord of point to check distance from
     * @param y
     *              y-coord of point to check distance from
     * @return
     *              the nearest source to point (x,y) - if multiple tiles have
     *              the same distance, one is returned
     */
    private Tile getNearestSource(ArrayList<Tile> sources, int x, int y) {
        double minDistance = getDistance(x, y, (int) sources.get(0).getWorldX(),
                (int) sources.get(0).getWorldY());
        Tile minTile = sources.get(0);
        for (Tile source : sources) {
            if (getDistance(x, y, (int) source.getWorldX(),
                    (int) source.getWorldY()) < minDistance) {
                minDistance = getDistance(x, y, (int) source.getWorldX(),
                        (int) source.getWorldY());
                minTile = source;
            }
        }

        return minTile;
    }

    /**
     * Get the distance from point (x1,y1) to point (x2,y2)
     * @param x1
     *          x-coord of first point
     * @param y1
     *          y-coord of first point
     * @param x2
     *          x-coord of second point
     * @param y2
     *          y-coord of second point
     * @return
     *          distance between points, as calculated using Pythagoras
     */
    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    /**
     * Tries to recursively generate a river from the coordinates given
     *
     * @param x1
     *          x-coord of point marking river start
     * @param y1
     *          y-coord of point marking river start
     * @param x2
     *          x-coord of point marking river end
     * @param y2
     *          y-coord of point marking river end
     * @throws Exception
     *          if no world exists
     * @return
     *          True if a river can be made,
     * 		    False if a river cannot be made
     */
    public boolean addRiver(int x1, int y1, int x2, int y2) throws Exception {
        if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);

        if (x1 == x2 && y1 == y2) {
            return true;
        }

        Tile current = currentWorld.getTile(x1, y1);
        int currElevation = (int) current.getProperty(TileProperty.ELEVATION);
        ArrayList<Tile> adjacent = new ArrayList<>();
        Point[] points = {new Point(x1 - 1, y1 - 1), new Point(x1, y1 - 1),
                new Point(x1 + 1, y1 - 1), new Point(x1 + 1, y1),
                new Point(x1 + 1, y1 + 1), new Point(x1, y1 + 1),
                new Point(x1 - 1, y1 + 1), new Point(x1 - 1, y1)};
        for (Point point : points) {
            if (point.getX() < 0 || point.getX() >= currentWorld.getWidth() ||
                point.getY() < 0 || point.getY() >= currentWorld.getHeight()) {
                continue;
            }

            Tile tile = currentWorld.getTile((int) point.getX(), (int) point.getY());
            if ((int) tile.getProperty(TileProperty.ELEVATION) <= currElevation &&
                getDistance((int) point.getX(), (int) point.getY(), x2, y2) <
                    getDistance(x1, y1, x2, y2)) {
                adjacent.add(tile);
            }
        }

        if (adjacent.isEmpty()) {
            return false;
        }

        Tile next = getNearestSource(adjacent, x2, y2);

        Random random = new Random(Integer.hashCode(x1) * Integer.hashCode(y1));
        for (int i = 0; i < 2; i++) {
            if (random.nextDouble() < 0.5) {
                adjacent.remove(next);
                if (!adjacent.isEmpty()) {
                    next = getNearestSource(adjacent, x2, y2);
                }
            }
        }

        if (addRiver((int) next.getWorldX(), (int) next.getWorldY(), x2, y2)) {
            current.setTileEntity(new Water(current));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates lakes from lowest 2 layers
     *
     * @throws Exception
     */
    public void addLakes() throws Exception {
        if (currentWorld == null) {
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);
        }

        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                int random = (int)(Math.random()*100);
                Tile tile = currentWorld.getTile(x, y);
                if ((int)  tile.getProperty(TileProperty.ELEVATION) < World.LAKE_ELEVATION_CUTOFF) {
                        tile.setTileEntity(new Water(tile));
                }
            }
        }
    }

    /**
     * Sets every tile in the World to a specified tile type.
     *
     * @param tileTypes The tile type to set every tile to
     * @throws Exception
     */
    public void setBaseTileTypes(int[] tileTypes) throws Exception {
        if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);
        Random random = new Random();
        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                addRandom(tileTypes, random, x, y);
            }
        }
    }

    /**
     * Choose a random tile to add to the world
     * @param tileTypes the list of tileTypes to choose from
     * @param random a RNG
     * @param x x coordinate to add the tile at
     * @param y y coordinate to add the tile at
     */
    private void addRandom(int[] tileTypes, Random random, int x, int y) {
        double randomDouble = random.nextDouble();
        for (int i = 0; i < tileTypes.length; i++) {
            if ((double) i / tileTypes.length < randomDouble) {
                currentWorld.getTile(x, y).setTileType(tileTypes[i]);
            }
        }
    }

    /**
     * Sets the water level of each tile based on moisture and proximity to
     * water entity.
     *
     * @throws Exception
     */
    public void setTileWaterLevel() throws Exception {
        if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);
        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                Tile tile = currentWorld.getTile(x, y);
                double moisture =
                        (double) tile.getProperty(TileProperty.MOISTURE);
                tile.setWaterLevel(
                        (float) (moisture / (World.MAX_MOISTURE - World.MIN_MOISTURE)));
            }
        }
    }

    /**
     * Set the elevation for the map
     *
     * @throws Exception if wrong arguments are given to Perlin.makenoise
     */
    public void setElevation() throws Exception {
        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                currentWorld.getTile(x, y).setProperty(TileProperty.ELEVATION,
                        (int) Perlin.makeNoise(x, y, 5, currentWorld.getSeed(),
                                World.MIN_ELEVATION, World.MAX_ELEVATION));
            }
        }
    }

    /**
     * Set the moisture levels for the map
     *
     * @throws Exception if wrong arguments are given to Perlin.makenoise
     */
    public void setMoisture() throws Exception {
        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                currentWorld.getTile(x, y).setProperty(TileProperty.MOISTURE,
                        Perlin.makeNoise(x, y, 3,
                                currentWorld.getSeed() * 10, World.MIN_MOISTURE,
                                World.MAX_MOISTURE));
            }
        }
    }

    /**
     * Update the moisture levels for the map, taking rivers and lakes into
     * account
     */
    public void updateMoisture() throws Exception {
        int height = currentWorld.getHeight();
        int width = currentWorld.getWidth();
        double moistureRange = Math.abs(World.MAX_MOISTURE - World.MIN_MOISTURE);

        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                Tile tile = currentWorld.getTile(x, y);
                TileEntity tileEntity;

                double moisture =
                        (double) tile.getProperty(TileProperty.MOISTURE);
                int waterCount = 0;

                // iterate around 10x10 square around tile - ugly ternaries to
                // ensure it doesn't go out of the world bounds
                for (int i = (y < 5) ? 0 : y - 5; i < ((y + 5 >= height) ? height
                        : y + 5); i++) {
                    for (int j = (x < 5) ? 0 : x - 5; j < ((x + 5 >= width)
                            ? width : x + 5); j++) {
                        tileEntity = currentWorld.getTile(j, i).getTileEntity();

                        if (tileEntity == null) {
                            continue;
                        }
                        if (tileEntity.getClass() == Water.class) {
                            waterCount++;
                        }
                    }
                }

                moisture += waterCount / 2;

                if ((moisture < moistureRange * 0.25) && (x * y < 2500)) {
                    moisture += 3 * (int) (random() + 1);
                    moisture += 3 * (int) (random() + 1);
                }
                if (moisture > World.MAX_MOISTURE) {
                    moisture = World.MAX_MOISTURE;
                }

                tile.setProperty(TileProperty.MOISTURE, moisture);
            }
        }
    }

    /**
     * Set the biomes for the map, based on elevation and moisture
     */
    public void setBiomes() throws Exception {
        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                selectBiome(x, y);
            }
        }
    }

    /**
     * Choose a biome for a particular tile
     * @param x tiles x coordinate
     * @param y tiles y coordinate
     */
    private void selectBiome(int x, int y) {
        Tile tile = currentWorld.getTile(x, y);
        int elevationRange = Math.abs(World.MAX_ELEVATION - World.MIN_ELEVATION);
        double moistureRange = Math.abs(World.MAX_MOISTURE - World.MIN_MOISTURE);
        int elevation = (int) tile.getProperty(TileProperty.ELEVATION)
                - World.MIN_ELEVATION;
        double moisture = (double) tile.getProperty(TileProperty.MOISTURE)
                - World.MIN_MOISTURE;
        Biomes biome;

        // See biome table on wiki for explanation
        if (elevation > elevationRange * 0.75) {
            if (moisture > moistureRange * 0.75) {
                biome = Biomes.FOREST;
            } else if (moisture > moistureRange * 0.5) {
                biome = Biomes.FOREST;
            } else if (moisture > moistureRange * 0.25) {
                biome = Biomes.ROCKY;
            } else {
                biome = Biomes.ROCKY;
            }
        } else if (elevation > elevationRange * 0.5) {
            if (moisture > moistureRange * 0.75) {
                biome = Biomes.FOREST;
            } else if (moisture > moistureRange * 0.5) {
                biome = Biomes.GRASSLAND;
            } else if (moisture > moistureRange * 0.25) {
                biome = Biomes.GRASSLAND;
            } else {
                biome = Biomes.ARID;
            }
        } else if (elevation > elevationRange * 0.25) {
            if (moisture > moistureRange * 0.75) {
                biome = Biomes.FOREST;
            } else if (moisture > moistureRange * 0.5) {
                biome = Biomes.GRASSLAND;
            } else if (moisture > moistureRange * 0.25) {
                biome = Biomes.GRASSLAND;
            } else {
                biome = Biomes.ARID;
            }
        } else {
            if (moisture > moistureRange * 0.75) {
                biome = Biomes.MARSH;
            } else if (moisture > moistureRange * 0.5) {
                biome = Biomes.GRASSLAND;
            } else if (moisture > moistureRange * 0.25) {
                biome = Biomes.GRASSLAND;
            } else {
                biome = Biomes.ARID;
            }
        }

        tile.setProperty(TileProperty.BIOME, biome);
    }

    /**
     * Add entities to tiles depending on biomes ROCKY -> Rock (0.75 chance)
     * FOREST -> Trees (0.9 chance), ARID -> Arid TileType
     */
    public void addBiomeEntities() {
        TileRegister tileRegister = TileRegister.getInstance();

        for (int y = 0; y < maxY; y++) {
            for (int x = (y > minY) ? 0 : minX; x < maxX; x++) {
                Tile tile = currentWorld.getTile(x, y);
                Biomes biome = (Biomes) tile.getProperty(TileProperty.BIOME);

                if ((biome == Biomes.ROCKY) && (random() < 0.75)) {
                    tile.setTileEntity(new Rock(tile));
                }
                if(tile.getTileEntity() == null){
                    if ((biome == Biomes.FOREST) && (random() < 0.9)) {
                       tile.setTileEntity(new Tree("tree", tile, 25));
                    }
                }
                if (biome == Biomes.ARID) {
                    tile.setTileType(tileRegister.getTileType("arid"));
                }
            }
        }
    }


    /**
     * Adds the HAS_SECRET_GOLD property to a random single tile in the World.
     *
     * @throws Exception
     */
    public void addSecretGold() throws Exception {
        if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);

        int x = (int) (random() * currentWorld.getWidth());
        int y = (int) (random() * currentWorld.getHeight());

        currentWorld.getTile(x, y).setProperty(TileProperty.HAS_SECRET_GOLD,
                true);
    }

    /**
     * Tries to place a staff house somewhere random in the world.
     *
     * @require currentWorld != null
     */
    public void placeStaffHouse() {
        Random random = new Random();
        int tries = 0;
        StaffHouse staffHouse = new StaffHouse(currentWorld);
        do {
            Point location = new Point(
                    random.nextInt(World.BASE_USABLE_SIZE - StaffHouse.WIDTH - 5) + 5,
                    random.nextInt(World.BASE_USABLE_SIZE - StaffHouse.HEIGHT - 5) + 5);
            staffHouse.setLocation(location);
            tries++;
        } while (!staffHouse.addToWorld() && tries < 10);
    }






    /**
     * Returns the built and configured World.
     *
     * @return The configured World
     * @throws Exception
     */
    @Override
    public World build() throws Exception {
        if (currentWorld == null)
            throw new Exception(NO_WORLD_EXCEPTION_MESSAGE);

        // Reset the state of the generator
        World world = currentWorld;
        currentWorld = null;
        return world;
    }
}
