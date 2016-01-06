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
public class SugarcaneTest {

    private SugarCane sugarcane;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Sugarcane class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 240000);
        properties.put("Gtime3", 480000);
        properties.put("Gtime4", 720000);
        properties.put("Gtime5", 1080000);
        properties.put("Hquantity", 6);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Sugarcane1");
        properties.put("StNames3", "Sugarcane2");
        properties.put("StNames4", "Sugarcane3");
        properties.put("DeadStage1", "Sugarcane1dead");
        properties.put("DeadStage2", "Sugarcane2dead");
        properties.put("DeadStage3", "Sugarcane3dead");
        properties.put("Penvironment", "MARSH");
        properties.put("Nenvironment", "ROCKY");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Sugarcane")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the sugarcane.
     */
    @Test
    public void initialisationTest() {
        sugarcane = new SugarCane(tile);
        assertEquals("Sugarcane", sugarcane.getName());
        assertFalse(sugarcane.isDead());
        assertFalse(sugarcane.isOrchard(sugarcane.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        sugarcane = new SugarCane(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        sugarcane.tick();
        assertFalse("Not dead yet", sugarcane.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        sugarcane.tick();
        assertTrue("Is dead", sugarcane.isDead());
        assertEquals("Incorrect stage", sugarcane.getTileType(), "seed");
    }
}

