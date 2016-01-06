package farmsim.world;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import farmsim.world.DayNight;
import farmsim.world.World;
import farmsim.world.WorldManager;

public class DayNightTest {
	
	private World world;

    @Before
    public void createWorld() {
        world = new World("world", 30, 30, 1, 1);
        WorldManager mgr = WorldManager.getInstance();
        mgr.setWorld(world);
    }

    @Test
    public void testInit() {
        assertEquals(8, WorldManager.getInstance().getWorld().getTimeManager().getHours());
        assertEquals(1, WorldManager.getInstance().getWorld().getTimeManager().getDays());
        assertEquals(0, WorldManager.getInstance().getWorld().getTimeManager().getWeeks());
        assertEquals(1, WorldManager.getInstance().getWorld().getTimeManager().getMonth());
        assertEquals(0, WorldManager.getInstance().getWorld().getTimeManager().getYears());
        assertTrue(WorldManager.getInstance().getWorld().getTimeManager().isDay());
    }



}
