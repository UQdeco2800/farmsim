package farmsim;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import javafx.scene.text.Font;

/**
 * Tests the loading of JavaFx headlessly, if this is fails then others will
 * on jenkins.
 */
public class Javafx {

    /**
     * Tests if javafx is running heedlessly while doing tests.
     * pinging javafx branch.
     */
    @Test
    public void testHeadlessJavafx() {
        Assert.assertTrue(
                System.getProperty("glass.platform", "").equals("Monocle"));
    }
}
