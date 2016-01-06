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
public class LemonTest {

    private Lemon lemon;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Lemon class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 560000);
        properties.put("Gtime3", 1200000);
        properties.put("Gtime4", 1680000);
        properties.put("Gtime5", 2520000);
        properties.put("Hquantity", 14);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Lemon1");
        properties.put("StNames3", "Lemon2");
        properties.put("StNames4", "Lemon3");
        properties.put("DeadStage1", "Lemon1dead");
        properties.put("DeadStage2", "Lemon2dead");
        properties.put("DeadStage3", "Lemon3dead");
        properties.put("Penvironment", "FOREST");
        properties.put("Nenvironment", "ROCKY");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Lemon")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the lemon.
     */
    @Test
    public void initialisationTest() {
        lemon = new Lemon(tile);
        assertEquals("Lemon", lemon.getName());
        assertFalse(lemon.isDead());
        assertTrue(lemon.isOrchard(lemon.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        lemon = new Lemon(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        lemon.tick();
        assertFalse("Not dead yet", lemon.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        lemon.tick();
        assertTrue("Is dead", lemon.isDead());
        assertEquals("Incorrect stage", lemon.getTileType(), "seed");
    }
}

