package farmsim.util.console.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * BaseHandler for Console Commands.
 */
public class BaseHandler implements BaseHandlerInterface {

    private Map<String, String> commands;
    protected String name =
            "!BaseHandler: " + "forget to set Handler.setName('Util')";

    /**
     * BaseHandler Class form with other handlers are extended.
     */
    public BaseHandler() {
        commands = new TreeMap<>();
    }

    /**
     * String representation fo the handler.
     * 
     * @return the string representation of the handler.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Sets the name of the handler.
     * 
     * @param name of the handler.
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Allows a single command to be added to the handler.
     * 
     * @param key the command key.
     * @param info the help description.
     */
    protected void addSingleCommand(String key, String info) {
        if (key != null && info != null && !"".equals(key)) {
            this.commands.put(key, info);
        }
    }

    /**
     * Gets all command keys in the handler.
     * 
     * @return List of keys which correspond to the handlers command keys.
     */
    public List<String> getCommandKeys() {
        List<String> commandKeys = new ArrayList<>();
        for (String key : this.commands.keySet()) {
            commandKeys.add(key);
        }
        return commandKeys;
    }

    /**
     * The generic getCommandHelp.
     * 
     * @return A structured representation of the functions available
     */
    public String getCommandHelp() {
        String commandHelp = "";
        String suiteName = this.toString();

        for (int i = 0; i < suiteName.length(); i++) {
            commandHelp += "-";
        }
        commandHelp += String.format("%n%s%n", suiteName);
        for (int i = 0; i < suiteName.length(); i++) {
            commandHelp += "-";
        }
        commandHelp += "\n";
        for (String commandKey : this.getCommandKeys()) {
            commandHelp += String.format(">> %s%n%s%n", commandKey,
                    this.commands.getOrDefault(commandKey, "!noDoc"));
        }
        return commandHelp;
    }

    /**
     * Finds if the passed key is a command key in this handler.
     * 
     * @param commandKey the key to check.
     * @return true if the handler contains a command key equal to commandKey.
     */
    public boolean contains(String commandKey) {
        return commandKey != null && this.commands.containsKey(commandKey);
    }

    /**
     * Handler verbal call which will let the Handle class handle a instruction.
     * 
     * @param parameters the incoming command to be processed.
     */
    @Override
    public void handle(String[] parameters) {
        // do nothing as we are base class
    }
}
