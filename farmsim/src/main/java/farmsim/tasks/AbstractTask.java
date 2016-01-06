package farmsim.tasks;

import java.util.Observable;

import farmsim.entities.agents.Agent;
import farmsim.entities.machines.MachineType;
import farmsim.entities.tools.ToolType;
import farmsim.tiles.Tile;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * AbstractTask is a framework for Tasks to be built upon.
 * 
 * @author Leggy
 *
 */
public abstract class AbstractTask extends Observable {

    protected Point location;

    private boolean complete;
    private boolean inProgress;

    private long startTime;
    private long endTime;
    private String name;
    private String id;
    protected World world;
    private Agent worker;
    private Agent allowedWorker;
    private ToolType requiredTool = null;
    private MachineType requiredMachine = null;
    private ToolType bonusTool = null;
    private MachineType bonusMachine = null;
    private boolean conflictable = true;

    protected Tile tile;
    
    // 
    private static final int TOOL_BONUS = 2;

    /**
     * The duration of the task, in milliseconds.
     */
    protected int duration;
    private int baseDuration;
    
    /**
     * Creates a new Task.
     *
     * @param point
     *            A Point object representing this Task's location on the map.
     * @param duration
     *            The time (in milliseconds) this task takes to complete.
     * @param name
     *            The name of the task for the task queue.
     */
    public AbstractTask(Point point, int duration, World world,
            String name, String id) {
        this.location = new Point(point);
        this.name = name;
        this.duration = duration;
        this.baseDuration = duration;
        this.complete = false;
        this.inProgress = false;
        this.startTime = -1;
        this.endTime = -1;
        this.world = world;
        this.tile = world.getTile(location);
        this.worker = null;
        this.allowedWorker = null;
        this.id = id;
    }
    
    /**
     * Returns duplicate of task.
     */
    public abstract AbstractTask copy();
    
    /**
     * Sets the task's worker. If the worker has a tool that gives a bonus,
     * the duration of the task is decreased according to the bonus.
     * @param newWorker
     *          Worker that is working on the task
     */
    public void setWorker(Agent newWorker) {
    	worker = newWorker;
    	ToolType workerTool = worker.getToolType();
    	if ((workerTool != null) && (workerTool == bonusTool)) {
    	    duration = baseDuration / TOOL_BONUS;
    	}
        setChanged();
        notifyObservers("setWorker");
    }
    
    /**
     * Gets worker currently working on task.
     * @return worker currently working on task.
     */
    public Agent getWorker() {
    	return worker;
    }
    
    /**
     * Gets worker set to be allowed to work on the task.
     * @return
     *       Worker allowed to work on the task.
     */
    public Agent getAllowedWorker() {
        return allowedWorker;
    }
    
    /**
     * Sets worker allowed to work on the task.
     * @param worker
     *          Worker to be allowed to work on the task.
     */
    public void setAllowedWorker(Agent worker) {
        this.allowedWorker = worker;
        setChanged();
        notifyObservers("allowedWorker");
    }
    
    /**
     * Returns a Point object representing the Task's location on the map.
     * @return Point
     *          A Point representing this Task's location.
     */
    public final Point getLocation() {
        return new Point(location);
    }
    
    /**
     * Returns a Point object representing the point from which the Agent should
     *      perform the task.
     * @return Point
     *      A point representing the location where the Agent should complete
     *      the task.
     */
    public Point getDestination() {
        return new Point(location);
    }
    
    /**
     * Takes an AbstractTask as an input and determines whether the given Task
     * is adjacent to this AbstractTask, where adjacent means either above,
     * below, to the left or to the right of the Tile the task occurs on.
     * @param task
     *          The AbstractTask to test this Task's adjacency against.
     * @return
     *          Returns true if tasks are adjacent and false otherwise.
     */
    public boolean isAdjacent(AbstractTask task) {
        double tx = this.getLocation().getX();
        double ty = this.getLocation().getY();
        double vx = task.getLocation().getX();
        double vy = task.getLocation().getY();

        return (tx == vx && ((ty == vy - 1) || (ty == vy + 1))
                || (ty == vy && ((tx == vx - 1) || (tx == vx + 1))));
    }
    
    /**
     * Returns a boolean indicating if this task has already been completed or
     * not.
     * @return boolean
     *          if the task is complete, return true, and false otherwise.
     */
    public final boolean isComplete() {
        if (complete) {
            return complete;
        }

        if (System.currentTimeMillis() > endTime) {
            postTask();
            inProgress = false;
            complete = true;
        }
        return complete;
    }
    
    /**
     * Returns true if this task has been started and is yet to be completed,
     * and false otherwise.
     * @return boolean
     *          Returns true if the task has been started and is yet to be
     *          completed, and false otherwise.
     */
    public final boolean isInProgress() {
        return inProgress;
    }

    /**
     * Returns the duration, which is the time in ms that this tasks takes to
     * complete from start to finish.
     * 
     * @return duration
     *          the time that this task takes to complete, in ms.
     */
    public final int getDuration() {
        return duration;
    }

    /**
     * Returns a string representing the task's name, which is a user friendly
     * string describing the action taking place in human readable words.
     * @return name
     *          a human readable string representing this Task.
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Returns a string used for internal identification of task type
     * @return id
     *         string identifying type of the task
     */
    public final String id() {
        return id;
    }

    /**
     * This function handles the activation of a Task object, and is called by
     * an Agent when they are assigned a Task from the queue. calls preTask().
     */
    public final void startTask() {
        inProgress = true;
        startTime = System.currentTimeMillis();
        endTime = startTime + duration;
        preTask();
        setChanged();
        notifyObservers("started");
    }

    /**
     * Runs when a task has been started.
     */
    public abstract void preTask();

    /**
     * Runs after a task has been completed.
     */
    public abstract void postTask();

    private boolean test = false;
    public boolean getTest(){
        return test;
    }


    /**
     * Checks to ensure this task is still achievable.
     * 
     * @return Returns true if the task is still achievable, false otherwise.
     */
    public boolean isValid() {
        return true;
    }
    
    /**
     * To String method
     * @return string representation of task
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Sets tool type required to complete the task.
     * @param tool
     *          ToolType required to complete the task.
     */
    protected void setRequiredTool(ToolType tool) {
        this.requiredTool = tool;
    }
    
    /**
     * Returns tool type required to complete the task.
     * @return ToolType required to complete the task.
     */
    public ToolType getRequiredTool() {
        return requiredTool;
    }
    
    /**
     * Sets machine required to complete the task.
     * @param machine
     *          Machine required to complete the task.
     */
    protected void setRequiredMachine(MachineType machine) {
        this.requiredMachine = machine;
    }
    
    /**
     * Returns machine required to complete the task.
     * @return MachineType required to complete the task.
     */
    public MachineType getRequiredMachine() {
        return requiredMachine;
    }
    
    /**
     * Sets tool that improves completion time of task.
     * @param tool
     *          ToolType that improves completion time.
     */
    protected void setBonusTool(ToolType tool) {
        this.bonusTool = tool;
    }
    
    /**
     * Returns tool type that improves completion time of task.
     * @return ToolType that improves completion time.
     */
    public ToolType getBonusTool() {
        return bonusTool;
    }
    
    /**
     * Sets machine that improves completion time of task.
     * @param tool
     *          MachineType that improves completion time.
     */
    protected void setBonusMachine(MachineType machine) {
        this.bonusMachine = machine;
    }
    
    /**
     * Returns machine type that improves completion time of task.
     * @return MachineType that improves completion time.
     */
    public MachineType getBonusMachine() {
        return bonusMachine;
    }
    
    /**
     * Sets the task to be able to happen with other tasks on the same
     * location.
     */
    protected void setNoConflicts() {
        conflictable = false;
    }
    
    /**
     * Returns true if the task can happen with other tasks on the same
     * location, false if it can't.
     * @return if the task can happen with other tasks on the same location.
     */
    public boolean canConflict() {
        return conflictable;
    }
    

    public void setTest(boolean test) {
        this.test = test;
    }
}
