package farmsim.world.weather;

import farmsim.world.weather.Season;
import farmsim.world.weather.seasons.*;

import org.junit.Assert;
import org.junit.Test;


/**
 * Testing the Season base class.
 *
 */
public class SeasonTest {
    
    /**
     * Test setting the name field, and getting as enum and as string.
     */
    @Test 
    public void setGetNameTest() {
        Season season = new Season(SeasonName.WINTER);
        Assert.assertEquals(SeasonName.WINTER, season.getName());
        Assert.assertEquals("WINTER", season.getNameString());
        season.setName(SeasonName.AUTUMN);
        Assert.assertEquals(SeasonName.AUTUMN, season.getName());
        Assert.assertEquals("AUTUMN", season.getNameString());
    }
    
    /**
     * Test icon paths.
     */
    @Test
    public void testGetIconPath() {
        Season summer = new Summer();
        Season autumn = new Autumn();
        Season winter = new Winter();
        Season spring = new Spring();
        
        Assert.assertEquals("weather/icons/summer64.png", summer.getPath());
        Assert.assertEquals("weather/icons/autumn64.png", autumn.getPath());
        Assert.assertEquals("weather/icons/winter64.png", winter.getPath());
        Assert.assertEquals("weather/icons/spring64.png", spring.getPath());
    }
}
