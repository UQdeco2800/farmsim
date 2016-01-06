package farmsim;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

public class JavaFXUI extends FxRobot {

    @Before
    public void setup() throws Exception {
        FxToolkit.toolkitContext().setSetupTimeoutInMillis(60000);
        FxToolkit.toolkitContext().setLaunchTimeoutInMillis(60000);

        ApplicationTest.launch(FarmSimLauncher.class);
    }

    @After
    public void cleanup() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    public void loadJavaFX() {
        //Im here to force load JavaFX onto the classpath.
    }
}