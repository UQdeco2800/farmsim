package farmsim.entities.agents;

import farmsim.tasks.AbstractTask;
import farmsim.tasks.AgentRoleTask;
import farmsim.tasks.AnimatedTask;
import farmsim.tasks.MovingTask;
import farmsim.util.Animation.Animation;

/**
 * Class that handles updating the agent for the Travelling state
 *
 * @author hbsteel
 */
public class TravellingAgentState implements AgentState {
    @Override
    public void updateAgent(Agent agent) {
        AbstractTask task = agent.getTask();
        Animation animation = agent.getAnimation();
        if (agent.getLocation().distance(task.getDestination()) < 0.2) {
            // If task is AgentRoleTask give it the level of the worker.
            if (task instanceof AgentRoleTask) {
                ((AgentRoleTask) task).setRoleLevel(
                        agent.getLevelForRole(((AgentRoleTask) task).relatedRole()));
            }
            if (task instanceof AnimatedTask) {
                // Task is animated so setup animation
                animation = ((AnimatedTask) task).getAnimation();
                animation.reset();
                animation.start();
            } else {
                // If not animated reset travelling animation
                animation.reset();
            }
            agent.setAnimation(animation);
            task.startTask();
            agent.setState(new WorkingAgentState());

        } else {
            // Check if task location can move (for following animals)
            if (task instanceof MovingTask) {
                ((MovingTask) task).updateTaskLocation();
            }
            agent.moveTowardTaskLocation();
        }
    }

    @Override
    public String name() {
        return "Travelling";
    }
}
