package farmsim.uiTest;

import farmsim.JavaFXUI;
import farmsim.world.World;
import farmsim.world.WorldManager;
import farmsim.world.weather.WeatherType;
import javafx.scene.input.KeyCode;
import javafx.scene.text.TextFlow;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;



import java.util.concurrent.TimeUnit;

import static org.loadui.testfx.GuiTest.find;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Launches instances of the game to test UI.
 */
public class MainScreen extends JavaFXUI {

    /**
     * In honour of Tom Manderson.
     *
     * In memory of the day @trmanderson added the click to
     * play test to factory.
     */
    @Test
    public void mainUITest() {
        //check the main menu
        testMainMenu();
        startDebugGame();
        disableFarmNoise(); // moo
        testConsole();  
        testTaskAndWorkerManager();
        testWorldSettings();
        testStorage();
    }

    /**
     * Starts the game in debug mode.
     */
    private void startDebugGame() {
        clickOn("#debugBtn");
        sleep(20, TimeUnit.MILLISECONDS);
    }

    /**
     * Tests that all main menu buttons are visible and actively uses some.
     */
    private void testMainMenu() {
        verifyThat("#startBtn", isVisible());
        verifyThat("#helpBtn", isVisible());
        verifyThat("#creditsBtn", isVisible());
        verifyThat("#closeBtn", isVisible());
        verifyThat("#debugBtn", isVisible());
        //click the help and credits menu
        clickOn("#helpBtn");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn(".exit-symbol");
        clickOn("#creditsBtn");
        sleep(10, TimeUnit.MILLISECONDS);
    }

    /**
     * Tests the console and logger then closes the console.
     */
    private void testConsole() {
        verifyThat("#consoleOutput", isVisible());
        verifyThat("#consoleInput", isVisible());
        verifyThat("#consoleInput", hasText(""));
        clickOn("#consoleInput").write("clear");
        type(KeyCode.ENTER);
        sleep(10, TimeUnit.MILLISECONDS);
        verifyThat("#consoleOutput", hasText(""));
        clickOn("#consoleInput").write("help");
        type(KeyCode.ENTER);
        clickOn("#consoleInput").write("clear");
        type(KeyCode.ENTER);

        World world = WorldManager.getInstance().getWorld();
        for (WeatherType type : WeatherType.values()) {
            clickOn("#consoleInput").write("weather-set ");
            write(type.toString().toLowerCase());
            type(KeyCode.ENTER);
            Assert.assertEquals(type, world.getWeatherType());
        }

        //clear the weather (reduces lag)
        clickOn("#consoleInput").write("weather-set default");
        type(KeyCode.ENTER);

        clickOn("#logger");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn("#clearLog");
        TextFlow logger = find("#loggerOutput");
        Assert.assertTrue(logger.getChildren().isEmpty());
        verifyThat("#loggerOutput", isVisible());
        sleep(10, TimeUnit.MILLISECONDS);

        clickOn("#closeConsole");
    }

    /**
     * Opens up the task and worker manager popups.
     */
    private void testTaskAndWorkerManager() {
        clickOn("#tabVisual");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn("#newTaskViewer");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn(".exit-symbol");
        clickOn("#openWorkerManagement");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn(".exit-symbol");
    }

    /**
     * Disables the farm audio. More my sanity than anything.
     */
    private void disableFarmNoise() {
        clickOn("#tabAudio");
        clickOn("#toggleGameSounds");
    }


    /**
     * Tests toggling the options in the world menu.
     */
    private void testWorldSettings() {
        clickOn("#tabWorld");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn("#showBuildingsPopup");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn(".exit-symbol");
        clickOn("#minimapToggle");
        sleep(10, TimeUnit.MILLISECONDS);
        clickOn("#minimapToggle");
        clickOn("#toggleMinimapAgents");
        sleep(10, TimeUnit.MILLISECONDS);
    }

    /**
     * Tests the storage gui system.
     */
    private void testStorage() {
    	clickOn("#tabMain");
    	clickOn("#storage"); 
		sleep(100, TimeUnit.MILLISECONDS); 
		sleep(20, TimeUnit.MILLISECONDS);
		clickOn("#removeButton"); 
		clickOn("#item1-0");
		clickOn("#item1-0"); 
		clickOn("#item1-1"); 
		clickOn("#item1-0"); 
		drag("#item1-0").dropTo("#item1-1"); 
		clickOn("#removeButton"); 
		clickOn("#count").write("a");
		clickOn("#removeButton");
		clickOn("#count").type(KeyCode.BACK_SPACE).write("0");
		clickOn("#removeButton");
		clickOn("#count").type(KeyCode.BACK_SPACE).write("4");
		clickOn("#removeButton");
		clickOn("#count").type(KeyCode.BACK_SPACE).write("1");
		clickOn("#removeButton");
		clickOn("#toolButton");
		clickOn("#item2-0"); 
		clickOn("#item2-0"); 
		clickOn("#item2-0"); 
		clickOn("#removeButton");
		clickOn(".exit-symbol");
    }
}
