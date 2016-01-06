package farmsim;

import farmsim.world.World;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniMapTest {
    @Test
    public void sizeVerification() {
        World world = new World("test", 20, 20, 0, 1);
        MiniMap minimap = new MiniMap(world, null);

        assertEquals(
                Double.compare(minimap.getWidth(), 20 * minimap.getTileSize()),
                0
        );
        assertEquals(
                Double.compare(minimap.getHeight(), 20 * minimap.getTileSize()),
                0
        );

        world.setDimensions(30, 25);
        assertEquals(
                Double.compare(minimap.getWidth(), 30 * minimap.getTileSize()),
                0
        );
        assertEquals(
                Double.compare(minimap.getHeight(), 25 * minimap.getTileSize()),
                0
        );

        world.setDimensions(2, 3);
        assertEquals(
                Double.compare(minimap.getWidth(), 2 * minimap.getTileSize()),
                0
        );
        assertEquals(
                Double.compare(minimap.getHeight(), 3 * minimap.getTileSize()),
                0
        );
    }
}
