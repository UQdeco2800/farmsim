package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.ui.tasks.TaskViewerController;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.LinkedList;
import java.util.List;

/**
 * TaskManager is a queue-like repository of Tasks (classes deriving from
 * {@link AbstractTask}s).
 * 
 * @author Leggy, samsin3
 *
 */
public class TaskManager {
    private static TaskManager INSTANCE = new TaskManager();
    private TaskViewerController controller;

    private LinkedList<AbstractTask> tasks;
    private LinkedList<AbstractTask> runningTasks;

    public TaskManager() {
        tasks = new LinkedList<AbstractTask>();
        runningTasks = new LinkedList<AbstractTask>();
        controller = null;
    }
    
    /**
     * Assigns the controller object for the TaskManager.
     * @param controller
     * 			TaskViewerController to be the current controller.
     */
    public void setController(TaskViewerController controller) {
    	this.controller = controller;
    	for(AbstractTask task:tasks) {
    		controller.addTaskToView(task);
    	}
    }
   

    /**
     * Public method used to reset the persistent state of the TaskManager, only
     * for test coverage.
     */
    public void refresh() {
        INSTANCE = new TaskManager();
    }
    
    public void setInstance(TaskManager taskManager) {
    	INSTANCE = taskManager;
    }

    /**
     * Returns the instance of {@link TaskManager}.
     * 
     * @return Returns an instance of TaskManager.
     */
    public static TaskManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a task to the task queue and the view.
     * 
     * @param task
     *            AbstractTask to be added to the queue.
     */
    public void addTask(AbstractTask task) {
        tasks.add(task);
        if(controller == null) {
        	return;
        }
        controller.addTaskToView(task);
    }

    /**
     * Returns a list of all the Task objects currently in the queue, both
     * running and waiting Tasks.
     * 
     * @return taskList A list of all Tasks in the game, ordered first by
     *         running status(running tasks first, then waiting tasks) followed
     *         by time in existence (oldest tasks first).
     */
    @SuppressWarnings("unchecked")
    public List<AbstractTask> listTasks() {
        LinkedList<AbstractTask> result = new LinkedList<AbstractTask>();
        result.addAll((LinkedList<AbstractTask>) runningTasks.clone());
        result.addAll((LinkedList<AbstractTask>) tasks.clone());
        return result;

    }

    /**
     * Moves the given task to the top or bottom of the queue.
     * 
     * @require task != null && (direction == "up" || direction =="down")
     * 
     * @param task
     *            the task to move.
     * @param direction
     *            the string name of the direction to move the task in.
     */
    public void moveTask(AbstractTask task, String direction) {
        if (!tasks.contains(task)) {
            return;
        }

        int index = tasks.indexOf(task);
        if (direction == "up" && index > 0) {
            tasks.remove(task);
            tasks.addFirst(task);
        } else if (direction == "down" && index < (tasks.size() - 1)) {
            tasks.remove(task);
            tasks.addLast(task);
        }
    }

    /**
     * Checks if there is another instance of the given task class (eg, a
     * Harvest operation) on the tile that the given task represents. Returns
     * true if there is another instance of the given task class on the tile,
     * and false otherwise.
     * 
     * @param task
     *            The task to check.
     * @return Returns true if there is another instance of the given task class
     *         on the tile, and false otherwise.
     */
    public boolean contains(AbstractTask task) {
        LinkedList<AbstractTask> allTasks = new LinkedList<AbstractTask>();
        allTasks.addAll(tasks);
        allTasks.addAll(runningTasks);
        for (AbstractTask listTask : allTasks) {
            if (task.getClass() == listTask.getClass()
                    && task.getLocation().getX() == listTask.getLocation()
                            .getX()
                    && task.getLocation().getY() == listTask.getLocation()
                            .getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is another task running on the tile that this task is to
     * be run on.
     * 
     * @param task
     *            The task to check.
     * @return Returns true if there is another task currently running on the
     *         tile represented by the given task, and false otherwise.
     */
    public boolean conflictingTasks(AbstractTask task) {
        if (!task.canConflict()) {
            return false;
        }
        for (AbstractTask listTask : runningTasks) {
            if (task.getLocation().getX() == listTask.getLocation().getX()
                    && task.getLocation().getY() == listTask.getLocation()
                            .getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there are waiting tasks in the queue.
     * 
     * @return True if there are tasks in the queue, false otherwise.
     */
    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    /**
     * Removes a task from the model and the view, and cancels it for any Agents
     * currently working on it.
     * 
     * @param task
     *            The task to be removed.
     */
    public void removeTask(AbstractTask task) {
        tasks.remove(task);
        runningTasks.remove(task);
        if(controller == null) {
        	return;
        }
        controller.removeTaskFromView(task);
        AgentManager.getInstance().removeTask(task);
    }

    /**
     * Looks through the task queue and assigns a task based first on adjacency
     * to the worker, and then on time spent in the queue. Removes all invalid
     * tasks if there are no other tasks in the queue.
     * 
     * @param agent
     *            Agent to find a task for.
     * @return Returns either the next valid task in the queue if there is one,
     *         and null otherwise. If there is a task the worker could complete
     *         but is not of the right role and there are no workers of that
     *         role, a ChangeRoleTask is returned for that worker to change into
     *         that role.
     */
    public AbstractTask getTask(Agent agent) {
        AbstractTask task;
        int preferredTaskIndex = -1;
        int index = 0;
        while(!tasks.isEmpty() && index < tasks.size()) {
        	task = tasks.get(index);
        	if((task.getLocation().getX() - 0.5) < agent.getLocation().getX() &&
        		(task.getLocation().getX() + 0.5) > agent.getLocation().getX() &&
        		(task.getLocation().getY()) > agent.getLocation().getY() &&
				(task.getLocation().getY() - agent.getLocation().getY()) < 1.5 &&
				(task.getLocation().getY() - agent.getLocation().getY()) > 0) {
					preferredTaskIndex = index;
					break;
				}
        	index++;
        }
        index = 0;
        while (!tasks.isEmpty() && index < tasks.size()) {
            if (preferredTaskIndex >= 0) {
                task = tasks.get(preferredTaskIndex);
                index--;
                preferredTaskIndex = -1;
            } else {
                task = tasks.get(index);
            }
            if ((task.getAllowedWorker() == null
                    || task.getAllowedWorker() == agent)
                    && agent.hasRequiredTool(task)) {
                if (agent.isRequiredRole(task)) {
                    tasks.remove(task);
                    task.setWorker(agent);
                    if (task.isValid() && !conflictingTasks(task)) {
                        runningTasks.add(task);
                        return task;
                    }
                } else if (!agent.isRoleTypeLocked()
                        && !AgentManager.getInstance()
                                .suitableAgentExists(task)) {
                    Agent.RoleType requiredRole = ((AgentRoleTask) task)
                            .relatedRole();
                    World world = WorldManager.getInstance().getWorld();
                    return new ChangeRoleTask(requiredRole, world, false);
                }
            }
            index++;
        }
        return null;
    }

    /**
     * call to update the connected view's lists of workers.
     */
	public void updateTaskRoles() {
		if(controller == null) {
        	return;
        }
		controller.updateTaskDropdowns();
		
	}
}
