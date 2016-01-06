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
public class MangoTest {

    private Mango mango;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Mango class can be
     * tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 400000);
        properties.put("Gtime3", 800000);
        properties.put("Gtime4", 1200000);
        properties.put("Gtime5", 1800000);
        properties.put("Hquantity", 10);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Mango1");
        properties.put("StNames3", "Mango2");
        properties.put("StNames4", "Mango3");
        properties.put("DeadStage1", "Mango1dead");
        properties.put("DeadStage2", "Mango2dead");
        properties.put("DeadStage3", "Mango3dead");
        properties.put("Penvironment", "FOREST");
        properties.put("Nenvironment", "ROCKY");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Mango")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the mango.
     */
    @Test
    public void initialisationTest() {
        mango = new Mango(tile);
        assertEquals("Mango", mango.getName());
        assertFalse(mango.isDead());
        assertTrue(mango.isOrchard(mango.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        mango = new Mango(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        mango.tick();
        assertFalse("Not dead yet", mango.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        mango.tick();
        assertTrue("Is dead", mango.isDead());
        assertEquals("Incorrect stage", mango.getTileType(), "seed");
    }
}

