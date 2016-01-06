package farmsim.events.statuses;

import org.junit.Assert;
import org.junit.Test;

import farmsim.events.statuses.StatusHandler;
import farmsim.events.statuses.StatusName;

/**
 * Tests for the statusHandler class
 * 
 * @author bobri
 *
 */

public class StatusHandlerTest {

    StatusHandler statusHandler = StatusHandler.getInstance();

    /**
     * Test adding a valid status
     */

    @Test
    public void statusHandlerAddValidEvent() {
        Assert.assertEquals("Return value should be 1", 1,
                statusHandler.addStatus(StatusName.VOIDSTATUS, 2));
        Assert.assertTrue("Status should exist",
                statusHandler.checkStatus(StatusName.VOIDSTATUS));
        Assert.assertTrue("Status should exist",
                statusHandler.checkStatus(StatusName.VOIDSTATUS, 2));
        Assert.assertFalse("Status should not exist",
                statusHandler.checkStatus(StatusName.STRIKE));
        Assert.assertFalse("Status should not exist",
                statusHandler.checkStatus(StatusName.VOIDSTATUS, 1));
        Assert.assertFalse("Status should not exist",
                statusHandler.checkStatus(StatusName.STRIKE, 1));
        Assert.assertEquals("Return value should be 1", 1,
                statusHandler.removeStatus(StatusName.VOIDSTATUS));
    }

    /**
     * Test adding invalid status
     */

    @Test
    public void statusHandlerAddInvalidStatus() {
        Assert.assertEquals("Return value should be 0", 0,
                statusHandler.addStatus(StatusName.VOIDSTATUS, 0));
        statusHandler.addStatus(StatusName.VOIDSTATUS, 1);
        Assert.assertEquals("Return value should be 0", 0,
                statusHandler.addStatus(StatusName.VOIDSTATUS, 1));
        Assert.assertEquals("Return value should be 1", 1,
                statusHandler.removeStatus(StatusName.VOIDSTATUS));
    }

    /**
     * Test Remvoing status'
     */
    @Test
    public void statusHandlerRemoveEvent() {
        Assert.assertEquals("Return value should be 0", 0,
                statusHandler.removeStatus(StatusName.VOIDSTATUS));
        Assert.assertEquals("Return value should be 0", 0,
                statusHandler.removeStatus(StatusName.VOIDSTATUS, 4));
        Assert.assertEquals("Return value should be 1", 1,
                statusHandler.addStatus(StatusName.VOIDSTATUS, 2));
        Assert.assertEquals("Return value should be 1", 1,
                statusHandler.removeStatus(StatusName.VOIDSTATUS, 2));
    }

    /**
     * Test increase/decrease status
     */
    @Test
    public void statusHandlerIncreaseDecrease() {
        statusHandler.addStatus(StatusName.VOIDSTATUS, 1);
        Assert.assertEquals("Return should be 4.5", 4.5,
                statusHandler.increaseStatus(StatusName.VOIDSTATUS, 3.5), 0.01);
        Assert.assertEquals("Return should be 3", 3,
                statusHandler.decreaseStatus(StatusName.VOIDSTATUS, 1.5), 0.01);
        statusHandler.addStatus(StatusName.VOIDSTATUS, 1);
        Assert.assertEquals("Return should be 0", 0,
                statusHandler.increaseStatus(StatusName.VOIDSTATUS, 0), 0.01);
        Assert.assertEquals("Return should be 0", 0,
                statusHandler.decreaseStatus(StatusName.VOIDSTATUS, 0), 0.01);
        Assert.assertEquals("Return should be 0", 0,
                statusHandler.decreaseStatus(StatusName.VOIDSTATUS, 50), 0.01);
        Assert.assertTrue("Status level should be 3",
                statusHandler.checkStatus(StatusName.VOIDSTATUS, 3));
    }
}
