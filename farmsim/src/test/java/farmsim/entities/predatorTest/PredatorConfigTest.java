package farmsim.entities.predatorTest;

import farmsim.entities.predators.PredatorConfigLoader;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the predator config loader
 * @author r-portas
 *
 */
public class PredatorConfigTest {
	PredatorConfigLoader configLoader;
	
	@Test
	public void testCreate() {
		configLoader = new PredatorConfigLoader();
		assertTrue(configLoader.getConfig().isEmpty());
	}

	@Test
	public void loadFile() {
		configLoader = new PredatorConfigLoader();
		configLoader.loadFile("src/test/java/farmsim/entities/predatorTest/testConfig.txt");
		
		assertEquals(configLoader.getLoadedCount(), 3);
	}

	@Test
	public void testToString() {
		configLoader = new PredatorConfigLoader();
		configLoader.loadFile("src/test/java/farmsim/entities/predatorTest/testConfig.txt");
	
		assertEquals("{wolf={health=90, speed=0.2}, rabbit={health=60, speed=0.3}, bear={health=100, speed=0.1}}",
				configLoader.toString());
	}

	@Test
	public void testGetAttr() {
		configLoader = new PredatorConfigLoader();
		configLoader.loadFile("src/test/java/farmsim/entities/predatorTest/testConfig.txt");
	
		assertEquals("90",
				configLoader.getPredatorAttribute("wolf", "health"));
	}

	@Test
	public void testGet() {
		configLoader = new PredatorConfigLoader();
		configLoader.loadFile("src/test/java/farmsim/entities/predatorTest/testConfig.txt");
	
		assertEquals(configLoader.getPredator("bear").toString(), "{health=100, speed=0.1}");

		assertEquals(configLoader.getPredator("invalid"), null);

		assertEquals(configLoader.getPredator("wolf").toString(), "{health=90, speed=0.2}");
	}
}
