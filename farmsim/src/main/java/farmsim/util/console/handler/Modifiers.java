package farmsim.util.console.handler;

import farmsim.util.Point;
import farmsim.util.console.Console;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.HashMap;

/**
 * WeatherHandler.
 */
public class Modifiers extends BaseHandler implements BaseHandlerInterface {

    /**
     * Weather Command Handler.
     */
    public Modifiers() {
        super();
        this.addCommands();
        this.setName("Modifiers");
    }

    /**
     * adds the commands to the handler.
     */
    private void addCommands() {
        addSingleCommand("hydrateM", "creates a hydrating weather modifier");
        addSingleCommand("burnM", "dries the land to create fires");
        addSingleCommand("purgeM", "removes all modifiers in game");
        addSingleCommand("badApple", "sets all apple tress on fire.");
        addSingleCommand("winterWonderland", "changes tiles to snow");
        addSingleCommand("ringOfFire", "ring of fire (tests circle method)");
    }

    /**
     * Handler for commands of the weather system.
     * 
     * @param parameters the incoming command parameters.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "hydrateM":
                    hydrate();
                    break;
                case "burnM":
                    burn();
                    break;
                case "purgeM":
                    purge();
                    break;
                case "badApple":
                    badApple();
                    break;
                case "winterWonderland":
                    winterWonderland();
                    break;
                case "ringOfFire":
                    ringOfFire();
                    break;
                default:
                    break;
            }
        }
    }

    private void hydrate() {
        WorldManager.getInstance().getWorld().getModifierManager()
                .removeAttributeModifier("all", "Console", new Point(0, 0));
        //setup the options
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Console");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (new Point(40,40)).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "moisture");
        modifierSettings.put("effect", "0.001");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);

    }

    private void burn() {
        Console.getInstance().println("Some people just want to watch the world burn");
        WorldManager.getInstance().getWorld().getModifierManager()
                .removeAttributeModifier("all", "Console", new Point(0, 0));
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Console");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (new Point(40,40)).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "moisture");
        modifierSettings.put("effect", "-0.001");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }

    private void badApple() {
        Console.getInstance().println("It was a bad apple");
        WorldManager.getInstance().getWorld().getModifierManager()
                .removeAttributeModifier("all", "Console", new Point(0, 0));
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "apple1");
        modifierSettings.put("tag", "Console");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (new Point(40,40)).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "fire");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }

    private void ringOfFire() {
        Console.getInstance().println("Some people just want to watch the world burn");
        WorldManager.getInstance().getWorld().getModifierManager()
                .removeAttributeModifier("all", "Console", new Point(0, 0));
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Console");
        modifierSettings.put("position", (new Point(20, 20)).toString());
        modifierSettings.put("radius", "10");
        modifierSettings.put("shape", "circle");
        modifierSettings.put("attribute", "moisture");
        modifierSettings.put("effect", "-0.001");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }

    private void winterWonderland() {
        Console.getInstance().println("You know nothing John Snow");
        WorldManager.getInstance().getWorld().getModifierManager()
                .removeAttributeModifier("all", "Console", new Point(0, 0));
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Console");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (new Point(100, 100)).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "season");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }

    private void purge() {
        //removes all console modifiers
        WorldManager.getInstance().getWorld().getModifierManager()
                .purge("Console");
        WorldManager.getInstance().getWorld().getModifierManager()
                .purge("all");
    }
}
