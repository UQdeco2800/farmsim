package farmsim.events;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class EventManagerTest {

    EventManager eventManager = EventManager.getInstance();

    @Test
    public void getInstance() {
        EventManager tstInstance = EventManager.getInstance();
        Assert.assertTrue("Not getting instance of EventManager",
                eventManager == tstInstance);
    }

    @Test
    public void beginEvent() {
        SpeedChangeEvent event = new SpeedChangeEvent(2, 1000);
        eventManager.beginEvent(event);
        //
        // Finish this test
        //
    }

    @Test
    public void toggleREC() {
        RandomEventCreator rEC = RandomEventCreator.getInstance();
        boolean enabled = rEC.toggleRandomEvents();
        rEC = null;
        if (enabled) {
            Assert.assertFalse("rEC should turn off",
                    eventManager.toggleRanomEvents());
        } else {
            Assert.assertTrue("rEC should turn on",
                    eventManager.toggleRanomEvents());
        }
    }

}
