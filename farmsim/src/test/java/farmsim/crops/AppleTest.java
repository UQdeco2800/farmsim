package farmsim.crops;

import farmsim.tiles.TileProperty;
import farmsim.world.terrain.Biomes;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import farmsim.contracts.ContractHandler;
import farmsim.entities.agents.Agent;
import farmsim.entities.tileentities.crops.*;
import farmsim.tiles.Tile;

@PrepareForTest({DatabaseHandler.class})
@RunWith(PowerMockRunner.class)
public class AppleTest {

    private Apple apple;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AppleTest.class);
    
    /**
     * Mocks the relevant classes and static calls so the Apple class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 500);
        properties.put("Gtime3", 1000);
        properties.put("Gtime4", 1500);
        properties.put("Gtime5", 2000);
        properties.put("Hquantity", 8);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Apple1");
        properties.put("StNames3", "Apple2");
        properties.put("StNames4", "Apple3");
        properties.put("DeadStage1", "Apple1dead");
        properties.put("DeadStage2", "Apple2dead");
        properties.put("DeadStage3", "Apple3dead");
        properties.put("Penvironment", "FOREST");
        properties.put("Nenvironment", "ROCKY");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Apple")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the apple.
     */
    @Test
    public void initialisationTest() {
        apple = new Apple(tile);
        assertEquals("Apple", apple.getName());
        assertFalse(apple.isDead());
        assertTrue(apple.isOrchard("Apple"));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        apple = new Apple(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        apple.tick();
        assertFalse("Not dead yet", apple.isDead());
        assertEquals("Incorrect stage", apple.getTileType(), "seed");
        for(int i = 0; i < 2; i++) { 
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            apple.tick();
            assertFalse("Not dead yet", apple.isDead());
            switch(i) {
                case 0:
                    assertEquals("Incorrect stage", apple.getTileType(), "Apple1");
                    break;
                case 1:
                    assertEquals("Incorrect stage", apple.getTileType(), "Apple2");
                    break;
            }
        }
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        apple.tick();
        assertTrue("Is dead", apple.isDead());
        assertEquals("Incorrect stage", apple.getTileType(), "Apple2dead");
    }
    
    /**
     * Tests the mechanism used to alter the growth rates of the crops.
     */
    @Test
    public void advanceTest() {
        apple = new Apple(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        for(int i = 0; i < 2; i++) { 
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            apple.advanceGrowth(200);
            apple.tick();
            switch(i) {
                case 0:
                    assertEquals("Incorrect stage", apple.getTileType(), "Apple1");
                    break;
                case 1:
                    assertEquals("Incorrect stage", apple.getTileType(), "Apple2");
                    break;
            }
        }
    }
  
    /**
     * Tests that a negative biome increases the growth time.
     */
    @Test
    public void testNegativeBiome() {
        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.ROCKY);
        Apple apple = new Apple(tile);
        try {
           Thread.sleep(700);
       } catch (InterruptedException e) {
           LOGGER.error(e.getMessage());
       }
       apple.tick();
       assertEquals("Wrong stage", "seed", apple.getTileType());
    }
}


