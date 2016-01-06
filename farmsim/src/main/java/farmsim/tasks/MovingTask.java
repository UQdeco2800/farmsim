package farmsim.tasks;

/**
 * Interface for tasks that have a variable location
 */
public interface MovingTask {
    /**
     * Method that is called before Agent moves toward a 'MovingTask'
     */
    public void updateTaskLocation();
}
