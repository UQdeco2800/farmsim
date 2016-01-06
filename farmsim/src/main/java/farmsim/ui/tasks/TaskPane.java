package farmsim.ui.tasks;

import farmsim.entities.agents.Agent;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.AgentRoleTask;
import farmsim.util.Point;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * TaskPane is a BorderPane representing and containing information about a
 * Task, to be displayed in a {@link TaskViewer} (classes deriving from
 * {@link AbstractTask}).
 * 
 * @author samsin3
 *
 */
public class TaskPane extends BorderPane {

    private ArrayList<AbstractTask> tasks;
    private ArrayList<AbstractTask> allTasks;

    private boolean selected = false;
    private boolean isStack = false;
    private boolean started = false;

    private Label taskLocation;
    private ProgressBar progressBar;
    private Label taskSize;
    private Label workerText;
    private Label taskState;
    private ImageView workerImage;
    
    private ChoiceBox<String> workerDropdown;
    private ArrayList<Agent> workerList;
    private TaskViewerController controller;
    
    /**
     * @param task
     *            The task this TaskPane should be based off of. In the case of
     *            a non stacked task pane, this should be the task to be shown.
     *            In the case of a stacked task pane, this task should be
     *            representative of the type of task this taskpane will have (eg
     *            a harvest task should be used as the constructor of a task
     *            pane that is to show harvest tasks.
     * @param taskViewerController
     *            Controller class for the containing TaskViewer.
     */
    public TaskPane(AbstractTask task,
            TaskViewerController taskViewerController) {
        super();
        controller = taskViewerController;
        task.addObserver(new TaskObserver());
        tasks = new ArrayList<AbstractTask>();
        allTasks = new ArrayList<AbstractTask>();
        tasks.add(task);
        allTasks.add(task);

        this.getStyleClass().add("task-pane");
        this.getStyleClass()
                .add("task-" + tasks.get(0).id());

        VBox textContainer = new VBox(2);
        this.setCenter(textContainer);

        Label taskName = new Label(task.getName());
        taskName.getStyleClass().add("task-name");
        textContainer.getChildren().add(taskName);
        
        HBox firstLine = new HBox();
        taskSize = new Label();
        taskSize.getStyleClass().add("body-text");
        firstLine.getChildren().add(taskSize);
        taskState = new Label(" - Queued");
        taskState.getStyleClass().add("body-text");
        firstLine.getChildren().add(taskState);
        textContainer.getChildren().add(firstLine);
        
        progressBar = new ProgressBar();
        progressBar.setProgress(0);
        progressBar.setPrefSize(199, 10);
        textContainer.getChildren().add(progressBar);
        
        taskLocation = new Label("");
        taskLocation.getStyleClass().add("body-text");
        textContainer.getChildren().add(taskLocation);
        
        BorderPane layoutPane = new BorderPane();
        VBox leftColumn = new VBox(2);
        leftColumn.setPrefWidth(157);
        
        Label toolText;
        if (task.getRequiredTool() != null) {
            toolText = new Label("Required tool: " 
                    + task.getRequiredTool());
        } else if (task.getBonusTool() != null) {
            toolText = new Label("Helpful tool: " + task.getBonusTool());
        } else {
            toolText = new Label("Required tool: any");
        }
        toolText.getStyleClass().add("body-text");
        leftColumn.getChildren().add(toolText);
        
        Label requiredRole;
        if ((task instanceof AgentRoleTask)
                && ((AgentRoleTask) task).relatedRole() != null) {
            requiredRole = new Label("Required role: "
                    + ((AgentRoleTask) task).relatedRole());

        } else {
            requiredRole = new Label("Required role: any");
        }
        requiredRole.getStyleClass().add("body-text");
        leftColumn.getChildren().add(requiredRole);
     
        workerText = new Label("");
        workerText.getStyleClass().add("body-text");
        leftColumn.getChildren().add(workerText);
        
        workerImage = new ImageView(new Image("/agents/individual/any.png"));
        workerImage.getStyleClass().add("worker-image");
        layoutPane.setCenter(workerImage);
        
        layoutPane.setLeft(leftColumn);
        textContainer.getChildren().add(layoutPane);
        
        workerDropdown = agentSelectorSetup();
        workerDropdown.setPrefWidth(199);
        workerDropdown.getStyleClass().add("body-text");
        textContainer.getChildren().add(workerDropdown);
        
        this.setPrefSize(207, 85);
        this.setOnMouseClicked(event -> TaskPane.this.selectToggle());
        setLocationText();
        updateSizeText();
        setAllowedWorkerText(task);
    }
    
    private ChoiceBox<String> agentSelectorSetup() {
        workerList = new ArrayList<Agent>();
        ChoiceBox<String> dropdown = new ChoiceBox<String>(
                controller.getAgentList(tasks.get(0), workerList));
        dropdown.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number value, Number newValue) {
                        if ((int) newValue >= 0) {
                            controller.setWorkerForTasks(tasks,
                                    workerList.get((int) newValue));
                        }
                    }
                });
        return dropdown;
    }
    
    /**
     * Updates agents in the worker selection dropdown.
     */
    public void updateAgents() {
        Platform.runLater(() -> {
            workerDropdown.hide();
            workerDropdown.getItems().clear();
            workerList = new ArrayList<Agent>();
            workerDropdown.setItems(controller.getAgentList(tasks.get(0), workerList));
        });
    }

    /**
     * takes a group of tasks and if they can be represented as a stack,
     * transform this tile into a stack with these tasks.
     * 
     * @param group
     *            The group representing the stack.
     */
    @SuppressWarnings("unchecked")
    private void stack(ArrayList<AbstractTask> group) {
        progressBar.setProgress((float) 1
                - ((float) tasks.size() / allTasks.size()));
        if (group.size() > 1) {
            this.tasks = (ArrayList<AbstractTask>) group.clone();
            if (group.size() > allTasks.size()) {
                this.allTasks = (ArrayList<AbstractTask>) group.clone();
            }
        }
        updateSizeText();
    }
    
    private void updateSizeText() {
        String size;
        if (isStack) {
            if (tasks.size() == 1) {
                size = tasks.size() + " task remaining";
            } else {
                size = tasks.size() + " tasks remaining";
            }
        } else {
            size = tasks.size() + " task";
        }
        Platform.runLater(() -> taskSize.setText(size));
    }
    
    /**
     * Updates the location text of this tile to be representative of the tasks
     * that are currently in the pane.
     */
    private void setLocationText() {
        String location;
        if (isStack) {
            location = "Location: "
                    + allTasks.get(0).getLocation().toNeatString()
                    + " to "
                    + allTasks.get(allTasks.size() - 1).getLocation()
                            .toNeatString();
        } else {
            location = "Location: " + tasks.get(0).getLocation().toNeatString();
        }
        Platform.runLater(() -> taskLocation.setText(location));
    }
    
    private void setAllowedWorkerText(AbstractTask task) {
        if (!started || isStack) {
            if (task.getAllowedWorker() == null) {
                Platform.runLater(() -> {
                    workerText.setText("Assigned worker: any");
                    workerImage
                            .setImage(new Image("/agents/individual/any.png"));
                });
            } else {
                Platform.runLater(() -> {
                    workerText.setText("Assigned worker: "
                            + task.getAllowedWorker());
                    workerImage.setImage(new Image(task.getAllowedWorker()
                            .getImageFileName()));
                });
            }
        }
    }

    /**
     * Gets a list of all the tasks in this TaskPane.
     * 
     * @return An ArrayList of AbstractTask Objects.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<AbstractTask> getTasks() {
        return (ArrayList<AbstractTask>) tasks.clone();
    }

    /**
     * Allows the user to add additional tasks to the queue in the task pane.
     *
     * @param task
     *            The Task to be added.
     */
    public void addToStack(AbstractTask task) {
        isStack = true;
        task.addObserver(new TaskObserver());
        tasks.add(task);
        allTasks.add(task);
        this.stack(tasks);
        setLocationText();
    }

    /**
     * RemoveFromStack allows the user to delete any tasks the user no longer
     * wishes to complete.
     * 
     * @param task
     *            The Task to be removed.
     */
    public void removeFromStack(AbstractTask task) {
        tasks.remove(task);
        this.stack(tasks);
        task.deleteObservers();
    }

    /**
     * Toggles the state of selection of this TaskPane.
     */
    private void selectToggle() {
        ArrayList<Point> points = new ArrayList<Point>();
        for (AbstractTask task : allTasks) {
            points.add(task.getLocation());
        }
        if (selected) {
            selected = false;
            controller.removeFromSelection(points);
            this.getStyleClass().remove("selected");
        } else {
            selected = true;
            controller.addToSelection(points);
            this.getStyleClass().add("selected");
        }
    }
    
    private void onStartTask() {
        if (!started) {
            started = true;
            Platform.runLater(() -> taskState.setText(" - In progress"));
            if (!isStack) {
                Platform.runLater(() -> progressBar.setProgress(-1));
            }
        }
    }
    
    private void addActiveWorker(AbstractTask task) {
        if (!started) {
            Platform.runLater(() -> taskState.setText(" - Moving to location"));
        }
        if (!isStack) {
            Platform.runLater(() -> {
                workerText.setText("Worker: " + task.getWorker());
                workerImage.setImage(new Image(task.getWorker()
                        .getImageFileName()));
            });
        } else {
            if (task.getAllowedWorker() != null) {
                Platform.runLater(() -> workerText.setText("Worker: "
                        + task.getWorker()));
            }
        }
    }

    /**
     * A boolean function that works out which of the tasks have been selected
     * to to be deleted.
     *
     * @return The selection state of this taskPane.
     */
    public boolean isSelected() {
        return selected;
    }
    
    private class TaskObserver implements Observer {
        
        /**
         * Updates task pane according to message received from task update.
         */
        @Override
        public void update(Observable task, Object arg) {
           String option = (String) arg;
           switch (option) {
               case "allowedWorker":
                   setAllowedWorkerText((AbstractTask) task);
                   break;
               case "started":
                   onStartTask();
                   break;
               case "setWorker":
                   addActiveWorker((AbstractTask) task);
                   break;
               default:
                   break;
           }

        }
    }

}
