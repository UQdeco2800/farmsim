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

import java.util.HashMap;
import java.util.Map;

import farmsim.entities.tileentities.crops.*;
import farmsim.tiles.Tile;

@PrepareForTest({DatabaseHandler.class})
@RunWith(PowerMockRunner.class)
public class LettuceTest {

    private Lettuce lettuce;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Lettuce class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 80000);
        properties.put("Gtime3", 160000);
        properties.put("Gtime4", 240000);
        properties.put("Gtime5", 360000);
        properties.put("Hquantity", 2);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Lettuce1");
        properties.put("StNames3", "Lettuce2");
        properties.put("StNames4", "Lettuce3");
        properties.put("DeadStage1", "Lettuce1dead");
        properties.put("DeadStage2", "Lettuce2dead");
        properties.put("DeadStage3", "Lettuce3dead");
        properties.put("Penvironment", "sunny");
        properties.put("Nenvironment", "dry");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Lettuce")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the lettuce.
     */
    @Test
    public void initialisationTest() {
        lettuce = new Lettuce(tile);
        assertEquals("Lettuce", lettuce.getName());
        assertFalse(lettuce.isDead());
        assertFalse(lettuce.isOrchard(lettuce.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        lettuce = new Lettuce(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        lettuce.tick();
        assertFalse("Not dead yet", lettuce.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        lettuce.tick();
        assertTrue("Is dead", lettuce.isDead());
        assertEquals("Incorrect stage", lettuce.getTileType(), "seed");
    }
}

