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
public class CottonTest {

    private Cotton cotton;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;
    
    /**
     * Mocks the relevant classes and static calls so the Cotton class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 200000);
        properties.put("Gtime3", 400000);
        properties.put("Gtime4", 600000);
        properties.put("Gtime5", 900000);
        properties.put("Hquantity", 4);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Cotton1");
        properties.put("StNames3", "Cotton2");
        properties.put("StNames4", "Cotton3");
        properties.put("DeadStage1", "Cotton1dead");
        properties.put("DeadStage2", "Cotton2dead");
        properties.put("DeadStage3", "Cotton3dead");
        properties.put("Penvironment", "sunny");
        properties.put("Nenvironment", "dry");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Cotton")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the cotton.
     */
    @Test
    public void initialisationTest() {
        cotton = new Cotton(tile);
        assertEquals("Cotton", cotton.getName());
        assertFalse(cotton.isDead());
        assertFalse(cotton.isOrchard(cotton.getName()));
    }

    @Test
    public void tickTest() {
        cotton = new Cotton(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        cotton.tick();
        assertFalse("Not dead yet", cotton.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        cotton.tick();
        assertTrue("Is dead", cotton.isDead());
        assertEquals("Incorrect stage", cotton.getTileType(), "seed");
    }
}

