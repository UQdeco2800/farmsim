package farmsim.entities.agents;

/**
 * Interface which defines the methods applicable for handling agent state
 *
 * @author hbsteel
 */
public interface AgentState {
	/**
	 * Method for handling agent state behavior 
	 * @param agent agent context
	 */
    public void updateAgent(Agent agent);

    public String name();
}
