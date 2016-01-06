package farmsim.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class Array2DTest {

    Array2D<Integer> arr;

    @Before
    public void createArray() {
        arr = new Array2D<>(30, 30);
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 30; x++) {
                arr.initialise(x, y, y * 100 + x);
            }
        }
    }

    @Test
    public void testGetWidth() {
        assertEquals(30, arr.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(30, arr.getHeight());
    }

    @Test
    public void testGet() {
        /* Randomly located values */
        assertEquals((Integer) 2902, arr.get(2, 29));
        assertEquals((Integer) 1416, arr.get(16, 14));
        assertEquals((Integer) 305, arr.get(5, 3));
        /* Boundary conditions */
        assertEquals((Integer) 0, arr.get(0, 0));
        assertEquals((Integer) 2900, arr.get(0, arr.getHeight() - 1));
        assertEquals((Integer) 29, arr.get(arr.getWidth() - 1, 0));
        assertEquals((Integer) 2929,
                arr.get(arr.getWidth() - 1, arr.getHeight() - 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFail() {
        arr.get(40, 50);
    }

    @Test
    public void testSet() {
        arr.set(3, 5, 12345);
        assertEquals((Integer) 12345, arr.get(3, 5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetFail() {
        arr.set(40, 50, new Integer(123));
    }

    @Test
    public void testSetDimensions() {
        arr.setDimensions(40, 40);
        /* Check information isn't lost */
        assertEquals((Integer) 1417, arr.get(17, 14));
        /* Check default value from new array */
        assertEquals((Integer) 0, arr.get(34, 36));
        arr.set(39, 39, new Integer(3939));
        assertEquals((Integer) 3939, arr.get(39, 39));
        arr.setDimensions(20, 20);
        /* Check information isn't lost */
        assertEquals((Integer) 1417, arr.get(17, 14));
    }

}
