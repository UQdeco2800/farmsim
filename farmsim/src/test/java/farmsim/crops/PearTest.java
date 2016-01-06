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
public class PearTest {

    private Pear pear;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Pear class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 360000);
        properties.put("Gtime3", 720000);
        properties.put("Gtime4", 1080000);
        properties.put("Gtime5", 1620000);
        properties.put("Hquantity", 10);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Pear1");
        properties.put("StNames3", "Pear2");
        properties.put("StNames4", "Pear3");
        properties.put("DeadStage1", "Pear1dead");
        properties.put("DeadStage2", "Pear2dead");
        properties.put("DeadStage3", "Pear3dead");
        properties.put("Penvironment", "FOREST");
        properties.put("Nenvironment", "ROCKY");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Pear")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the pear.
     */
    @Test
    public void initialisationTest() {
        pear = new Pear(tile);
        assertEquals("Pear", pear.getName());
        assertFalse(pear.isDead());
        assertTrue(pear.isOrchard(pear.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        pear = new Pear(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        pear.tick();
        assertFalse("Not dead yet", pear.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        pear.tick();
        assertTrue("Is dead", pear.isDead());
        assertEquals("Incorrect stage", pear.getTileType(), "seed");
    }
}

