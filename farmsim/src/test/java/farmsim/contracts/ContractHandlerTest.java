package farmsim.contracts;

import static org.junit.Assert.*;

import org.junit.Test;

import farmsim.contracts.Contract;
import farmsim.contracts.ContractHandler;
import farmsim.contracts.ContractGenerator;
import farmsim.world.*;
import org.junit.Before;
import farmsim.inventory.*;

import org.junit.Ignore;

public class ContractHandlerTest {
	
	private World world;
	private ContractHandler availableContracts;
	private ContractHandler activeContracts;

    @Before
    public void createWorld() {
        world = new World("world", 30, 30, 1, 1);
        WorldManager mgr = WorldManager.getInstance();
        mgr.setWorld(world);
		availableContracts = mgr.getWorld().getAvailableContracts();
		activeContracts = mgr.getWorld().getActiveContracts();
    }
	
	@Test
	public void addRemoveTest() {
		final SimpleResourceHandler crops = SimpleResourceHandler.getInstance();
		Contract a = new Contract("Business1", crops.apple, 20, 5, 2, 400, 200, "Crop description", 20);
		Contract b = new Contract("Business2", crops.apple, 20, 5, 2, 400, 200, "Crop description", 20);
		Contract c = new Contract("Business3", crops.apple, 20, 5, 2, 400, 200, "Crop description", 20);
		Contract d = new Contract("Business", crops.apple, 20, 5, 2, 400, 200, "Crop description", 20);
		assertTrue("contract is added", activeContracts.addContract(a));
		assertTrue("contract is added", activeContracts.addContract(b));
		assertTrue("contract is added", activeContracts.addContract(c));
		assertFalse("contract is not added", activeContracts.addContract(d));
		assertTrue("contract is added", availableContracts.addContract(a));
		assertTrue("contract is added", availableContracts.addContract(b));
		assertTrue("contract is added", availableContracts.addContract(c));
		assertFalse("contract is not added", availableContracts.addContract(d));
		assertEquals(2, activeContracts.getDeliveryDate(a));
		availableContracts.removeContract(a);
		availableContracts.removeContract(b);
		availableContracts.removeContract(c);
		assertTrue("table is empty", availableContracts.getContracts().isEmpty());
	}
	
	@Ignore
	@Test
	public void checkContractsTest() {
		final SimpleResourceHandler crops = SimpleResourceHandler.getInstance();
		Contract a = new Contract("Business", crops.apple, 20, 0, 0, 400, 200, "Crop description", 20);
		activeContracts.addContract(a);
		activeContracts.checkContracts();
		assertTrue("table is empty", activeContracts.getContracts().isEmpty());
	}

}
