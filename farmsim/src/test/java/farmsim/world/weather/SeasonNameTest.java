package farmsim.world.weather;

import farmsim.world.weather.SeasonName;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing the SeasonName enum type.
 *
 */
public class SeasonNameTest {
  
    /**
     * Test that next() correctly cycles through the seasons.
     * @throws Exception if result is incorrect season or invalid season.
     */
    @Test
    public void testNext() throws Exception {
        SeasonName season = SeasonName.SPRING;
        Assert.assertEquals(season.next(), SeasonName.SUMMER);
        season = season.next();
        Assert.assertEquals(season.next(), SeasonName.AUTUMN);
        season = season.next();
        Assert.assertEquals(season.next(), SeasonName.WINTER);
        season = season.next();
        Assert.assertEquals(season.next(), SeasonName.SPRING);
    }
}
