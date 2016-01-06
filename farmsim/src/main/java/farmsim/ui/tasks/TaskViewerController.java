package farmsim.ui.tasks;

import farmsim.entities.agents.AgentManager;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.TaskManager;
import farmsim.util.Point;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import farmsim.GameManager;
import farmsim.entities.agents.Agent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages communication between TaskManager and TaskViewer.
 * 
 * @author samsin3
 *
 */
public class TaskViewerController {
    private ArrayList<AbstractTask> singleTasksInView;
    private ArrayList<ArrayList<AbstractTask>> groupTasksInView;
    private HashMap<String, ArrayList<AbstractTask>> recordedTaskMacros;
    private ArrayList<AbstractTask> recordedTasks;
    private boolean recordingTasks;
    private ArrayList<Point> selectedPoints;
    private TaskViewer taskViewer;

    public TaskViewerController() {
        recordingTasks = false;
        singleTasksInView = new ArrayList<AbstractTask>();
        recordedTasks = new ArrayList<AbstractTask>();
        recordedTaskMacros = new HashMap<String, ArrayList<AbstractTask>>();
        groupTasksInView = new ArrayList<ArrayList<AbstractTask>>();
        selectedPoints = new ArrayList<Point>();
    }

    public void setViewer(TaskViewer taskViewer) {
        this.taskViewer = taskViewer;
    }

    public ObservableList<String> getAgentList(AbstractTask task,
            ArrayList<Agent> workerList) {
        ObservableList<String> agentSelectorStrings = FXCollections
                .observableArrayList();
        int index = 1;
        agentSelectorStrings.add("Any");
        workerList.add(0, null);
        for (Agent agent : AgentManager.getInstance().getAgents()) {
            if (agent.eligibleForTask(task)) {
                agentSelectorStrings.add("#" + index + ": " + agent);
                workerList.add(index, agent);
                index++;
            }
        }
        return agentSelectorStrings;
    }

    /**
     * Checks if task at the top of the view is in the manager, and removes it
     * if it isn't.
     */
    public void checkTopTask() {
        if (!singleTasksInView.isEmpty()
                && !TaskManager.getInstance().listTasks()
                        .contains(singleTasksInView.get(0))) {
            removeTaskFromView(singleTasksInView.get(0));
        }
        if (!groupTasksInView.isEmpty()
                && !TaskManager.getInstance().listTasks()
                        .contains(groupTasksInView.get(0).get(0))) {
            removeTaskFromView(groupTasksInView.get(0).get(0));
        }
        return;
    }

    /**
     * Takes a list of tasks(from a TaskPane) and assigns the selected worker to
     * the tasks.
     * 
     * @param tasks
     *            Tasks to be given the selected worker.
     * @param agentIndex
     *            The index of the agent in the agent list.
     */
    public void setWorkerForTasks(ArrayList<AbstractTask> tasks, Agent agent) {
        for (AbstractTask task : tasks) {
            if (agent == null) {
                task.setAllowedWorker(null);
            } else {
                task.setAllowedWorker(agent);
            }
        }
    }

    /**
     * signals for all tasks queued from now until stopRecordingTasks is run to
     * be added to the recordedTasks list for storage as a macro.
     */
    public void startRecordingTasks() {
        recordedTasks = new ArrayList<AbstractTask>();
        recordingTasks = true;
    }

    /**
     * takes all tasks in the recordedTasks list, generates a name for them and
     * saves them as a macro for future requeueing.
     */
    public void stopRecordingTasks() {
        recordingTasks = false;
        String name = "";
        boolean firstName = true;
        for (AbstractTask task : recordedTasks) {
            if (!name.contains(task.getName())) {
                if (firstName) {
                    name = task.getName();
                    firstName = false;
                } else {
                    name += (" + " + task.getName());
                }
            }
        }
        if (recordedTasks.size() > 0) {
            recordedTaskMacros.put(name, recordedTasks);
            taskViewer.updateMacrosDropdown();
        }
        recordedTasks = new ArrayList<AbstractTask>();
    }

    /**
     * Runs the task macro with associated value, that is adds all the tasks in
     * the macro to the task manager.
     * 
     * @param value
     *            Value of the macro to run.
     */
    public void runTaskMacro(String value) {
        if (value != null) {
            for (AbstractTask task : recordedTaskMacros.get(value)) {
                TaskManager.getInstance().addTask(task.copy());
            }
        }
    }

    /**
     * Generates a list of the macros that have been created.
     * 
     * @return The list of macros.
     */
    public ObservableList<String> getMacroList() {
        ObservableList<String> macroNames = FXCollections.observableArrayList();
        for (String key : recordedTaskMacros.keySet()) {
            macroNames.add(key);
        }
        return macroNames;
    }

    /**
     * Adds the specified task to the view, stacking it with any adjacent tasks
     * if possible.
     * 
     * @param task
     *            Task to be added to the view.
     */
    @SuppressWarnings("unchecked")
    public void addTaskToView(AbstractTask task) {
        if (recordingTasks) {
            recordedTasks.add(task.copy());
        }
        for (ArrayList<AbstractTask> group : groupTasksInView) {
            for (AbstractTask groupTask : group) {
                if (groupTask.isAdjacent(task)
                        && groupTask.getClass() == task.getClass()) {
                    taskViewer.addToStackPane(
                            (ArrayList<AbstractTask>) group.clone(), task);
                    group.add(task);
                    checkTopTask();
                    return;
                }
            }
        }
        // check all single tasks to see if they are adjacent
        // to this new task.
        for (AbstractTask viewTask : singleTasksInView) {
            // checks if tasks are adjacent;
            if (viewTask.isAdjacent(task)
                    && viewTask.getClass() == task.getClass()) {
                // tasks are adjacent, so make a group.
                ArrayList<AbstractTask> newGroup = new ArrayList<AbstractTask>();
                newGroup.add(viewTask);
                taskViewer.addToStackPane(newGroup, task);
                newGroup.add(task);
                groupTasksInView.add(newGroup);
                checkTopTask();
                return;
            }
        }

        // check to see if there is already a group of tasks
        // that this task will fit.
        // no group for this task, so add it to the single list.
        singleTasksInView.add(task);
        taskViewer.addTaskPane(task);
        checkTopTask();
    }

    /**
     * Removes the specified task from the view.
     * 
     * @param task
     *            Task to be removed from the view.
     */
    public void removeTaskFromView(AbstractTask task) {
        taskViewer.findAndRemoveTask(task);
        singleTasksInView.remove(task);
        // iterate over list manually to avoid concurrent modification
        // exception.
        for (int i = groupTasksInView.size() - 1; i >= 0; i--) {
            groupTasksInView.get(i).remove(task);
            if (groupTasksInView.get(i).isEmpty()) {
                groupTasksInView.remove(i);
            }
        }
        checkTopTask();
    }

    /**
     * Removes the tasks contained in a TaskPane from the TaskManager.
     * 
     * @param taskPane
     *            The TaskPane containing tasks to be removed.
     */
    public void removeTaskPane(TaskPane taskPane) {
        for (AbstractTask t : taskPane.getTasks()) {
            TaskManager.getInstance().removeTask(t);
        }
    }

    /**
     * Moves a list of tasks to the top of the task manager model and view.
     * 
     * @param tasks
     *            List of tasks to move to the top.
     */
    public void moveToTopPane(ArrayList<AbstractTask> tasks) {
        if (tasks.size() == 1) {
            if (singleTasksInView.contains(tasks.get(0))) {
                singleTasksInView.remove(tasks.get(0));
                singleTasksInView.add(0, tasks.get(0));
            }
            // is a single task
            if (TaskManager.getInstance().listTasks().get(0) != tasks.get(0)) {
                TaskManager.getInstance().moveTask(tasks.get(0), "up");
            }
        } else {
            // is a task stack
            if (groupTasksInView.contains(tasks)) {
                groupTasksInView.remove(tasks);
                groupTasksInView.add(0, tasks);
            }
            for (int i = tasks.size() - 1; i >= 0; i--) {
                AbstractTask task = tasks.get(i);
                ArrayList<AbstractTask> singleTaskList =
                        new ArrayList<AbstractTask>();
                singleTaskList.add(task);
                moveToTopPane(singleTaskList);
            }
        }
    }

    /**
     * Moves a list of tasks to the bottom of the task manager model and view.
     * 
     * @param tasks
     *            List of tasks to move to the bottom.
     */
    public void moveToBottomPane(ArrayList<AbstractTask> tasks) {
        if (tasks.size() == 1) {
            if (singleTasksInView.contains(tasks.get(0))) {
                singleTasksInView.remove(tasks.get(0));
                singleTasksInView.add(tasks.get(0));
            }
            // is a single task
            if (TaskManager.getInstance().listTasks().get(0) != tasks.get(0)) {
                TaskManager.getInstance().moveTask(tasks.get(0), "down");
            }
        } else {
            // is a task stack
            if (groupTasksInView.contains(tasks)) {
                groupTasksInView.remove(tasks);
                groupTasksInView.add(tasks);
            }
            for (AbstractTask task : tasks) {
                ArrayList<AbstractTask> singleTaskList =
                        new ArrayList<AbstractTask>();
                singleTaskList.add(task);
                moveToBottomPane(singleTaskList);
            }
        }
    }

    public void updateTaskDropdowns() {
        taskViewer.updateAgentDropdowns();
    }

    public void addToSelection(ArrayList<Point> points) {
        selectedPoints.addAll(points);
        GameManager.getInstance().setSelection(selectedPoints);
    }

    public void removeFromSelection(ArrayList<Point> points) {
        selectedPoints.removeAll(points);
        GameManager.getInstance().setSelection(selectedPoints);
    }
}
