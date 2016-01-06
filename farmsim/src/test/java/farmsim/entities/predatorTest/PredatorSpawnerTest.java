package farmsim.entities.predatorTest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.*;

import farmsim.entities.predators.PredatorSpawner;
import farmsim.entities.predators.ViewportTranslator;
import farmsim.util.Point;

public class PredatorSpawnerTest {
	ViewportTranslator translator; 
	PredatorSpawner spawner;
	
	@Before
	public void setUp() {
		translator = mock(ViewportTranslator.class);
		
		// We intentionally made the sample space small
		// so we can test the random generator
		when(translator.getRight()).thenReturn(3);
		when(translator.getLeft()).thenReturn(2);
		when(translator.getTop()).thenReturn(4);
		when(translator.getBottom()).thenReturn(5);
		
		spawner = new PredatorSpawner(translator);
	}
	
	@Test
	public void testRight() {
		Point dest = spawner.getSpawnLocation(3);
		assertEquals(dest, new Point(3, 4));
	}
	
	@Test
	public void testLeft() {
		Point dest = spawner.getSpawnLocation(2);
		assertEquals(dest, new Point(2, 4));
	}
	
	
	@Test
	public void testTop() {
		Point dest = spawner.getSpawnLocation(0);
		assertEquals(dest, new Point(2, 4));
	}
	
	@Test
	public void testBottom() {
		Point dest = spawner.getSpawnLocation(1);
		assertEquals(dest, new Point(2, 5));
	}
	
	@Test
	public void testNull() {
		Point dest = spawner.getSpawnLocation(5);
		assertEquals(dest, null);
	}
	
	@Test
	public void testRandomGen() {
		// This list contains all possibilities of the random gen
		ArrayList<Point> possibilities = new ArrayList<Point>();
		possibilities.add(new Point(3, 4));
		possibilities.add(new Point(2, 4));
		possibilities.add(new Point(2, 5));
		
		Point dest;
		for (int i = 0; i < 50; i++) {
			dest = spawner.getRandomSpawnLocation();
			assertTrue(possibilities.contains(dest));
		}
	}
}
