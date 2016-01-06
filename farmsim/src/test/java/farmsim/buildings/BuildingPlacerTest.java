package farmsim.buildings;

import farmsim.util.Point;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class BuildingPlacerTest {

    BuildingPlacer placer;

    @Before
    public void setup() {
        placer = new BuildingPlacer();
    }

    @Test
    public void testPlacer() {
        assertFalse("placer should not be placing building",
                placer.isPlacingBuilding());
        assertNull("placer should not have a building",
                placer.getBuilding());

        AbstractBuilding building = mock(AbstractBuilding.class);
        placer.startPlacingBuilding(building);

        assertTrue("placer should be placing a building",
                placer.isPlacingBuilding());

        assertEquals("building is not the same", building,
                placer.getBuilding());

        placer.stopPlacingBuilding();

        assertFalse("placer should not be placing building",
                placer.isPlacingBuilding());
        assertNull("placer should not have a building",
                placer.getBuilding());
    }

    @Test
    public void testPlace() {
        Point location = new Point(0, 0);
        assertTrue("placer should receive all click events",
                placer.containsPoint(location));

        placer.click(location);

        AbstractBuilding building = mock(AbstractBuilding.class);
        when(building.addToWorld()).thenReturn(false);

        placer.startPlacingBuilding(building);
        placer.click(location);

        assertTrue("placer should still be placing a building",
                placer.isPlacingBuilding());
        assertEquals("building is not the same", building,
                placer.getBuilding());
        verify(building).setLocation(any(Point.class));

        reset(building);
        when(building.addToWorld()).thenReturn(true);
        placer.click(location);

        assertFalse("placer should not still be placing a building",
                placer.isPlacingBuilding());
        assertNull("placer should not have a building",
                placer.getBuilding());
        verify(building).setLocation(any(Point.class));
    }

    @Test
    public void testEventHandler() {
        BuildingPlacerEventHandler handler = mock(
                BuildingPlacerEventHandler.class);

        AbstractBuilding building = mock(AbstractBuilding.class);
        when(building.addToWorld()).thenReturn(false);
        placer.startPlacingBuilding(building, handler);

        placer.click(new Point(0, 0));
        verify(handler, never()).onPlace(any());
        verify(handler, never()).onCancel(any());

        placer.stopPlacingBuilding();
        verify(handler, never()).onPlace(any());
        verify(handler, times(1)).onCancel(building);

        reset(handler);
        when(building.addToWorld()).thenReturn(true);
        placer.startPlacingBuilding(building, handler);

        placer.click(new Point(0, 0));
        verify(handler, times(1)).onPlace(building);
        verify(handler, never()).onCancel(any());
    }
}
