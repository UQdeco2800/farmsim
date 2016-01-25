package farmsim.util.console;

import farmsim.util.console.handler.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * Console Utility.
 */
public class Console implements Initializable {

    private static final String[] levels =
        {"ERROR", "WARN", "INFO", "DEBUG", "TRACE", "MISC"};
    private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);
    private static Console INSTANCE;
    private Stage gui;
    private List<List<String>> logs;
    private List<BaseHandler> handlers;
    @FXML
    private TextArea consoleOutput;
    @FXML
    private TextField consoleInput;
    @FXML
    private TextFlow loggerOutput;
    @FXML
    private TabPane consolePane;

    private ArrayList<String> commandHistory;
    private int historyIndex;
    private String consoleOutputBuffer = "";

    /**
     * Initialises the util.console with the command definitions loaded.
     */
    public Console() {
        this.handlers = new ArrayList<>();
        this.addHandlers();
        this.commandHistory = new ArrayList<>();

        // setup logger
        logs = new ArrayList<>();
    }

    /**
     * Allows a main instance to be accessed at all times form anywhere.
     *
     * @return a instance of the util.console running at game launch.
     */
    public static Console getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new Console();
            return INSTANCE;
        }
    }

    /**
     * Initialises the internals setting the INSTANCE of the static class.
     *
     * @param location not required.
     * @param resources not required.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        INSTANCE = this;
        this.setInitText();
        if (this.loggerOutput != null) {
            this.loggerOutput.setPrefWidth(Region.USE_COMPUTED_SIZE);
        }
        if (this.consolePane != null) {
            consolePane.setOnKeyReleased(new CommandRecallListener());
        }
    }

    /**
     * Adds the initialised stage to the console for output accessibility.
     *
     * @param gui the stage which the console output too.
     */
    public void attachGui(Stage gui) {
        this.gui = gui;
    }

    public void close() {
        gui.close();
    }

    /**
     * Displays the gui onto the screen.
     */
    public boolean show() {
        if (this.gui != null) {
            gui.show();
            return true;
        } else {
            LOGGER.error("Tried to display gui without being set");
            return false;
        }
    }

    /*
     * Console Functions
     */

    /**
     * Get all the command key's currently loaded.
     *
     * @return Array of objects (Strings) which are the keys.
     */
    public List<String> getCommandKeys() {
        ArrayList<String> commandKeys = new ArrayList<>();
        for (BaseHandler handler : this.handlers) {
            commandKeys.addAll(handler.getCommandKeys());
        }
        return commandKeys;
    }

    /**
     * Adds each handler to the list of handlers to check for the ability to
     * handle an incoming command.
     */
    private void addHandlers() {
        this.handlers.add(new Util());
        this.handlers.add(new Weather());
        this.handlers.add(new farmsim.util.console.handler.World());
        this.handlers.add(new Building());
        this.handlers.add(new Peons());
        this.handlers.add(new Event());
        this.handlers.add(new Pollution());
        this.handlers.add(new PredatorHandler());
        this.handlers.add(new Particle());
        this.handlers.add(new Modifiers());
    }

    /**
     * Catches the input from the event (console input) and sends the command to
     * be acted on.
     *
     * @param event the event that caused the catching.
     */
    @FXML
    public void sendInput(ActionEvent event) {
        if (this.consoleInput != null
                && !this.consoleInput.getText().isEmpty()) {
            this.printCommand(this.consoleInput.getText());
            try {
                this.parseCommand(consoleInput.getText());
                commandHistory.add(consoleInput.getText());
            } catch (Exception e) {
                throw new ConsoleHandlerException(e);
            }
            this.consoleInput.setText("");
            historyIndex = commandHistory.size();
        }
    }

    /**
     * Displays a welcoming msg to the user.
     */
    public void setInitText() {
        this.print("" + "\n" + "           .----.\n" + "         _/aa    \\\n"
                + "       \\__\\      |\n" + "           )     (      ,\n"
                + "          /       `-----'\\\n"
                + "         |         --.   |\n"
                + "       .-\\        --'    /-.\n"
                + "  jgs  `'.-_.-_-,_-._-.-'` \n"
                + "--------------------------------\n\n");
    }

    /**
     * Prints out a command input with a ~~ at the beginning and a new line
     * character at the end of the string.
     *
     * @param command the command to be printed.
     */
    public void printCommand(String command) {
        this.println("~~  " + command);
    }

    /**
     * Gives a structured string in the form of a list definition to be printed.
     */
    public void commandHelp() {
        for (BaseHandler handler : this.handlers) {
            this.println(handler.getCommandHelp());
        }
    }

    /**
     * Clears the output of the util.console.
     */
    public void clear() {
        if (this.consoleOutput != null) {
            this.consoleOutput.setText("");
        }
        this.consoleOutputBuffer = "";
    }

    /**
     * Executes a command depending on the input
     *
     * @param input the users input coming from the util.console window
     */
    public Boolean parseCommand(String input) {
        if (input == null) {
            return false;
        }
        String commandInput = input.trim();
        String[] commandParameters = commandInput.split(" ");

        for (BaseHandler handler : handlers) {
            if (handler.contains(commandParameters[0])) {
                handler.handle(commandParameters);
                return true;
            }
        }
        return false;
    }

    /**
     * Prints to the console.
     */
    public void print(String input) {
        if (this.consoleOutput != null) {
            this.consoleOutput.appendText(input);
        }
        this.consoleOutputBuffer = input;
    }

    /**
     * Prints to console with the new line character at the end.
     */
    public void println(String input) {
        this.print(input + "\n");
    }

    /**
     * Gets the contents of the console buffer (last thing to be printed)
     * through the print function.
     *
     * @return the last string to be printed by the console.
     */
    public String getConsoleOutputBuffer() {
        return consoleOutputBuffer;
    }

    /**
     * Event Listen to listen for when to load previous command in console.
     */
    private class CommandRecallListener implements EventHandler<KeyEvent> {
        /**
         * Handles loading the previous command into the console input.
         * 
         * @param key the key that was pressed.
         */
        @Override
        public void handle(KeyEvent key) {
            if (key.getCode().equals(KeyCode.UP)) {
                historyIndex = Math.max(historyIndex - 1, 0);
                if (!commandHistory.isEmpty()) {
                    consoleInput.setText(commandHistory.get(historyIndex));
                    consoleInput.positionCaret(consoleInput.getLength());
                }
            } else if (key.getCode().equals(KeyCode.DOWN)) {
                historyIndex =
                        Math.min(historyIndex + 1, commandHistory.size());
                if (historyIndex == commandHistory.size()) {
                    consoleInput.setText("");
                    return;
                }
                if (!commandHistory.isEmpty()) {
                    consoleInput.setText(commandHistory.get(historyIndex));
                    consoleInput.positionCaret(consoleInput.getLength());
                }
            }
        }
    }

    /*
     * Logger Functions
     */

    /**
     * Checks if a level is configured for the logger using its string
     * representation.
     *
     * @param proposedLevel the string representation of which could be a level.
     * @return true is the level is an available level, false otherwise.
     */
    public static boolean isLevel(String proposedLevel) {
        for (String level : levels) {
            if (level.equals(proposedLevel)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a log element with a valid level.
     * 
     * @param level the level to which assign if available, otherwise MISC.
     * @param msg the message to store.
     * @return an empty array if no msg was passed, otherwise a 2 element
     *         ArrayList with 0->Level, 1->Msg
     */
    public static List<String> createLog(String level, String msg) {
        List<String> log = new ArrayList<>();
        if (msg == null) {
            return log;
        }
        if (level != null && !"".equals(level) && isLevel(level)) {
            log.add(0, level);
        } else {
            log.add(0, "MISC");
        }
        log.add(1, msg);
        return log;
    }

    /**
     * Add a msg of a level to the log, If the level is not found the msg will
     * be put into the MISC logs.
     *
     * @param level the logger level to store the log.
     * @param msg the message to store.
     * @require this.logs != null
     */
    public void addLog(String level, String msg) {
        List<String> log = createLog(level, msg);
        storeLog(log);
        appendLog(log);
    }

    /**
     * Returns a list of all msgs stored under a log level.
     *
     * @param level the log level.
     * @return A list containing the logs stored. If no logs stored or key
     *         invalid returns a empty list.
     */
    public List<String> getLog(String level) {
        ArrayList<String> keyLogs = new ArrayList<>();
        for (List<String> log : logs) {
            if (log.get(0).equals(level)) {
                keyLogs.add(log.get(1));
            }
        }
        return keyLogs;
    }

    /**
     * Adds the log to the stored list of logs for storage and history reasons.
     * 
     * @param log to be added to the history.
     * @require log != null
     */
    private void storeLog(List<String> log) {
        this.logs.add(log);
    }

    /**
     * Clears the Output Log and history of logs.
     *
     * @require this.logs != null
     */
    public void clearLog() {
        if (this.loggerOutput != null) {
            this.loggerOutput.getChildren().clear();
        }
        this.logs.clear();
    }

    /**
     * Appends a new log to the end of the logger.
     */
    public void appendLog(List<String> log) {
        if (this.loggerOutput != null && log != null && !log.isEmpty()) {
            Text text = formatLog(log.get(0), log.get(1));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    loggerOutput.getChildren().add(text);
                }
            });
        }
    }

    /**
     * Formats the text block to be sent to the logger based on key.
     * 
     * @param level the log level.
     * @param msg the log message.
     * @return A text block that has been formatted according to the level.
     */
    public Text formatLog(String level, String msg) {
        Text text = new Text(msg);
        text.getStyleClass().add("logger" + level);
        switch (level) {
            case "ERROR":
                text.setFill(Color.RED);
                break;
            case "WARN":
                text.setFill(Color.YELLOW);
                break;
            case "INFO":
                text.setFill(Color.CORAL);
                break;
            case "DEBUG":
                text.setFill(Color.GREEN);
                break;
            case "TRACE":
                text.setFill(Color.HONEYDEW);
                break;
            default:
                text.setFill(Color.WHITE);
                break;
        }
        return text;
    }
}
