package farmsim.util.console.handler;

import farmsim.util.console.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Utility Command Handler.
 */
public class Util extends BaseHandler implements BaseHandlerInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    /**
     * Handler to handle all util console commands.
     */
    public Util() {
        super();
        this.addCommands();
        this.setName("Util");
    }

    /**
     * Adds the commands to the handler.
     */
    private void addCommands() {
        addSingleCommand("help", "Lists out all the command descriptions.");
        addSingleCommand("clear", "Clears the output window.");
        addSingleCommand("echo", "Echos out the input as an array");
        addSingleCommand("log", "generates a log");
    }

    /**
     * Handles the incoming command parameters.
     * 
     * @param parameters the incoming command to be processed.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "log":
                    log();
                    break;
                case "clear":
                    clear();
                    break;
                case "echo":
                    echo(parameters);
                    break;
                case "help":
                    help();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Runs dummy logs to the logging backend.
     */
    private void log() {
        LOGGER.info("Test of a info log {}", "test");
        LOGGER.debug("Test of a debug log {}", "test");
        LOGGER.error("Test of a error log {}", "test");
        LOGGER.trace("Test of a trace log {}", "test");
        LOGGER.warn("Test of a warn log {}", "test");
    }

    /**
     * clears the console output.
     */
    private void clear() {
        Console.getInstance().clear();
    }

    /**
     * echos the input to console out.
     * 
     * @param parameters the input to be echoed.
     */
    private void echo(String[] parameters) {
        Console.getInstance().println(Arrays.toString(parameters));
    }

    /**
     * generates help text and displays it to the consoles output.
     */
    private void help() {
        Console.getInstance().commandHelp();
    }
}
