package farmsim.pollution;

import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.world.World;
import farmsim.world.WorldManager;

public class PollutionUpdaterTest {

	World world = new World("World", 10, 10, 1, 1);
	Tile t;
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testUpdate() {
		t = world.getTile(0, 0);
		t.setProperty(TileProperty.IS_POLLUTION_SOURCE, 1);
		Assert.assertTrue((int) t.getProperty( TileProperty.IS_POLLUTION_SOURCE) == 1);
		Assert.assertFalse((int) t.getProperty( TileProperty.IS_POLLUTION_SOURCE) == 0);
		WorldManager.getInstance().setWorld(world);
		PollutionUpdater polUp = PollutionUpdater.getInstance();
		for (int i = 0; i < 30; i++) {
			polUp.updateTiles();
		}
		world.setDimensions(12, 12);
		polUp.updateTiles();
		polUp.updateTiles();
	}
}
