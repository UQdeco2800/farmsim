package farmsim.contracts;

import static org.junit.Assert.*;
import farmsim.world.*;
import farmsim.contracts.ContractGenerator;
import org.junit.Test;
import org.junit.Before;

public class ContractGeneratorTest {
	
	private World world;
	private ContractGenerator contractGenerator;
	
    @Before
    public void createWorld() {
        world = new World("world", 30, 30, 1, 1);
        WorldManager mgr = WorldManager.getInstance();
        mgr.setWorld(world);
		contractGenerator = mgr.getWorld().getContractGenerator();
    }

	@Test
	public void test() {
		Contract c = contractGenerator.generatePreMadeContract();
		assertNotNull(c);
		assertNotNull(c.getContractGiver());
		assertNotNull(c.getResourceType());
		assertTrue(c.getAmount() > 0);
		assertTrue(c.getRepeatCount() > 0);
		assertTrue(c.getInterval() > 0);
		assertTrue(c.getReward() >= 0);
		assertTrue(c.getPenalty() >= 0);
        assertNotNull(c.getDescription());
        assertTrue(c.getExpiryDate() > 0);
	}

}