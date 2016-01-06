package farmsim.wages;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for wages.
 *
 * @author BlakeEddie
 */
public class WageTest {
    // lower bounds
    Wage testWage1 = new Wage(100, 1);
    // upper bounds
    Wage testWage2 = new Wage(100, 5);
    // out of bounds low
    Wage testWage3 = new Wage(100, 0);
    // out of bounds high
    Wage testWage4 = new Wage(100, 6);

    // string constructors should equate to 100
    // lower bounds
    Wage testWage1b = new Wage("500", "5", 1);
    // upper bounds
    Wage testWage2b = new Wage("500", "5", 5);
    // out of bounds low
    Wage testWage3b = new Wage("500", "5", 0);
    // out of bounds high
    Wage testWage4b = new Wage("500", "5", 6);
    // comparetor for change wage
    Wage testWage5b = new Wage("500", "5", 1);
    /**
     * test to see if the constructor set wages correctly.
     */
    @Test
    public void testConstructorWage() {
        assertEquals("wage1 unchanged expected 100", 100, testWage1.getWage());
        assertEquals("wage2 unchanged expected 100", 100, testWage2.getWage());
        assertEquals("wage3 unchanged expected 100", 100, testWage3.getWage());
        assertEquals("wage4 unchanged expected 100", 100, testWage4.getWage());

        assertEquals("wage1b unchanged expected 100", 100, testWage1b.getWage());
        assertEquals("wage2b unchanged expected 100", 100, testWage2b.getWage());
        assertEquals("wage3b unchanged expected 100", 100, testWage3b.getWage());
        assertEquals("wage4b unchanged expected 100", 100, testWage4b.getWage());

    }


    /**
     * test to see if the constructor set happiness correctly.
     */
    @Test
    public void testConstructorHappiness() {
        /*
         * checking correct happiness is quite difficult as it will change with
         * what val of money becomes
         */
        assertEquals("wage1 happiness unchanged level 1 happy:5", 5,
                testWage1.getHappiness());


        // all out of bounds happiness should be the same as 5
        assertEquals("out of bound don't equal the same as 5",
                testWage2.getHappiness(), testWage3.getHappiness());
        assertEquals("out of bound don't equal the same as 5",
                testWage2.getHappiness(), testWage4.getHappiness());

        assertEquals("wage2 happiness != 1", 1, testWage2.getHappiness());

        // the worker
        assertEquals("wage1b happiness != 5", 5, testWage1b.getHappiness());
        assertEquals("wage2b happiness != 5", 5, testWage2b.getHappiness());
        assertEquals("wage3b happiness != 5", 5, testWage3b.getHappiness());
        assertEquals("wage4b happiness != 5", 5, testWage4b.getHappiness());
    }


    /**
     * test to see if we can set wages incorrectly.
     */
    @Test
    public void invalidWageChange() {
        // failed update of Wage
        assertFalse("The Wage change should have failed but didn't",
                testWage1.changeWage(49));
        assertEquals("Wage changed when it shouldn't have", 100,
                testWage1.getWage());

        // successful update of Wage
        assertTrue("Wage change should succeed", testWage1.changeWage(50));
        assertEquals("Wage didn't changed when it should have", 50,
                testWage1.getWage());
        // set Wage back
        testWage1.changeWage(100);

        // failed update of Wage
        assertFalse("The Wage change should have failed but didn't",
                testWage1b.changeWage(49));
        assertEquals("Wage changed when it shouldn't have", 100,
                testWage1b.getWage());

        // successful update of Wage
        assertTrue("Wage change should succeed", testWage1b.changeWage(50));
        assertEquals("Wage didn't changed when it should have", 50,
                testWage1b.getWage());
        // set Wage back
        testWage1b.changeWage(100);

    }

    /**
     * test level update.
     */
    @Test
    public void levelUpdate() {
        // level up (exaggerated)
        testWage1.updateLevel(5);
        assertEquals("after update level 5 happiness in not right",
                testWage2.getHappiness(), testWage1.getHappiness());

        // level down (exaggerated)
        testWage1.updateLevel(1);
        assertEquals("after update level 1 happiness in not right", 5,
                testWage1.getHappiness());

        //level up not the same
        testWage1b.updateLevel(5);
        assertNotEquals("after update level 5b happiness in not right",
                testWage5b.getHappiness(), testWage1b.getHappiness());

        // level down the same
        testWage1b.updateLevel(1);
        assertEquals("after update level 1 happiness in not right", 5,
                testWage1b.getHappiness());
    }

}
