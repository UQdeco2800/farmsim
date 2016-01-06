package farmsim.util.console.handler;

import farmsim.util.Leveler;
import farmsim.world.WorldManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.util.console.Console;

/**
 * Peons Command Handler.
 */
public class LevelerHandler extends BaseHandler implements BaseHandlerInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            LevelerHandler.class);

    /**
     * Handler for all building console commands.
     */
    public LevelerHandler() {
        super();
        this.addCommands();
        this.setName("exp");
    }

    private void addCommands() {
        addSingleCommand("exp", "Get experience map");
    }

    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "exp":
                    if (parameters.length == 4 && parameters[1].equals("add")){
                        addExp(parameters);
                    }
                    //String s = Leveler.getInstance().getExperienceMap().toString();
                    String s = WorldManager.getInstance().getWorld()
                            .getLeveler().getExperienceMap().toString();
                    Console.getInstance().println(s);
                    break;
                default:
                    break;
            }
        }
    }

    private void addExp(String[] parameters){
        int exp;
        String crop = parameters[2];
        try {
            exp = Integer.parseInt(parameters[3]);
        } catch (NumberFormatException e){
            exp = 0;
        }
        //Leveler.getInstance().addExperience(crop, exp);
        WorldManager.getInstance().getWorld().getLeveler()
                .addExperience(crop, exp);
    }
}
