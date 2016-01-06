package farmsim.buildings;

import farmsim.entities.tileentities.TileEntity;
import farmsim.tasks.TaskManager;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * A class to test the Scarecrow class
 * 
 * @author rachelcatchpoole for Team Adleman
 *
 */
public class ScarecrowTest {

    World world;
    Scarecrow scarecrow;
    Tile tile;

    @Before
    public void setupInstances() {
        world = mock(World.class);
        tile = mock(Tile.class);
        when(world.getWidth()).thenReturn(8);
        when(world.getHeight()).thenReturn(4);
        when(world.containsBuilding(any())).thenReturn(false);
        when(world.getTile(anyInt(),anyInt())).thenReturn(tile);

        setScarecrow(new Point(1,1));
    }

    public void setScarecrow(Point location) {
        scarecrow = new Scarecrow(world) {
            @Override
            public void setSprite(String imageName) {}
        };
        scarecrow.setLocation(location);
    }

    @Test
    public void testWorld() {
        assertEquals("World has changed.", world, scarecrow.getWorld());
    }
    
    /**
     * Test the reduceDaysLeft and isScarecrowDead methods
     */
    @Test
    public void testDead() {
        assertEquals(false, scarecrow.isScarecrowDead());
        // run the scarecrow through its lifespan
        for (int i = 7; i > 1; i--) {
            assertEquals(i, scarecrow.getDaysLeft());
            scarecrow.reduceDaysLeft();
            assertEquals(false, scarecrow.isScarecrowDead());
        }
        assertEquals(1, scarecrow.getDaysLeft());
        scarecrow.reduceDaysLeft();
        assertEquals(0, scarecrow.getDaysLeft());
        assertEquals(true, scarecrow.isScarecrowDead());
        
        // check that there are no more safe tiles, since scarecrow is dead
        testSafeZonesRemoved(1,1);
    }
    
    /**
     * Test the addition and removal of safe zones
     * @param x the x-coordinate of the scarecrow
     * @param y the y-coordinate of the scarecrow
     * @require x and y are valid coordinates
     */
    public void testSafeZonesRemoved(int x, int y) {
        //scarecrow.onRemove();
        int radius = scarecrow.getRadius();
        int minX = Math.max(0, x - radius);
        int maxX = Math.min(world.getWidth(), x + radius);
        int minY = Math.max(0, y - radius);
        int maxY = Math.min(world.getWidth(), y + radius);
        
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                assertEquals(false, world.getTile(i, j).getSafeZone());
            }
        }
    }
    
    /**
     * Test scarecrow location functions
     */
    @Test
    public void testIsLocationValid() {
        Tile tile = mock(Tile.class);
        when(tile.getTileEntity()).thenReturn(null);
        when(world.getTile(anyInt(), anyInt())).thenReturn(tile);

        // put the scarecrow within the world
        scarecrow.setLocation(new Point(1, 1));
        assertTrue("Building is in a valid location",
                scarecrow.isLocationValid());

        // place a tile entity on the tile
        TileEntity entity = mock(TileEntity.class);
        when(tile.getTileEntity()).thenReturn(entity);
        assertFalse("Building has a tile entity underneath it.",
                scarecrow.isLocationValid());
    }


    /**
     * Test the getLocation and setLocation methods
     */
    @Test
    public void testLocation() {
        Point location1 = new Point(0, 0);
        scarecrow.setLocation(location1);

        assertEquals("Location 1 has changed.", 0.0,
                scarecrow.getLocation().getX(), 0);
        assertEquals("Location 1 has changed.", 0.0,
                scarecrow.getLocation().getY(), 0);
        assertEquals("getWorldX is incorrect.", 0.0,
                scarecrow.getWorldX(), 0);
        assertEquals("getWorldY is incorrect.", 0.0,
                scarecrow.getWorldY(), 0);

        Point location2 = new Point(6, 2);
        setScarecrow(location2);

        assertEquals("Location 2 has changed.", 6.0,
                scarecrow.getLocation().getX(), 0);
        assertEquals("Location 2 has changed.", 2.0,
                scarecrow.getLocation().getY(), 0);
        assertEquals("getWorldX is incorrect.", 6.0,
                scarecrow.getWorldX(), 0);
        assertEquals("getWorldY is incorrect.", 2.0,
                scarecrow.getWorldY(), 0);

        when(world.containsBuilding(scarecrow)).thenReturn(true);
        scarecrow.setLocation(location1);

        assertEquals(
                "setLocation should not change the scarecrow's location"
                        + "once it has been added to the world.",
                6.0, scarecrow.getLocation().getX(), 0);
        assertEquals(
                "setLocation should not change the scarecrow's location"
                        + "once it has been added to the world.",
                2.0, scarecrow.getLocation().getY(), 0);
    }
    
}
