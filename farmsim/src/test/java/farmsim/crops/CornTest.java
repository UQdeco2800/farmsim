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
public class CornTest {

    private Corn corn;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Corn class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 280000);
        properties.put("Gtime3", 560000);
        properties.put("Gtime4", 840000);
        properties.put("Gtime5", 1260000);
        properties.put("Hquantity", 6);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Corn1");
        properties.put("StNames3", "Corn2");
        properties.put("StNames4", "Corn3");
        properties.put("DeadStage1", "Corn1dead");
        properties.put("DeadStage2", "Corn2dead");
        properties.put("DeadStage3", "Corn3dead");
        properties.put("Penvironment", "GRASSLAND");
        properties.put("Nenvironment", "ARID");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Corn")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the corn.
     */
    @Test
    public void initialisationTest() {
        corn = new Corn(tile);
        assertEquals("Corn", corn.getName());
        assertFalse(corn.isDead());
        assertFalse(corn.isOrchard(corn.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        corn = new Corn(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        corn.tick();
        assertFalse("Not dead yet", corn.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        corn.tick();
        assertTrue("Is dead", corn.isDead());
        assertEquals("Incorrect stage", corn.getTileType(), "seed");
    }
}

