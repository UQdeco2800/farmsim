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

public class AbstractBuildingTest {

    World world;
    AbstractBuilding building;

    @Before
    public void setupInstances() {
        world = mock(World.class);
        when(world.getWidth()).thenReturn(8);
        when(world.getHeight()).thenReturn(4);
        when(world.containsBuilding(any())).thenReturn(false);

        setBuilding(4, 3, 1000);
    }

    public void setBuilding(int width, int height, long maxHealth) {
        building = new AbstractBuilding(world, width, height,
                "/empty.png") {
            @Override
            public void setSprite(String imageName) {}
        };
        building.setMaxHealth(maxHealth);
    }

    public void setBuilding(Point location, int width, int height,
            long maxHealth) {
        building = new AbstractBuilding(world, width, height,
                "/empty.png") {
            @Override
            public void setSprite(String imageName) {}
        };
        building.setLocation(location);
        building.setMaxHealth(maxHealth);
    }

    @Test
    public void testWorld() {
        assertEquals("World has changed.", world, building.getWorld());
    }

    @Test
    public void testLocation() {
        Point location1 = new Point(0, 0);
        building.setLocation(location1);

        assertEquals("Location 1 has changed.", 0.0,
                building.getLocation().getX(), 0);
        assertEquals("Location 1 has changed.", 0.0,
                building.getLocation().getY(), 0);
        assertEquals("getWorldX is incorrect.", 0.0,
                building.getWorldX(), 0);
        assertEquals("getWorldY is incorrect.", 0.0,
                building.getWorldY(), 0);


        Point location2 = new Point(6, 2);
        setBuilding(location2, 4, 3, 1000);

        assertEquals("Location 2 has changed.", 6.0,
                building.getLocation().getX(), 0);
        assertEquals("Location 2 has changed.", 2.0,
                building.getLocation().getY(), 0);
        assertEquals("getWorldX is incorrect.", 6.0,
                building.getWorldX(), 0);
        assertEquals("getWorldY is incorrect.", 2.0,
                building.getWorldY(), 0);


        when(world.containsBuilding(building)).thenReturn(true);
        building.setLocation(location1);

        assertEquals(
                "setLocation should not change the building's location"
                        + "once it has been added to the world.",
                6.0, building.getLocation().getX(), 0);
        assertEquals(
                "setLocation should not change the building's location"
                        + "once it has been added to the world.",
                2.0, building.getLocation().getY(), 0);
    }

    @Test
    public void testIsLocationValid() {
        Tile tile = mock(Tile.class);
        when(tile.getTileEntity()).thenReturn(null);
        when(world.getTile(anyInt(), anyInt())).thenReturn(tile);

        // put the building within the world
        building.setLocation(new Point(1, 1));
        assertTrue("Building is in a valid location",
                building.isLocationValid());

        // place a tile entity on the tile
        TileEntity entity = mock(TileEntity.class);
        when(tile.getTileEntity()).thenReturn(entity);
        assertFalse("Building has a tile entity underneath it.",
                building.isLocationValid());

        // put the building half outside the world
        when(tile.getTileEntity()).thenReturn(null);
        setBuilding(new Point(2, 2), 5, 3, 1000);
        assertFalse("Building is not in a valid location",
                building.isLocationValid());
    }

    @Test
    public void testAddToWorld() {
        Tile tile = mock(Tile.class);
        when(tile.getTileEntity()).thenReturn(null);
        when(world.getTile(anyInt(), anyInt())).thenReturn(tile);

        // put the building within the world
        // note from samsin3:
        // this method relied on my dependency injection into 
        //TileEntity that used to happen in a test that ran previous to this test.
        //i have moved it to this class below:
        TileRegister tileRegister = new TileRegister();
        TileRegister mockRegister = mock(TileRegister.class);
        when(mockRegister.getTileImage("")).thenReturn(null);
        tileRegister.setInstance(mockRegister);
        building.setLocation(new Point(1, 1));
        assertTrue("Building should be able to be added to the world",
                building.addToWorld());

        verify(tile, times(4 * 3)).setProperty(TileProperty.IS_BUILDING, true);
        verify(tile, times(4 * 3)).setTileEntity(any(BuildingTileEntity.class));
        verify(world).addBuilding(building);

        when(world.containsBuilding(building)).thenReturn(true);
        assertFalse("Building already added to the world.",
                building.addToWorld());

        when(world.containsBuilding(building)).thenReturn(false);
        TileEntity entity = mock(TileEntity.class);
        when(tile.getTileEntity()).thenReturn(entity);
        assertFalse("Building is being placed on a tile entity.",
                building.addToWorld());
    }

    @Test
    public void testRemoveFromWorld() {
        Tile tile = mock(Tile.class);
        when(tile.getTileEntity()).thenReturn(null);
        when(world.getTile(anyInt(), anyInt())).thenReturn(tile);

        // put the building within the world
        building.setLocation(new Point(1, 1));
        when(world.containsBuilding(building)).thenReturn(true);

        building.removeFromWorld();

        verify(tile, times(4 * 3)).removeProperty(TileProperty.IS_BUILDING);
        verify(tile, times(4 * 3)).setTileEntity(null);
        verify(world).removeBuilding(building);

        // should not throw an exception
        when(world.getTile(anyInt(), anyInt())).thenReturn(null);
        building.removeFromWorld();

        when(world.containsBuilding(building)).thenReturn(false);
        building.removeFromWorld();
    }

    @Test
    public void testGetTiles() {
        Tile tile = mock(Tile.class);
        when(world.getTile(anyInt(), anyInt())).thenReturn(tile);

        // put the building within the world
        building.setLocation(new Point(1, 1));

        List<Tile> tiles = building.getTiles();
        assertEquals("incorrect number of tiles were returned", 4 * 3,
                tiles.size());
        assertFalse("Building is within the world but contains null.",
                tiles.contains(null));
        assertTrue("Building does not contain the tile.", tiles.contains(tile));

        // put the building partially outside the world (top left)
        setBuilding(new Point(-1, -3), 3, 4, 1000);

        tiles = building.getTiles();
        assertEquals("incorrect number of tiles were returned", 3 * 4,
                tiles.size());
        assertTrue("Building is outside the world but tiles do not contain"
                + "null.", tiles.contains(null));
        assertTrue("Building does not contain the tile.", tiles.contains(tile));

        // put the building partially outside the world (bottom right)
        setBuilding(new Point(2, 2), 5, 3, 1000);

        tiles = building.getTiles();
        assertEquals("incorrect number of tiles were returned", 5 * 3,
                tiles.size());
        assertTrue("Building is outside the world but tiles do not contain"
                + "null.", tiles.contains(null));
        assertTrue("Building does not contain the tile.", tiles.contains(tile));

        // put the building completely outside the world by changing the world
        // size
        when(world.getWidth()).thenReturn(2);
        when(world.getHeight()).thenReturn(2);

        tiles = building.getTiles();
        assertEquals("incorrect number of tiles were returned", 5 * 3,
                tiles.size());
        assertTrue("Building is outside the world but tiles do not contain"
                + "null.", tiles.contains(null));
        assertFalse("Building should not contain the tile.",
                tiles.contains(tile));

        // building with no size
        setBuilding(new Point(0, 0), 0, 0, 1000);

        tiles = building.getTiles();
        assertEquals("No tiles should be returned", 0, tiles.size());
    }

    @Test
    public void testSize() {
        assertEquals("Size has changed.", 4, building.getWidth());
        assertEquals("Size has changed.", 3, building.getHeight());
    }

    @Test
    public void testHealth() {
        assertEquals("Max health has changed", 1000, building.getMaxHealth());
        assertEquals("Current health should be at maximum", 1000,
                building.getCurrentHealth());

        assertEquals("Returned current health after taking damage is incorrect",
                900, building.damageHealth(100));
        assertEquals("New current health after taking damage is incorrect", 900,
                building.getCurrentHealth());
        assertEquals("Max health has changed after taking damage", 1000,
                building.getMaxHealth());

        assertEquals("Returned current health after healing is incorrect", 950,
                building.healHealth(50));
        assertEquals("New current health after healing is incorrect", 950,
                building.getCurrentHealth());
        assertEquals("Max health has changed after healing", 1000,
                building.getMaxHealth());

        building.fullyRestoreHealth();
        assertEquals("Health was not fully restored", 1000,
                building.getCurrentHealth());

        building.setHealth(0);
        assertEquals("New current health after being set is incorrect", 0,
                building.getCurrentHealth());

        building = new AbstractBuilding(world, 4, 3, "/empty.png") {
            @Override
            public void setSprite(String imageName) {}

            @Override
            public boolean onChangeHealth(long newHealth) {
                return false;
            }
        };
        building.setMaxHealth(1000);

        building.setHealth(500);
        assertEquals("Health should not have changed", 1000,
                building.getCurrentHealth());
    }

    @Test
    public void testContainsPoint() {
        Point location = new Point(1, 3);
        building.setLocation(location);

        // top left (inside)
        Point point1 = new Point(1, 3);
        // bottom right (inside)
        Point point2 = new Point(4.75, 5.33);
        // top right (outside)
        Point point3 = new Point(5.1, 3.25);
        // bottom left (outside)
        Point point4 = new Point(1, 7);

        // left (outside)
        Point point5 = new Point(0.92, 3.5);
        // top (outside)
        Point point6 = new Point(4, 2.8);

        assertTrue("containsPoint [point1] was incorrect",
                building.containsPoint(point1));
        assertTrue("containsPoint [point2] was incorrect",
                building.containsPoint(point2));
        assertFalse("containsPoint [point3] was incorrect",
                building.containsPoint(point3));
        assertFalse("containsPoint [point4] was incorrect",
                building.containsPoint(point4));
        assertFalse("containsPoint [point6] was incorrect",
                building.containsPoint(point5));
        assertFalse("containsPoint [point6] was incorrect",
                building.containsPoint(point6));
    }
}
