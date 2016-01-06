package farmsim.buildings;

import farmsim.tiles.Tile;
import farmsim.util.Point;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class BuildingTileEntityTest {

    @Test
    public void test() {
        AbstractBuilding building = mock(AbstractBuilding.class);
        when(building.getLocation()).thenReturn(new Point(2.0, 4.0));
        when(building.getWorldX()).thenReturn(2.0);
        when(building.getWorldY()).thenReturn(4.0);
        when(building.getHeight()).thenReturn(2);
        when(building.getWidth()).thenReturn(2);

        Tile tile = mock(Tile.class);
        when(tile.getWorldX()).thenReturn(3.0);
        when(tile.getWorldY()).thenReturn(4.0);

        BuildingTileEntity entity = new BuildingTileEntity(building, tile);

        assertEquals("Tile entity's draw location X should be same as the "
                + "building's.", 2.0, entity.getWorldX(), 0);
        assertEquals("Tile entity's draw location Y should be same as the "
                + "building's.", 4.0, entity.getWorldY(), 0);

        assertFalse("Buildings should not be clearable.",
                entity.isClearable());

        assertEquals("Building has changed", building, entity.getBuilding());
    }
}
