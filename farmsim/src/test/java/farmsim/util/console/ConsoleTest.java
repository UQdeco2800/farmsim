package farmsim.util.console;

import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.Text;

/**
 * Test Cases for the util.console package
 */
public class ConsoleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);

    /**
     * Test instance.
     */
    @Test
    public void createInstance() {
        Console console = new Console();
        console.initialize(null, null);
        Assert.assertTrue("Instance created successfully",
                console.equals(Console.getInstance()));
    }

    /**
     * Test Message of The Day.
     */
    @Test
    public void messageOfTheDay() {
        Console console = new Console();
        console.setInitText();

        Assert.assertEquals(console.getConsoleOutputBuffer(),
                "" + "\n" + "           .----.\n" + "         _/aa    \\\n"
                        + "       \\__\\      |\n"
                        + "           )     (      ,\n"
                        + "          /       `-----'\\\n"
                        + "         |         --.   |\n"
                        + "       .-\\        --'    /-.\n"
                        + "  jgs  `'.-_.-_-,_-._-.-'` \n"
                        + "--------------------------------\n\n");
    }

    /**
     * Test that commands exist.
     */
    @Test
    public void commandOperations() {
        Console console = new Console();

        // Can we print to the console
        console.clear();
        console.print("Test");
        Assert.assertEquals("Test", console.getConsoleOutputBuffer());
        console.clear();
        console.println("Test");
        Assert.assertEquals("Test\n", console.getConsoleOutputBuffer());

        // Does the console clear
        console.clear();
        Assert.assertTrue("Clear", "".equals(console.getConsoleOutputBuffer()));

        // Does the buffer store last print
        console.clear();
        console.print("Not in buffer");
        console.print("Im in buffer");
        Assert.assertEquals("Im in buffer", console.getConsoleOutputBuffer());

        // check that we have commands
        Assert.assertTrue("Console has Commands",
                console.getCommandKeys().size() > 0);

        // Send a Command message and expect printout of command
        console.printCommand("Testing");
        Assert.assertEquals("~~  Testing\n", console.getConsoleOutputBuffer());

        // Does Command Help Output Text
        console.clear();
        console.commandHelp();
        Assert.assertTrue("Was anything output",
                console.getConsoleOutputBuffer().length() > 0);
    }

    /**
     * Test if we can correctly handle a known command.
     */
    @Test
    public void handleCommand() {
        Console console = new Console();
        Assert.assertFalse("Testing null parameter",
                console.parseCommand(null));
        Assert.assertTrue("Testing a known util command",
                console.parseCommand("echo testing command"));
    }

    /**
     * Test creation of log elements.
     */
    @Test
    public void createLogTest() {
        List<String> logElement;
        List<String> miscLog = new ArrayList<>();
        String logMsg = "you merly adopted the testing, i was born in it, "
                + "molded by it";

        miscLog.add(0, "MISC");
        miscLog.add(1, logMsg);

        // Test msg == null
        logElement = Console.createLog("MISC", null);
        Assert.assertEquals(0, logElement.size());
        // Test level == null
        logElement = Console.createLog(null, logMsg);
        Assert.assertEquals(miscLog, logElement);
        // Test level == ""
        logElement = Console.createLog("", logMsg);
        Assert.assertEquals(miscLog, logElement);
        // Test level != valid level
        logElement = Console.createLog("Batman", logMsg);
        Assert.assertEquals(miscLog, logElement);
    }


    /**
     * Test Appender.
     */
    @Test
    public void loggerAdd() {
        Console console = new Console();
        console.initialize(null, null);
        org.apache.log4j.Logger.getRootLogger().setLevel(Level.ALL);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Test addition to INFO");
            Assert.assertTrue("Testing INFO log",
                    console.getLog("INFO").size() > 0);
        }
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Test addition to ERROR");
            Assert.assertTrue("Testing ERROR log",
                    console.getLog("ERROR").size() > 0);
        }
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Test addition to WARN");
            Assert.assertTrue("Testing WARN LOG",
                    console.getLog("WARN").size() > 0);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Test addition to DEBUG");
            Assert.assertTrue("Testing DEBUG log",
                    console.getLog("DEBUG").size() > 0);
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Test addition to TRACE");
            Assert.assertTrue("Testing TRACE log",
                    console.getLog("TRACE").size() > 0);
        }
        // Make sure that misc is empty
        Assert.assertTrue("Testing MISC log",
                console.getLog("MISC").size() == 0);

        // Check we do not crash by closing the appender
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.close();
    }

    /**
     * Check the removal of logs.
     */
    @Test
    public void loggerClear() {
        Console console = new Console();
        LOGGER.info("Creating a log to be removed");
        console.clearLog();
        Assert.assertEquals(0, console.getLog("INFO").size());
    }

    /**
     * Check misc logger manually.
     */
    @Test
    public void miscLogger() {
        Console console = new Console();
        // manually add a misc log
        console.clearLog();
        console.addLog("MISC", "testing");
        Assert.assertEquals(1, console.getLog("MISC").size());
        // level null should get into misc
        console.clearLog();
        console.addLog(null, "null level");
        Assert.assertEquals(1, console.getLog("MISC").size());
        // level not in defined levels
        console.clearLog();
        console.addLog("Not in levels", "not a level");
        Assert.assertEquals(1, console.getLog("MISC").size());
    }

    /**
     * Check the format of logs.
     */
    @Test
    public void testLogFormatter() {
        Console console = new Console();
        // check if it can produce a element
        Text testText = console.formatLog("ERROR", "testError");
        Assert.assertEquals("testError", testText.getText());
        testText = console.formatLog("WARN", "testWarn");
        Assert.assertEquals("testWarn", testText.getText());
        testText = console.formatLog("INFO", "testInfo");
        Assert.assertEquals("testInfo", testText.getText());
        testText = console.formatLog("DEBUG", "testDebug");
        Assert.assertEquals("testDebug", testText.getText());
        testText = console.formatLog("TRACE", "testTrace");
        Assert.assertEquals("testTrace", testText.getText());
        testText = console.formatLog("Not int Switch", "testSwitch");
        Assert.assertEquals("testSwitch", testText.getText());
    }


    /* Test Gui Components */

    /**
     * Test loading gui.
     */
    @Test
    public void loadGui() {
        Console console = new Console();
        Assert.assertFalse("no gui attached", console.show());
        // todo javafx test for when a stage attached to console.
    }
}
