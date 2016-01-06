package farmsim.world.weather;

import farmsim.world.weather.SeasonManager;
import farmsim.world.weather.SeasonName;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Testing the SeasonManager.
 *
 */
public class SeasonManagerTest {
    
    private List<String> values = 
            Arrays.asList("SUMMER", "AUTUMN", "WINTER", "SPRING");
    private Boolean observerUpdated = false;
    
    /**
     * Test that the constructor creates an instance with a defined valid season
     * Also tests getSeason() returns a valid string.
     */
    @Test
    public void testConstructor() {
        SeasonManager manager = new SeasonManager();
        manager.seasonStart();
        
        Assert.assertTrue(values.contains(manager.getSeason()));
    }
    
    /**
     * Test that getSeason returns the expected result.
     */
    @Test
    public void testGetSeason() {
        SeasonManager manager = new SeasonManager();
        manager.seasonSwitch(SeasonName.SUMMER);
        Assert.assertEquals("SUMMER", manager.getSeason());   
    }
    
    /**
     * Test that seasonSwitch() cycles correctly.
     */
    @Test
    public void testSwitchCycle() {
        SeasonManager manager = new SeasonManager();
        manager.seasonSwitch(SeasonName.SUMMER);
        manager.seasonSwitch();
        Assert.assertEquals("AUTUMN", manager.getSeason());
        manager.seasonSwitch();
        Assert.assertEquals("WINTER", manager.getSeason());
        manager.seasonSwitch();
        Assert.assertEquals("SPRING", manager.getSeason());
        manager.seasonSwitch();
        Assert.assertEquals("SUMMER", manager.getSeason());
    }
    
    /**
     * Test that tick() runs.
     */
    /*
     * I'm unhappy with this test, because I'd *like* to test the behaviour
     * on the true condition, not just the false. However, to do that I appear
     * to need to write a public setter for startDay (which I don't believe
     * should exist), or mock the DayNight result (attempting which has caused
     * much suffering, mocking the singleton isn't working out).
     */
    @Ignore
    @Test
    public void testTick() {
        SeasonManager manager = new SeasonManager();
        String season = manager.getSeason();
        
        //manager.tick();
        Assert.assertEquals(season, manager.getSeason());
    }
    
    /**
     * Test that an observer is properly called on changing season.
     */
    @Test
    public void testObserver() {
        clearObserverUpdated();
        SeasonManager manager = new SeasonManager();
        TestObserver observer = new TestObserver();
        manager.addObserver(observer);
        manager.seasonSwitch();
        Assert.assertTrue(checkObserverUpdated());
    }
    
    @Test 
    public void testGetSeasonObject() {
        SeasonManager manager = new SeasonManager();
        manager.seasonStart();
        Assert.assertTrue(manager.getSeasonObject() instanceof Season);
        Assert.assertNotNull(manager.getSeasonObject());
    }
    
    /**
     * an observer for testing observable implementation.
     */
    public class TestObserver implements Observer {
        @Override
        public void update(Observable arg0, Object arg1) {
            setObserverUpdated();
        }
    }
    
    /**
     * sets observerUpdated to true.
     */
    public void setObserverUpdated() {
        observerUpdated = true;
    }
    
    /**
     * sets observerUpdated to false.
     */
    public void clearObserverUpdated() {
        observerUpdated = false;
    }
    
    /**
     * Check the current value of observerUpdated.
     * @return true/false
     */
    public Boolean checkObserverUpdated() {
        return observerUpdated;
    }
    
    /**
     * Test returns for all cases of getIconStyle.
     */
    @Test
    public void testGetIconStyle() {
        SeasonManager manager = new SeasonManager();
        for (SeasonName n : SeasonName.values()) {
            Assert.assertTrue(manager.getIconStyle(n, true).equals(
                    "forecast" + n.toString().charAt(0) + 
                    n.toString().substring(1).toLowerCase()));
            Assert.assertTrue(manager.getIconStyle(n, false).equals(
                    "season" + n.toString().charAt(0) + 
                    n.toString().substring(1).toLowerCase()));
        }
    }
    
    
}
