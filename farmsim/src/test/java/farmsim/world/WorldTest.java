package farmsim.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import farmsim.buildings.AbstractBuilding;
import farmsim.tiles.TileProperty;
import farmsim.util.Point;
import farmsim.world.weather.Season;
import farmsim.world.weather.WeatherType;

public class WorldTest {

    private World world;

    @Before
    public void createWorld() {
        world = new World("world", 30, 30, 1, 1);
        WorldManager mgr = WorldManager.getInstance();
        mgr.setWorld(world);
    }
    
    @Test
    public void testGetName() {
        assertEquals("world", world.getName());
    }

    @Test
    public void testGetWidth() {
        assertEquals(30, world.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(30, world.getHeight());
    }

    @Test
    public void testSetDimensionsUp() {
        world.setDimensions(40, 40);
        assertEquals(40, world.getWidth());
        assertEquals(40, world.getHeight());
    }

    @Test
    public void testSetDimensionsDown() {
        world.setDimensions(20, 20);
        assertEquals(20, world.getWidth());
        assertEquals(20, world.getHeight());
    }

    @Test
    public void testSetDimensionsRevert() {
        world.setDimensions(40, 40);
        world.setDimensions(30, 30);
        assertEquals(30, world.getWidth());
        assertEquals(30, world.getHeight());
    }

    @Test
    public void testSetDimensionsTileConsistency() {
        world.setTile(10, 10, 2);
        world.setDimensions(40, 40);
        assertEquals(2, world.getTile(10, 10).getTileType());
        /* Test getTile with a point while we're here */
        assertEquals(2, world.getTile(new Point(10, 10)).getTileType());
    }
    
    @Test
    public void testWeather() {
        world.setWeather(WeatherType.RAINING);
        assertEquals(WeatherType.RAINING, world.getWeatherType());
        assertEquals("Raining", world.getWeatherName());
    }
    
    @Test(expected = Exception.class) 
    public void testAdjustMoistureBelowBounds() throws Exception {
        world.adjustMoisture((int) (World.MIN_MOISTURE - 1));
    }
    
    @Test(expected = Exception.class) 
    public void testAdjustMoistureAboveBounds() throws Exception {
        world.adjustMoisture((int) (World.MAX_MOISTURE + 1));
    }
    
    @Test
    public void testAdjustMoisture() throws Exception {
        world.getTile(4, 5).setProperty(TileProperty.MOISTURE, World.MAX_MOISTURE - 4);
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                world.getTile(x, y).setProperty(TileProperty.MOISTURE, World.MAX_MOISTURE - 4);
            }
        }
        world.adjustMoisture(3);
        assertEquals(World.MAX_MOISTURE - 1, world.getTile(4, 5).getProperty(TileProperty.MOISTURE));
    }
    
    @Test(expected = Exception.class) 
    public void testUpdateLakesMin() throws Exception {
        world.updateLakes((int) (World.MIN_ELEVATION - 1));
    }
    
    @Test(expected = Exception.class) 
    public void testUpdateLakesMax() throws Exception {
        world.updateLakes((int) (World.MAX_ELEVATION - 1));
    }
    
    @Test
    public void testUpdateLakes() throws Exception {
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                world.getTile(x, y).setProperty(TileProperty.ELEVATION, World.MAX_ELEVATION - 5);
            }
        }
        world.updateLakes((int) (World.MAX_ELEVATION - 3));
        assertEquals("water", world.getTile(5, 6).getTileEntity().getTileType());
    }
    
    @Test
    public void testGetSeasonObject() {
        world.tick();
        Assert.assertTrue(world.getSeasonObject() instanceof Season);
        Assert.assertNotNull(world.getSeasonObject());
    }
}
