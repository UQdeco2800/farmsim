package farmsim.util.console.handler;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.util.console.Console;

import java.util.List;

/**
 * Peons Command Handler.
 */
public class Peons extends BaseHandler implements BaseHandlerInterface {

    /**
     * Handler for all building console commands.
     */
    public Peons() {
        super();
        this.addCommands();
        this.setName("Peon");
    }

    /**
     * adds commands to the handler.
     */
    private void addCommands() {
        addSingleCommand("peons", "I will list the peons in game");
    }

    /**
     * Handles the incoming command parameters.
     * 
     * @param parameters the incoming command to be processed.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])
                && "peons".equals(parameters[0])) {
            list();
        }
    }

    /**
     * Lists all the peons names.
     */
    private void list() {
        List<Agent> agents = AgentManager.getInstance().getAgents();
        for (Agent agent : agents) {
            Console.getInstance().println("Agent: " + agent.getName());
        }
    }
}
