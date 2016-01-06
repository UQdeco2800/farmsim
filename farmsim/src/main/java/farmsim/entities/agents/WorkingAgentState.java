package farmsim.entities.agents;

import farmsim.tasks.AbstractTask;
import farmsim.tasks.AgentRoleTask;

/**
 * Class that handles updating the agent for the Working state
 *
 * @author hbsteel
 */
public class WorkingAgentState implements AgentState {
    @Override
    public void updateAgent(Agent agent) {
        AbstractTask task = agent.getTask();
        if (task.isComplete()) {
            if (agent.getTool() != null) {
                agent.getTool().decreaseDurability();
                if (agent.getTool().isBroken()) {
                    agent.removeTool();
                }
            }
            // If task implements AgentSkillTask add experience for the
            // completed task.
            if (task instanceof AgentRoleTask) {
                agent.addExperienceForRole(((AgentRoleTask) task).relatedRole(),
                        ((AgentRoleTask) task).experienceGained());
            }
            AgentManager.getInstance().removeRunningTask(task);
            agent.setTask(null);
            AgentManager.getInstance().checkAgentTasks(agent);
        }
    }

    @Override
    public String name() {
        return "Working";
    }
}
