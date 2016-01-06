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
public class StrawberryTest {

    private Strawberry strawberry;
    private DatabaseHandler dbHandler;
    private Map<String, Object> properties;
    private Tile tile;

    /**
     * Mocks the relevant classes and static calls so the Strawberry class can 
     * be tested in isolation.
     */
    @Before
    public void setup() {
        properties = new HashMap<>();
        properties.put("Clevel", 1);
        properties.put("Gtime1", 0);
        properties.put("Gtime2", 160000);
        properties.put("Gtime3", 320000);
        properties.put("Gtime4", 480000);
        properties.put("Gtime5", 720000);
        properties.put("Hquantity", 4);
        properties.put("StNames1", "seed");
        properties.put("StNames2", "Strawberry1");
        properties.put("StNames3", "Strawberry2");
        properties.put("StNames4", "Strawberry3");
        properties.put("DeadStage1", "Strawberry1dead");
        properties.put("DeadStage2", "Strawberry2dead");
        properties.put("DeadStage3", "Strawberry3dead");
        properties.put("Penvironment", "GRASSLAND");
        properties.put("Nenvironment", "ARID");
        tile = mock(Tile.class);
        dbHandler = mock(DatabaseHandler.class);
        PowerMockito.mockStatic(DatabaseHandler.class);
        PowerMockito.when(DatabaseHandler.getInstance()).thenReturn(dbHandler);
        when(dbHandler.getPlantData("Strawberry")).thenReturn(properties);

        when(tile.getProperty(TileProperty.BIOME)).thenReturn(Biomes.GRASSLAND);
    }

    /**
     * Checks the initialised state of the strawberry.
     */
    @Test
    public void initialisationTest() {
        strawberry = new Strawberry(tile);
        assertEquals("Strawberry", strawberry.getName());
        assertFalse(strawberry.isDead());
        assertFalse(strawberry.isOrchard(strawberry.getName()));
    }

    /**
     * Check that the tick method behaves correctly depending on the water 
     * state of the tile.
     */
    @Test
    public void tickTest() {
        strawberry = new Strawberry(tile);
        when(tile.getWaterLevel()).thenReturn((float) 0.1);
        strawberry.tick();
        assertFalse("Not dead yet", strawberry.isDead());
        when(tile.getWaterLevel()).thenReturn((float) 0.0);
        strawberry.tick();
        assertTrue("Is dead", strawberry.isDead());
        assertEquals("Incorrect stage", strawberry.getTileType(), "seed");
    }
}

