package farmsim.events.statuses;

import org.junit.Assert;
import org.junit.Test;

import farmsim.events.statuses.Status;
import farmsim.events.statuses.StatusName;

/**
 * Tests for the StatusHandler class
 * 
 * @author bobri
 */

public class StatusTest {

    /**
     * Test creating a valid status
     */
    @Test
    public void statusValidInit() {
        Status grunty = new Status(StatusName.VOIDSTATUS, 4);
        Assert.assertEquals("Status should be of name voidstatus",
                StatusName.VOIDSTATUS, grunty.getName());
        Assert.assertEquals("Status should be of level 4, actual", 4,
                grunty.getLvl(), 0.1);
        Assert.assertEquals("Status should be of duration -1", -1,
                grunty.getDuration());
        grunty.setDuration(5000);
        Assert.assertEquals("Status should be of duration 5000", 5000,
                grunty.getDuration());
    }

    /**
     * Test creating a status with an invalid level
     */
    @Test
    public void statusInvalidLevelInit() {
        Status grunty = new Status(StatusName.STRIKE, -2);
        Assert.assertEquals("Status should be of name voidstatus",
                StatusName.VOIDSTATUS, grunty.getName());
        Assert.assertEquals("Status should be level 1", 1, grunty.getLvl(),
                0.1);
    }

    /**
     * Test adding invalid levels
     */
    @Test
    public void statusInvalidLevelAdd() {
        Status grunty = new Status(StatusName.VOIDSTATUS, 1);
        Assert.assertEquals("Value 4 should be returned", 4,
                grunty.changeLvl(4), 0.1);
        Assert.assertEquals("Status should be level 4", 4, grunty.getLvl(),
                0.1);
        Assert.assertEquals("Value 0 should be returned", 0,
                grunty.changeLvl(-5), 0.1);
        Assert.assertEquals("Status should be level 4", 4, grunty.getLvl(),
                0.1);
    }

    /**
     * Test invalid durations
     */
    @Test
    public void statusInvalidDuration() {
        Status grunty = new Status(StatusName.VOIDSTATUS, 1);
        Assert.assertEquals("Status should return 50", 50,
                grunty.setDuration(50));
        Assert.assertEquals("Status should return 0", 0,
                grunty.setDuration(-5));
        Assert.assertEquals("Status duration should be 50", 50,
                grunty.getDuration());
    }
}


