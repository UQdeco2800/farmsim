package farmsim.util;

import farmsim.tiles.TileRegister;
import farmsim.world.World;
import farmsim.world.WorldManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import farmsim.util.Leveler;

import java.util.HashMap;

/**
 * Tests for the Leveler class
 *
 * @author team-hartmanis
 *
 */
public class LevelerTest {
	// We need a world to be created to create the leveler so do this first
	World w = new World("duckworld", 100, 100, 3, 123456);
	Leveler leveler = w.getLeveler();

	/**
	 * Reset the instance of Leveler before each test
	 */
	@Before
	public void resetInstance() {
		leveler.clearExperienceMap();
	}

	/**
	 * Test that getExperience returns -1 when crop does not exist
	 */
	@Test
    public void testGetExperienceEmpty() {
        Assert.assertEquals(0, leveler.getExperience("Strawberry"));
    }

	/**
	 * Test adding 0xp
	 */
	@Test
	public void testAddNoExperience() {
		Assert.assertEquals(0, leveler.addExperience("Banana", 0));
		Assert.assertEquals(0, leveler.getExperience("Banana"));
	}

	/**
	 * Test adding an arbitrary amount of xp
	 */
	@Test
	public void testAddExperience() {
		Assert.assertEquals(1, leveler.addExperience("Potato", 31));;
		Assert.assertEquals(31, leveler.getExperience("Potato"));
	}

	/**
	 * Test adding an arbitrary amount of xp multiple times in a row
	 */
	@Test
	public void testAddExperienceMany() {
		Assert.assertEquals(1, leveler.addExperience("Banana", 5));
		Assert.assertEquals(1, leveler.addExperience("Banana", 420));
		Assert.assertEquals(1, leveler.addExperience("Banana", 100));
		Assert.assertEquals(1, leveler.addExperience("Banana", 1102));
		Assert.assertEquals(1627, leveler.getExperience("Banana"));
	}

	/**
	 * Test adding varying good/bad xp values to multiple crops
	 */
	@Test
	public void testAddExperienceManyComplex() {
		Assert.assertEquals(1, leveler.addExperience("Banana", 5));
		Assert.assertEquals(0, leveler.getExperience("Potato"));
		Assert.assertEquals(1, leveler.addExperience("Potato", 607));
		Assert.assertEquals(1, leveler.addExperience("Banana", 420));
		Assert.assertEquals(1, leveler.addExperience("Banana", 100));
		Assert.assertEquals(1, leveler.addExperience("Banana", 1102));
		Assert.assertEquals(0, leveler.addExperience("Banana", -100));
		Assert.assertEquals(0, leveler.addExperience("Banana", 0));
		Assert.assertEquals(1627, leveler.getExperience("Banana"));
		Assert.assertEquals(607, leveler.getExperience("Potato"));
	}

	/**
	 * Test adding negative xp should behave as follows:
	 * 1. Add experience returns -1
	 * 2. Experience is unchanged after attempted addition
	 * 3. Crop is not created if it doesn't exist already
	 */
	@Test
	public void testAddNegativeExperience() {
		Assert.assertEquals(0, leveler.addExperience("Apple", -14));;
		Assert.assertEquals(0, leveler.getExperience("Apple"));
		Assert.assertEquals(1, leveler.addExperience("Apple", 55));;
		Assert.assertEquals(55, leveler.getExperience("Apple"));
		Assert.assertEquals(0, leveler.addExperience("Apple", -14));;
		Assert.assertEquals(55, leveler.getExperience("Apple"));
	}

	/**
	 * Test removing a small amount of xp from a larger total
	 */
	@Test
	public void testRemoveSmallExperience() {
		Assert.assertEquals(1, leveler.addExperience("Wheat", 22));
		Assert.assertEquals(1, leveler.removeExperience("Wheat", 3));
		Assert.assertEquals(19, leveler.getExperience("Wheat"));
	}

	/**
	 * Test removing exactly all the xp
	 */
	@Test
	public void testRemoveAllExperience() {
		Assert.assertEquals(1, leveler.addExperience("Wheat", 22));
		Assert.assertEquals(22, leveler.getExperience("Wheat"));
		Assert.assertEquals(1, leveler.removeExperience("Wheat", 22));
		Assert.assertEquals(0, leveler.getExperience("Wheat"));
	}

	/**
	 * Test removing a larger amount of xp than the total
	 */
	@Test
	public void testRemoveLargeExperience() {
		Assert.assertEquals(1, leveler.addExperience("Cotton", 10));;
		Assert.assertEquals(1, leveler.removeExperience("Cotton", 50));;
		Assert.assertEquals(0, leveler.getExperience("Cotton"));
	}

	/**
	 * Test removing negative xp should behave as follows:
	 * 1. removeExperience() returns -1
	 * 2. Experience is unchanged after attempted removal
	 * 3. Crop is not created if it doesn't exist already
	 */
	@Test
	public void testRemoveNegativeExperience() {
		Assert.assertEquals(-1, leveler.removeExperience("Mango", -93));;
		Assert.assertEquals(0, leveler.getExperience("Mango"));
		Assert.assertEquals(1, leveler.addExperience("Mango", 52));;
		Assert.assertEquals(-1, leveler.removeExperience("Mango", -13));
		Assert.assertEquals(52, leveler.getExperience("Mango"));
	}

	/**
	 * Test removing xp from a non-existent crop
	 */
	@Test
	public void testRemoveExperienceEmpty() {
		Assert.assertEquals(0, leveler.removeExperience("Mango", 65));
		Assert.assertEquals(0, leveler.getExperience("Mango"));
	}

	/**
	 * Test adding and removing good/bad xp values from multiple crops
	 */
	@Test
	public void testAddRemoveExperienceComplex() {
		Assert.assertEquals(1, leveler.addExperience("Banana", 40));
		Assert.assertEquals(1, leveler.addExperience("Potato", 35));
		Assert.assertEquals(1, leveler.removeExperience("Potato", 45));
		Assert.assertEquals(1, leveler.addExperience("Potato", 35));
		Assert.assertEquals(-1, leveler.removeExperience("Potato", -1));
		Assert.assertEquals(1, leveler.addExperience("Banana", 80));
		Assert.assertEquals(-1, leveler.removeExperience("Banana", -80));
		Assert.assertEquals(120, leveler.getExperience("Banana"));
		Assert.assertEquals(35, leveler.getExperience("Potato"));
	}

	/**
	 * Test that crops with a certain amount of xp are at a certain level
	 */
	@Test
	public void testExperienceLevels() {
		leveler.addExperience("Strawberry", 55);
		leveler.addExperience("Banana", 1000);
		leveler.addExperience("Wheat", 5000);
		leveler.addExperience("Cotton", 15000);
		leveler.addExperience("Mango", 25000);
		leveler.addExperience("Rice", -1000000);

		Assert.assertEquals(1, leveler.getLevel("Strawberry"));
		Assert.assertEquals(2, leveler.getLevel("Banana"));
		Assert.assertEquals(3, leveler.getLevel("Wheat"));
		Assert.assertEquals(4, leveler.getLevel("Cotton"));
		Assert.assertEquals(5, leveler.getLevel("Mango"));
		Assert.assertEquals(0, leveler.getLevel("Rice"));
	}

	/**
	 * Test clearing the xp map
	 */
	@Test
	public void testClearMap() {
		HashMap<String, Integer> blank = new HashMap<>();

		leveler.addExperience("Banana", 20);
		leveler.clearExperienceMap();
		Assert.assertEquals(0, leveler.getExperience("Banana"));
		Assert.assertEquals(blank, leveler.getExperienceMap());
		Assert.assertFalse(leveler.getExperienceMap().containsKey("Banana"));
	}

	/**
	 * Test getting the experience percentage until next level
	 */
	@Test
	public void testXPPercent() {
		leveler.addExperience("Cotton", 15000);
		leveler.addExperience("Banana", 10000);
		leveler.addExperience("Mango", 0);
		leveler.addExperience("Potato", 876);
		leveler.addExperience("Wheat", 16788);

		Assert.assertEquals(75, leveler.getExperiencePercentage("Cotton"));
		Assert.assertEquals(50, leveler.getExperiencePercentage("Banana"));
		Assert.assertEquals(0, leveler.getExperiencePercentage("Mango"));
		Assert.assertEquals(87, leveler.getExperiencePercentage("Potato"));
		Assert.assertEquals(83, leveler.getExperiencePercentage("Wheat"));
	}

	/**
	 * Test getting the modifier value for a crop that has XP in Leveler
	 */
	@Test
	public void testModifierCropHasXp() {
		leveler.addExperience("Cotton", 50);
		Assert.assertEquals(1.00, leveler.getLevelModifier("Cotton",
				"growthtime"), 0.01);
	}

	/**
	 * Test getting the modifier value for a crop that has no XP in Leveler
	 */
	@Test
	public void testModifierCropNoXp() {
		Assert.assertEquals(1.00, leveler.getLevelModifier("Mango",
				"growthtime"), 0.01);
	}

	/**
	 * Test giving a bad modifier for a crop that has XP
	 */
	@Test
	public void testModifierBadModifierArgHasXp() {
		leveler.addExperience("Banana", 50);
		Assert.assertEquals(-1.00, leveler.getLevelModifier("Banana", "blah"),
				0.01);
	}

	/**
	 * Test giving a bad modifier for a crop that has no XP
	 */
	@Test
	public void testModifierBadModifierArgNoXp() {
		Assert.assertEquals(-1.00, leveler.getLevelModifier("Banana", "foobar"),
				0.01);
	}

	/**
	 * Test getting a modifier for a level 5 crop (check for off by one errors
	 * and general functionality)
	 */
	@Test
	public void testModifierCropLevel5() {
		leveler.addExperience("Sugarcane", 100000);
		Assert.assertEquals(0.50, leveler.getLevelModifier("Sugarcane",
				"waterreq"), 0.01);
	}

	/**
	 * A bunch of in-order and out of order consecutive tests
	 */
	@Test
	public void testModifierComplex() {
		leveler.addExperience("Cotton", 50);
		leveler.addExperience("Banana", 56789);
		Assert.assertEquals(1.00, leveler.getLevelModifier("Cotton",
				"growthtime"), 0.01);
		Assert.assertEquals(0.50, leveler.getLevelModifier("Banana",
				"growthtime"), 0.01);
		Assert.assertEquals(1.00, leveler.getLevelModifier("Cotton",
				"growthtime"), 0.01);
		Assert.assertEquals(-1.00, leveler.getLevelModifier("Cotton",
				"foobar"), 0.01);
		Assert.assertEquals(-1.00, leveler.getLevelModifier("Banana",
				"foobar"), 0.01);
		Assert.assertEquals(1.00, leveler.getLevelModifier("foobar",
				"growthtime"), 0.01);

	}
}