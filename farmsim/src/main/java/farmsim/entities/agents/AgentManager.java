package farmsim.entities.agents;

import farmsim.tasks.AbstractTask;
import farmsim.tasks.TaskManager;
import farmsim.tasks.idle.IdleMoveTask;
import farmsim.tasks.idle.IdleTask;
import farmsim.tasks.idle.IdleWaveTask;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * AgentManager is responsible for the managing of Agents (see {@link Agent})
 * including the distribution of their Tasks.
 *
 * @author Leggy
 */
public class AgentManager {

    private static final AgentManager INSTANCE = new AgentManager();

    private List<Agent> agents;
    private TaskManager taskManager;
    private WorldManager worldManager;

    private AgentManager() {
        agents = new ArrayList<>();
        taskManager = TaskManager.getInstance();
        worldManager = WorldManager.getInstance();
    }

    /**
     * Returns the instance of {@link AgentManager}.
     *
     * @return Returns an instance of AgentManager.
     */
    public static AgentManager getInstance() {
        return INSTANCE;
    }

    public void tick() {
        for (int i = 0; i < agents.size(); i++) {
            agents.get(i).tick();
        }
    }

    /**
     * Add agent to the world
     * @param agent agent object 
     */
    public void addAgent(Agent agent) {
        agents.add(agent);
        if (!agent.hasTask()) {
            checkAgentTasks(agent);
        }
    }

    /**
     * Remove running task 
     * @param task task object
     */
    public void removeRunningTask(AbstractTask task) {
        taskManager.removeTask(task);
    }

    /**
     * check if there are any tasks in queue, and assign agent task
     * @param agent agent object
     */
    public void checkAgentTasks(Agent agent) {
        boolean hasIdleTask = agent.hasIdleTask();
        if (!hasIdleTask && agent.hasTask()) {
            return;
        }
        /*
         * If the agent does not already have a task, we'll give them one.
         */
        if (taskManager.hasTasks()) {
            AbstractTask newTask = taskManager.getTask(agent);
            if (newTask != null) {
                agent.setTask(newTask);
                return;
            }
        }
        /*
         * The task manager does not have any tasks, so if agent doesnt have
         * idle task give them one.
         */
        if (!hasIdleTask) {
            setIdleTask(agent);
        }
    }

    /**
     * Set an idle task to agent
     * @param agent agent object
     */
    public void setIdleTask(Agent agent) {
        Random random = new Random();
        World world = worldManager.getWorld();
        IdleTask task = null;
        // Choose Randomly between a waving task and a moving task
        switch (random.nextInt(2)) {
            case 0:
                // If area locked give agent task in area otherwise anywhere
                // in world
                int xCoord;
                int yCoord;
                if (agent.isAreaLocked()) {
                    Rectangle rect = agent.getAreaLock();
                    xCoord = (int) rect.getX();
                    yCoord = (int) rect.getY();
                    if (rect.getWidth() > 0) {
                        xCoord += random.nextInt((int) rect.getWidth());
                        yCoord += random.nextInt((int) rect.getHeight());
                    }
                } else {
                    xCoord = random.nextInt(world.getWidth());
                    yCoord = random.nextInt(world.getHeight());
                }
                task = new IdleMoveTask(xCoord, yCoord,
                                worldManager.getWorld());
                break;
            case 1:
                int tileX = (int) Math.round(agent.getLocation().getX());
                int tileY = (int) Math.round(agent.getLocation().getY());
                task = new IdleWaveTask(tileX, tileY, worldManager.getWorld(),
                        agent.getCurrentRoleType(), agent.getGender());
                break;
        }
        agent.setTask(task);
    }
    
    /**
     * Removes the tasks from any workers that have it.
     * @param task
     *          Task to remove.
     */
    public void removeTask(AbstractTask task) {
        for (Agent a : agents) {
            if (a.getTask().equals(task)) {
                setIdleTask(a);
            }
        }
    }
    
    /**
     * Checks if any agents meet the task's role requirements.
     * 
     * @param task
     *            Task to check if workers meet requirements of.
     * @return True if there is a worker that meets the task's role
     *         requirements, false otherwise
     */
    public boolean suitableAgentExists(AbstractTask task) {
        for (Agent a : agents) {
            if (a.isRequiredRole(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there are new tasks able to be assigned to Agents
     *
     * @return Returns true if there are new tasks, false otherwise.
     */
    public boolean newTasks() {
        return taskManager.hasTasks();
    }

    public List<Agent> getAgents() {
        // TODO: Need to ensure that these agents are not editable.
        return Collections.unmodifiableList(agents);
    }

    /**
     * fire agents based on name
     * assuming no duplicate names
     * returns true if successful
     */
    public boolean removeAgent(String name) {
        name = name.replaceAll("_", " ");
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            if (agent.getName().equals(name)) {
                AbstractTask task = agent.getTask();
                taskManager.removeTask(task);
                agents.remove(agent);
                taskManager.addTask(task);
                return true;
            }
        }
        return false;
    }

    /**
     * changes an agent's wage
     *
     * @return returns true if change is successful
     */
    public boolean changeAgentWage(String name, int newWage) {
        name = name.replaceAll("_", " ");
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            if (agent.getName().equals(name)) {
                return agent.setWage(newWage);
            }
        }
        return false;
    }

}
