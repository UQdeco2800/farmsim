package farmsim.util;

import java.util.ArrayList;
import java.util.HashMap;

import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.world.World;
import farmsim.world.WorldManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.tests.utils.impl.PowerMockIgnorePackagesExtractorImpl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WorldManager.class)
public class PointTest {

    private Point testPoint1;
    private Point testPoint2;
    private Point testPoint3;
    private Point testPoint4;
    private Point testPoint5;

    @Before
    public void createInstances() {
        testPoint1 = new Point(0, 0);
        testPoint2 = new Point(1, 2);
        testPoint3 = new Point(1, 2);
        testPoint4 = new Point(testPoint1);
        testPoint5 = new Point(3, 4);
    }

    /**
     * Testing the {@link Point}'s distance method.
     */
    @Test
    public void distanceTest() {
        // Standard cases
        Assert.assertEquals("Error calculating distance (PT1 -> PT5).", 5,
                testPoint1.distance(testPoint5), 0);
        Assert.assertEquals("Error calculating distance (PT5 -> PT1).", 5,
                testPoint5.distance(testPoint1), 0);

        // Another standard cases
        Assert.assertEquals("Error calculating distance (PT1 -> PT2).",
                Math.sqrt(5), testPoint1.distance(testPoint2), 0);
        Assert.assertEquals("Error calculating distance (PT2 -> PT1).",
                Math.sqrt(5), testPoint2.distance(testPoint1), 0);

        // Boundary cases
        Assert.assertEquals("Error calculating distance (PT1 -> PT1).", 0,
                testPoint1.distance(testPoint1), 0);

        // More boundary cases
        Assert.assertEquals("Error calculating distance (PT2 -> PT3).", 0,
                testPoint2.distance(testPoint3), 0);
        Assert.assertEquals("Error calculating distance (PT3 -> PT2).", 0,
                testPoint3.distance(testPoint2), 0);

        // More boundary cases
        Assert.assertEquals("Error calculating distance (PT1 -> PT4).", 0,
                testPoint1.distance(testPoint4), 0);
        Assert.assertEquals("Error calculating distance (PT4 -> PT1).", 0,
                testPoint4.distance(testPoint1), 0);
    }

    /**
     * Testing the {@link Point}'s toString method.
     */
    @Test
    public void toStringTest() {
        // Standard cases
        Assert.assertEquals("Error calculating string (PT1).", "[0.00, 0.00]",
                testPoint1.toString());
        Assert.assertEquals("Error calculating string (PT2).", "[1.00, 2.00]",
                testPoint2.toString());
        Assert.assertEquals("Error calculating string (PT3).", "[1.00, 2.00]",
                testPoint3.toString());
        Assert.assertEquals("Error calculating string (PT4).", "[0.00, 0.00]",
                testPoint4.toString());
        Assert.assertEquals("Error calculating string (PT5).", "[3.00, 4.00]",
                testPoint5.toString());
    }

    /**
     * Testing the {@link Point}'s equals method.
     */
    @Test
    public void equalsTest() {
        // Standard cases
        Assert.assertTrue("Error calculating equality (PT1 == PT4).",
                testPoint1.equals(testPoint4));
        Assert.assertTrue("Error calculating equality (PT4 == PT1).",
                testPoint4.equals(testPoint1));

        Assert.assertTrue("Error calculating equality (PT2 == PT3).",
                testPoint2.equals(testPoint3));
        Assert.assertTrue("Error calculating equality (PT3 == PT2).",
                testPoint3.equals(testPoint2));

        // Boundary cases
        Assert.assertTrue("Error calculating equality (PT1 == PT1).",
                testPoint1.equals(testPoint1));
        Assert.assertTrue("Error calculating equality (PT2 == PT2).",
                testPoint2.equals(testPoint2));
        Assert.assertTrue("Error calculating equality (PT3 == PT3).",
                testPoint3.equals(testPoint3));
        Assert.assertTrue("Error calculating equality (PT4 == PT4).",
                testPoint4.equals(testPoint4));
        Assert.assertTrue("Error calculating equality (PT5 == PT5).",
                testPoint5.equals(testPoint5));

        Assert.assertFalse("Error calculating equality (PT1 != PT5).",
                testPoint1.equals(testPoint5));
        Assert.assertFalse("Error calculating equality (PT2 != PT5).",
                testPoint2.equals(testPoint5));
        Assert.assertFalse("Error calculating equality (PT3 != PT5).",
                testPoint3.equals(testPoint5));
        Assert.assertFalse("Error calculating equality (PT4 != PT5).",
                testPoint4.equals(testPoint5));

        // Branch coverage cases
        Assert.assertFalse("Error calculating equality (PT4 != (1, 3)).",
                testPoint2.equals(new Point(1, 3)));
        Assert.assertFalse("Error calculating equality (PT4 != (3, 2)).",
                testPoint2.equals(new Point(3, 2)));

        Assert.assertFalse("Error calculating equality (PT1 != ArrayList).",
                testPoint1.equals(new ArrayList<String>()));
        Assert.assertFalse("Error calculating equality (PT1 != HashMap).",
                testPoint1.equals(new HashMap<String, String>()));
    }

    /**
     * Testing the {@link Point}'s hashCode method.
     */
    @Test
    public void hashCodeTest() {
        // Standard cases
        Assert.assertEquals("Hashcode equality error (PT1 == PT4).",
                testPoint1.hashCode(), testPoint4.hashCode());
        Assert.assertEquals("Hashcode equality error (PT2 == PT3).",
                testPoint2.hashCode(), testPoint3.hashCode());

        Assert.assertNotEquals("Hashcode equality error (PT1 != PT2).",
                testPoint1.hashCode(), testPoint2.hashCode());
        Assert.assertNotEquals("Hashcode equality error (PT3 != PT4).",
                testPoint3.hashCode(), testPoint4.hashCode());
    }

    /**
     * Testing the {@link Point}'s moveTowards method.
     */
    @Test
    public void moveTowardTest() {
        WorldManager mockWorldManager = PowerMockito.mock(WorldManager.class);
        World mockWorld = mock(World.class);
        Tile mockTile = mock(Tile.class);
        when(mockTile.getTileEntity()).thenReturn(null);
        when(mockTile.getTileType()).thenReturn(1);
        PowerMockito.mockStatic(WorldManager.class);
        PowerMockito.when(WorldManager.getInstance()).thenReturn(mockWorldManager);
        when(mockWorldManager.getWorld()).thenReturn(mockWorld);
        when(mockWorld.getTile(anyObject())).thenReturn(mockTile);
        when(mockTile.getProperty(TileProperty.PASSABLE)).thenReturn(true);

        // Instant movement test
        Point point = new Point(1.1, 2);
        Assert.assertEquals("Distance calculation error.", 0.1,
                point.distance(testPoint2), 0.0001);
        point.moveToward(testPoint2, 3);
        Assert.assertEquals("Distance calculation error.", 0,
                point.distance(testPoint2), 0.0001);

        // Progressive movement test
        Point target = new Point(5, 5);
        double distanceToMove = 1;
        double distanceToTarget = point.distance(target);

        point.moveToward(target, distanceToMove);
        Assert.assertEquals("Move toward error.", distanceToMove,
                distanceToTarget - point.distance(target), 0.0001);
        distanceToTarget = point.distance(target);

        point.moveToward(target, distanceToMove);
        Assert.assertEquals("Move toward error.", distanceToMove,
                distanceToTarget - point.distance(target), 0.0001);
        distanceToTarget = point.distance(target);

        point.moveToward(target, distanceToMove);
        Assert.assertEquals("Move toward error.", distanceToMove,
                distanceToTarget - point.distance(target), 0.0001);
        distanceToTarget = point.distance(target);
    }

    /**
     * Testing the {@link Point}'s compareTo method.
     */
    @Test
    public void testCompareTo() {
        Point point = new Point(3, 3);

        Point north = new Point(3, 1);
        Point south = new Point(3, 5);
        Point east = new Point(5, 3);
        Point west = new Point(1, 3);

        Point northwest = new Point(1, 1);
        Point northeast = new Point(5, 1);
        Point southwest = new Point(1, 5);
        Point southeast = new Point(5, 5);

        Assert.assertEquals("Comparison error ().", 0, point.compareTo(point));

        Assert.assertEquals("Comparison error (N).", 1, point.compareTo(north));
        Assert.assertEquals("Comparison error (S).", -1,
                point.compareTo(south));
        Assert.assertEquals("Comparison error (E).", -1, point.compareTo(east));
        Assert.assertEquals("Comparison error (W).", 1, point.compareTo(west));

        Assert.assertEquals("Comparison error (NW).", 1,
                point.compareTo(northwest));
        Assert.assertEquals("Comparison error (NE).", 1,
                point.compareTo(northeast));
        Assert.assertEquals("Comparison error (SW).", -1,
                point.compareTo(southwest));
        Assert.assertEquals("Comparison error (SE).", -1,
                point.compareTo(southeast));

    }

}
