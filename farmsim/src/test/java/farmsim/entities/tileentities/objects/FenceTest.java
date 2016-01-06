package farmsim.entities.tileentities.objects;

import static org.junit.Assert.*;

import farmsim.tiles.Tile;
import org.junit.Test;

public class FenceTest {

    @Test
    public void testFence() {
        Tile testTile = new Tile(0, 0, 1);
        Fence fence = new Fence(testTile, null, false);
        assertEquals(true, fence.isClearable());
    }

    @Test
    public void testToggleGate() {
        Tile testTile = new Tile(0, 0, 1);
        Fence fence = new Fence(testTile, null, true);

        assertEquals(false, fence.isGateOpen());
        fence.toggleGate();
        assertEquals(true, fence.isGateOpen());
        fence.toggleGate();
        assertEquals(false, fence.isGateOpen());
    }

    @Test
    public void testGetTileType() {
        Tile testTile = new Tile(0, 0, 1);

        String[] tileTypes = {"fenceBL", "fenceBR", "fenceH", "fenceP",
                "fenceTL", "fenceTR", "fenceV", "fenceT", "fenceP"};
        
        String[] locations = {"BL", "BR", "H", "P",
                "TL", "TR", "V", "T", "P"};

        int i = 0;
        for (String type: tileTypes) {
            Fence fence = new Fence(testTile, locations[i], false);
            assertEquals(type, fence.getTileType());
            i++;
        }

        Fence fence = new Fence(testTile, null, true);
        assertEquals("fenceGateClosed", fence.getTileType());
        fence.toggleGate();
        assertEquals("fenceGateOpen", fence.getTileType());
    }
}
