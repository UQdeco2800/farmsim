package farmsim;

import static org.junit.Assert.*;


import farmsim.render.Drawable;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ViewportTest {
    World world;

    @Before
    public void setupWorld() {
        world = mock(World.class);
        when(world.getWidth()).thenReturn(20);
        when(world.getHeight()).thenReturn(20);
    }

    @Test
    public void testIsVisible() {
        Viewport viewport = new Viewport(
                TileRegister.TILE_SIZE,
                TileRegister.TILE_SIZE,
                world);

        Drawable notVisible = mock(Drawable.class);
        when(notVisible.getWorldX()).thenReturn(20.0);
        when(notVisible.getWorldY()).thenReturn(20.0);

        assertFalse(
                "Drawable should not be visible",
                viewport.isOnScreen(notVisible)
        );

        Drawable visible = mock(Drawable.class);
        when(notVisible.getWorldX()).thenReturn(0.0);
        when(notVisible.getWorldY()).thenReturn(0.0);

        assertTrue(
                "Drawable should be visible",
                viewport.isOnScreen(visible)
        );
    }

    @Test
    public void testSetPosition() {
        Viewport viewport = new Viewport(
                2 * TileRegister.TILE_SIZE,
                2 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setPosition(5, 5);
        assertEquals(
                "Viewport position should be at (5,5)",
                new Point(5, 5),
                viewport.getPosition()
        );

        viewport.setPosition(0, 30);
        assertEquals(
                "Viewport position should be at (0,18)",
                new Point(0, 18),
                viewport.getPosition()
        );

        viewport.setPosition(0, 19);
        assertEquals(
                "Viewport position should be at (0,18)",
                new Point(0, 18),
                viewport.getPosition()
        );

        viewport.setPosition(30, 0);
        assertEquals(
                "Viewport position should be at (18,0)",
                new Point(18, 0),
                viewport.getPosition()
        );

        viewport.setPosition(19, 0);
        assertEquals(
                "Viewport position should be at (18,0)",
                new Point(18, 0),
                viewport.getPosition()
        );

        viewport.setPosition(30, 30);
        assertEquals(
                "Viewport position should be at (18,18)",
                new Point(18, 18),
                viewport.getPosition()
        );

        viewport.setPosition(19, 19);
        assertEquals(
                "Viewport position should be at (18,18)",
                new Point(18, 18),
                viewport.getPosition()
        );

        viewport.setPosition(2, -5);
        assertEquals(
                "Viewport position should be at (2,0)",
                new Point(2, 0),
                viewport.getPosition()
        );

        viewport.setPosition(-5, 10);
        assertEquals(
                "Viewport position should be at (0,10)",
                new Point(0, 10),
                viewport.getPosition()
        );

        viewport.setPosition(-5, -10);
        assertEquals("Viewport position should be at (0,0)",
                new Point(0, 0),
                viewport.getPosition()
        );
    }

    @Test
    public void testGetX() {
        Viewport viewport = new Viewport(
                2 * TileRegister.TILE_SIZE,
                2 * TileRegister.TILE_SIZE,
                world
        );

        assertEquals("Viewport X should be 0", 0, viewport.getX());

        viewport.setPosition(2, 0);
        assertEquals("Viewport X should be 2", 2, viewport.getX());
    }

    @Test
    public void testGetY() {
        Viewport viewport = new Viewport(
                2 * TileRegister.TILE_SIZE,
                2 * TileRegister.TILE_SIZE,
                world
        );

        assertEquals("Viewport Y should be 0", 0, viewport.getY());

        viewport.setPosition(0, 2);
        assertEquals("Viewport Y should be 2", 2, viewport.getY());
    }

    @Test
    public void testMove() {
        Viewport viewport = new Viewport(
                2 * TileRegister.TILE_SIZE,
                2 * TileRegister.TILE_SIZE,
                world
        );

        viewport.move(10, 10);
        assertEquals("Viewport position should be at (10,10)",
                new Point(10, 10),
                viewport.getPosition()
        );

        viewport.move(-2, 3);
        assertEquals("Viewport position should be at (8,13)",
                new Point(8, 13),
                viewport.getPosition()
        );

        viewport.move(4, -1);
        assertEquals("Viewport position should be at (12,12)",
                new Point(12, 12),
                viewport.getPosition()
        );

        viewport.move(-5, -5);
        assertEquals("Viewport position should be at (7,7)",
                new Point(7, 7),
                viewport.getPosition()
        );

        viewport.move(-8, -8);
        assertEquals("Viewport position should be at (0,0)",
                new Point(0, 0),
                viewport.getPosition()
        );

        viewport.move(19, 0);
        assertEquals("Viewport position should be at (18,0)",
                new Point(18, 0),
                viewport.getPosition()
        );

        viewport.move(0, 100);
        assertEquals("Viewport position should be at (18,18)",
                new Point(18, 18),
                viewport.getPosition()
        );
    }

    @Test
    public void testSetWidthTiles() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setWidthTiles(20);
        assertEquals("Width should be 10", 10, viewport.getWidthTiles());

        viewport.setWidthTiles(5);
        assertEquals("Width should be 5", 5, viewport.getWidthTiles());
    }

    @Test
    public void testSetHeightTiles() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setHeightTiles(20);
        assertEquals("Height should be 10", 10, viewport.getHeightTiles());

        viewport.setHeightTiles(5);
        assertEquals("Height should be 5", 5, viewport.getHeightTiles());
    }

    @Test
    public void testSetMaxWidthTiles() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setMaxWidthTiles(5);
        assertEquals("Max width should be 5", 5, viewport.getMaxWidthTiles());

        viewport.setMaxWidthTiles(30);
        assertEquals("Max width should be 20", 20, viewport.getMaxWidthTiles());
    }

    @Test
    public void testSetMaxHeightTiles() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setMaxHeightTiles(5);
        assertEquals("Max height should be 5", 5, viewport.getMaxHeightTiles());

        viewport.setMaxHeightTiles(30);
        assertEquals("Max height should be 20", 20, viewport.getMaxHeightTiles());
    }

    @Test
    public void testSetWidthPixels() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setWidthPixels(20 * TileRegister.TILE_SIZE);
        assertEquals("Width should be 10", 10, viewport.getWidthTiles());

        viewport.setWidthPixels(4 * TileRegister.TILE_SIZE + 0.5);
        assertEquals("Width should be 5", 5, viewport.getWidthTiles());
    }

    @Test
    public void testGetWidthPixels() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setWidthTiles(5);
        assertEquals(
                "Width should be "
                        + Integer.toString(5 * TileRegister.TILE_SIZE),
                5 * TileRegister.TILE_SIZE
                , viewport.getWidthPixels()
        );
    }

    @Test
    public void testSetHeightPixels() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setHeightPixels(20 * TileRegister.TILE_SIZE);
        assertEquals("Height should be 10", 10, viewport.getHeightTiles());

        viewport.setHeightPixels(4 * TileRegister.TILE_SIZE + 0.5);
        assertEquals("Height should be 5", 5, viewport.getHeightTiles());
    }

    @Test
    public void testGetHeightPixels() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setHeightTiles(5);
        assertEquals(
                "Height should be "
                        + Integer.toString(5 * TileRegister.TILE_SIZE),
                5 * TileRegister.TILE_SIZE
                , viewport.getHeightPixels()
        );
    }

    @Test
    public void testSetMaxWidthPixels() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setMaxWidthPixels(4 * TileRegister.TILE_SIZE + 0.23);
        assertEquals("Max width should be 5", 5, viewport.getMaxWidthTiles());

        viewport.setMaxWidthTiles(30 * TileRegister.TILE_SIZE);
        assertEquals("Max width should be 20", 20, viewport.getMaxWidthTiles());
    }

    @Test
    public void testSetMaxHeightPixels() {
        Viewport viewport = new Viewport(
                10 * TileRegister.TILE_SIZE,
                10 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setMaxHeightPixels(4 * TileRegister.TILE_SIZE + 0.23);
        assertEquals("Max height should be 5", 5, viewport.getMaxHeightTiles());

        viewport.setMaxHeightTiles(30 * TileRegister.TILE_SIZE);
        assertEquals("Max height should be 20", 20, viewport.getMaxHeightTiles());
    }

    @Test
    public void testSetSizeTiles() {
        Viewport viewport = new Viewport(
                5 * TileRegister.TILE_SIZE,
                5 * TileRegister.TILE_SIZE,
                world
        );

        viewport.setSizeTiles(2, 2);
        assertEquals("Width should be 2", 2, viewport.getWidthTiles());
        assertEquals("Height should be 2", 2, viewport.getHeightTiles());
    }
}
