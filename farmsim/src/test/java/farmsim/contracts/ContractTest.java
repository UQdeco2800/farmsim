package farmsim.contracts;

import static org.junit.Assert.*;
import org.junit.Test;
import farmsim.contracts.Contract;
import farmsim.inventory.*;

public class ContractTest {

    @Test
    public void testNewContract() {
    	final SimpleResourceHandler crops = SimpleResourceHandler.getInstance();
        Contract c = new Contract("Business", crops.apple, 20, 5, 2, 400, 200, "Crop description", 20);
        assertEquals("Business", c.getContractGiver());
        assertEquals(crops.apple, c.getResourceType());
        assertEquals(20, c.getAmount());
        assertEquals(5, c.getRepeatCount());
        c.decrementRepeatCount();
        assertEquals(4, c.getRepeatCount());
        assertEquals(2, c.getInterval());
        assertEquals(400, c.getReward());
        assertEquals(200, c.getPenalty());
        assertEquals("Crop description", c.getDescription());
        assertEquals(20, c.getExpiryDate());
    }
}