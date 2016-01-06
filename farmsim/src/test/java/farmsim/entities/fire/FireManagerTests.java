package farmsim.entities.fire;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.crops.Apple;
import farmsim.entities.tileentities.objects.Rock;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.world.World;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for the FireManager class.
 * 
 * @author 
 *      yojimmbo
 */
@PrepareForTest({TileRegister.class})
@RunWith(PowerMockRunner.class)
public class FireManagerTests {
    
    /**FireManager instance*/
    private FireManager fireManager;
    /**Used to mock a world tile**/
    private Tile tile;
    /**Used to mock world calls**/
    private World world;
    /**Used to mock an Apple tile entity**/
    private TileEntity tileEntityApple;
    /**Used to mock a non rock tile entity**/
    private TileEntity tileEntityRock;
    /**Weather properties that a fire can spawn in**/
    private Map<String, Integer> spawnWeather;
    /**Weather properties that a fire can't spawn in**/
    private Map<String, Integer> nonSpawnWeather;
    /**Weather properties that a fire can't spawn in**/
    private Map<String, Integer> nonSpawnWeather1;
    
    private TileRegister tileRegister;
    
    /**
     * Test setup before tests are run.
     */
    @Before
    public void setup() {
        world = mock(World.class);
        fireManager = new FireManager(world);
        tile = mock(Tile.class);
        tileEntityApple = mock(Apple.class);
        tileEntityRock = mock(Rock.class);
        spawnWeather = new HashMap<String, Integer>();
        nonSpawnWeather = new HashMap<String, Integer>();
        nonSpawnWeather1 = new HashMap<String, Integer>();
        spawnWeather.put("Temperature", 4);
        spawnWeather.put("Sunny", 4);
        spawnWeather.put("Cloudy", 4);
        nonSpawnWeather.put("Temperature", 2);
        nonSpawnWeather.put("Rain", 2);
        nonSpawnWeather.put("Snow", 2);
        nonSpawnWeather1.put("Temperature", 4);
        nonSpawnWeather1.put("Rain", 2);
        nonSpawnWeather1.put("Snow", 2);
        when(world.getHeight()).thenReturn(5);
        when(world.getWidth()).thenReturn(5);
        tileRegister = mock(TileRegister.class);
        PowerMockito.mockStatic(TileRegister.class);
        PowerMockito.when(TileRegister.getInstance()).thenReturn(tileRegister);
    }
    
    /**
     * Tests creating a fire at different valid and invalid locations. 
     */
    @Test
    public void createFireLocationsTest() {
        //Low water level, valid burnable tile entity, should return true.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertTrue(fireManager.createFire(0, 0));    
        
        //Low water level, tile x out of bounds, should return false.
        when(world.getTile(6, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(6, 0));  
        
        //Low water level, tile x out of bounds, should return false.
        when(world.getTile(0, 6)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(0, 6));  
        
        //Low water level, positive bounds test, should return false.
        when(world.getTile(5, 5)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(5, 5));  
        
        //Low water level, negative bounds test, should return false.
        when(world.getTile(-1, -1)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(-1, -1));  
        
        //Same tests, now with time to burn, should return true.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertTrue(fireManager.createFire(0, 0, 350));    
        
        //Same tests, now with time to burn, should return false.
        when(world.getTile(6, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(6, 0, 350));  
        
        //Same tests, now with time to burn, should return false.
        when(world.getTile(0, 6)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(0, 6, 350));  
    }
    
    /**
     * Tests creating a fire with a high water level. 
     */
    @Test
    public void createFireHighWaterLevelTrueTest() {
        //High water level, valid burnable tile entity, should return false.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.6);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        assertFalse(fireManager.createFire(0, 0));    
        
        //High water level, invalid tile entity, should return false.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.6);
        when(tile.getTileEntity()).thenReturn(tileEntityRock); 
        assertFalse(fireManager.createFire(0, 0));  
    }
    
    /**
     * Tests creating a fire with a low water level.
     */
    @Test
    public void createFireLowWaterLevelTrueTest() {
        //Low water level, valid tile entity, should return true.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple);        
        assertTrue(fireManager.createFire(0, 0));
        
        //Low water level, invalid tile entity, should return false.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityRock);        
        assertFalse(fireManager.createFire(0, 0));      
    }
    
    
    /**
     * Tests creating a fire in different weather.
     */
    @Test
    public void createFireWeatherTests() {
        //Low water level, valid tile entity, valid weather, should return true.
        when(world.getTile(0, 0)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple);   
        when(world.getWeatherComponents()).thenReturn(spawnWeather);
        assertTrue(fireManager.createFire(0, 0));  
        
        //Low water level, valid tile entity, bad weather, should return false.
        when(world.getTile(1, 1)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple);   
        when(world.getWeatherComponents()).thenReturn(nonSpawnWeather);
        assertFalse(fireManager.createFire(1, 1)); 
        
      //Low water level, valid tile entity, bad weather, should return false.
        when(world.getTile(1, 1)).thenReturn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple);   
        when(world.getWeatherComponents()).thenReturn(nonSpawnWeather1);
        assertFalse(fireManager.createFire(1, 1)); 
    }
    
    /**
     * Tests the tick method.
     */
    @Test
    public void tickTests() {
        //Low water level, valid tile entity, valid weather, should return true.
        fireManager = new FireManager(world);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                when(world.getTile(i, j)).thenReturn(tile);
            }
        }
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple); 
        //when(tileRegister.getTileType("grass"));
        when(world.getWeatherComponents()).thenReturn(spawnWeather);
        for (int i = 0; i < 600; i++) {
            fireManager.tick();
        }
        
        //Low water level, valid tile entity, bad weather, should return false.
        fireManager = new FireManager(world);
        when(tile.getWaterLevel()).thenReturn((float) 0.4);
        when(tile.getTileEntity()).thenReturn(tileEntityApple);   
        when(world.getWeatherComponents()).thenReturn(nonSpawnWeather);
        for (int i = 0; i < 600; i++) {
            fireManager.tick();
        }
    }
}
