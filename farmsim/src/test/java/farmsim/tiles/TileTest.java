package farmsim.tiles;

import static org.junit.Assert.*;

import farmsim.entities.tileentities.objects.Rock;
import org.junit.Before;
import org.junit.Test;

/**
 * Test various methods of Tile
 * 
 * @author BlueDragon23
 *
 */
public class TileTest {

    private Tile tile;

    @Before
    public void setup() {
        // TileRegister.getInstance().registerTiles();
        // tile = new Tile(TileRegister.getInstance().getTileType("empty"));
        tile = new Tile(0, 0, 0);
    }

    @Test
    public void testTile() {
        // Tile testTile = new
        // Tile(TileRegister.getInstance().getTileType("grass"));
        // assertEquals(TileRegister.getInstance().getTileType("grass"),
        // testTile.getTileType());
        Tile testTile = new Tile(0, 0, 1);
    }

    @Test
    public void testSetTileType() {
        // tile.setTileType(TileRegister.getInstance().getTileType("grass"));
        // assertEquals(TileRegister.getInstance().getTileType("grass"),
        // tile.getTileType());
        tile.setTileType(1);
        assertEquals(1, tile.getTileType());
    }

    @Test
    public void testClearProperties() {
        tile.setProperty(TileProperty.HAS_SECRET_GOLD, true);
        tile.clearProperties();
        assertNull(tile.getProperty(TileProperty.HAS_SECRET_GOLD));
    }

    @Test
    public void testSetProperty() {
        tile.setProperty(TileProperty.HAS_SECRET_GOLD, true);
        assertEquals(true, tile.getProperty(TileProperty.HAS_SECRET_GOLD));
    }

    @Test
    public void testHasProperty() {
        assertFalse(tile.hasProperty(TileProperty.HAS_SECRET_GOLD));
        tile.setProperty(TileProperty.HAS_SECRET_GOLD, false);
        assertTrue(tile.hasProperty(TileProperty.HAS_SECRET_GOLD));
    }

    @Test
    public void testGetProperty() {
        assertNull(tile.getProperty(TileProperty.HAS_SECRET_GOLD));
        tile.setProperty(TileProperty.HAS_SECRET_GOLD, true);
        assertTrue((boolean) tile.getProperty(TileProperty.HAS_SECRET_GOLD));
    }

    @Test
    public void testRemoveProperty() {
        tile.removeProperty(TileProperty.HAS_SECRET_GOLD);
        assertFalse(tile.hasProperty(TileProperty.HAS_SECRET_GOLD));
        tile.setProperty(TileProperty.HAS_SECRET_GOLD, true);
        assertTrue(tile.hasProperty(TileProperty.HAS_SECRET_GOLD));
        tile.removeProperty(TileProperty.HAS_SECRET_GOLD);
        assertFalse(tile.hasProperty(TileProperty.HAS_SECRET_GOLD));
    }

    @Test
    public void testGetTileType() {
        // assertEquals(TileRegister.getInstance().getTileType("empty"),
        // tile.getTileType());
        assertEquals(0, tile.getTileType());
    }

    @Test
    public void testGetTileEntity() {
        tile.setTileEntity(new Rock(tile));
        assertEquals(new Rock(tile).getTileType(),
                tile.getTileEntity().getTileType());
    }

    @Test
    public void testSetTileEntity() {
        tile.setTileEntity(null);
        tile.setTileEntity(new Rock(tile));
        assertEquals(new Rock(tile).getTileType(),
                tile.getTileEntity().getTileType());
    }

    @Test
    public void testTick() {
        // tile.tick();
    }

    @Test
    public void testGetWaterLevel() {
        tile.increaseWaterLevel();
        assertTrue(1.0 == tile.getWaterLevel());
    }

    @Test
    public void testIncreaseWaterLevel() {
        tile.increaseWaterLevel();
        assertTrue(1.0 == tile.getWaterLevel());
    }

    @Test
    public void testDecreaseWaterLevel() {
        tile.increaseWaterLevel();
        tile.decreaseWaterLevel();
        assertTrue(tile.getWaterLevel() < 1);
        for (int i = 0; i < 1000; i++) {
            tile.decreaseWaterLevel();
        }
        assertTrue(0.0 == tile.getWaterLevel());
    }
}
