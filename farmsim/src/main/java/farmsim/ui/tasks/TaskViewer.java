package farmsim.ui.tasks;

import farmsim.tasks.AbstractTask;
import farmsim.ui.PopUpWindow;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Pop Up Window that displays current tasks and allows user to delete selected
 * tasks or move them to the top or bottom of the queue.
 *
 */
public class TaskViewer extends PopUpWindow {

    private VBox taskViewerBox;
    private TaskViewerController taskViewerController;
    private ChoiceBox<String> macrosDropdown;
    private ArrayList<TaskPane> taskPanes;
    private boolean recording = false;

    public TaskViewer() {
        super(370, 235, 0, 0, "Task Manager");
        getStylesheets().add("css/taskViewer.css");
        this.getStyleClass().add("task-viewer");
        
        macrosDropdown = macroDropdownSetup();
        ScrollPane scroll = new ScrollPane();
        scroll.getStyleClass().add("scroll");
        scroll.setPrefSize(235, 300);
        scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);

        taskViewerBox = new VBox(8);
        taskViewerBox.getStyleClass().add("task-viewer-inner");
        taskPanes = new ArrayList<TaskPane>();
        scroll.setContent(taskViewerBox);

        BorderPane taskViewerRootPane = new BorderPane();
        taskViewerRootPane.setCenter(scroll);
        
        HBox buttonSection = new HBox(5);
        buttonSection.setPrefSize(235, 30);
        buttonSection.getStyleClass().add("button-section");
        buttonSection.setPadding(new Insets(2));
        buttonSection.setAlignment(Pos.CENTER);
        addDeleteButton(buttonSection);
        addMoveButtons(buttonSection);
        addMacroButtons(buttonSection);
        
        Platform.runLater(() -> {
                Tooltip.install(macrosDropdown, new Tooltip("Macros"));
            });
        buttonSection.getChildren().add(macrosDropdown);
        taskViewerRootPane.setBottom(buttonSection);

        this.setContent(taskViewerRootPane);
    }
    
    
    /**
     * Initialises this TaskViewer with this controller object.
     * @param taskViewerController
     * 				Controller to control this view.
     */
    public void setController(TaskViewerController taskViewerController) {
        this.taskViewerController = taskViewerController;
    }
    
    /**
     * gets a new, updated list of macros for the purpose of keeping
     * the macros dropdown up to date
     */
    public void updateMacrosDropdown() {
    	macrosDropdown.getItems().clear();
    	macrosDropdown.setItems(taskViewerController.getMacroList());
    }
    
    /**
     * Initialises the macro dropdown.
     * @return
     * 		A choicebox representing the macro dropdown.
     */
    private ChoiceBox<String> macroDropdownSetup() {
        ChoiceBox<String> macroDropdown = new ChoiceBox<String>();
        macroDropdown.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number value, Number newValue) {
                        if ((int) newValue >= 0) {
                            taskViewerController.runTaskMacro(macroDropdown
                                    .getItems().get((int) newValue));
                            macroDropdown.getSelectionModel().select(-1);
                        }
                    }
                });
        return macroDropdown;
    }
    
    /**
     * Adds a taskPane containing task to the taskViewerBox in the view.
     * 
     * @param task
     *            The task to be added to the view as a TaskPane
     */
    public void addTaskPane(AbstractTask task) {
        TaskPane taskPane = new TaskPane(task, taskViewerController);
        taskPanes.add(taskPane);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taskViewerBox.getChildren().add(taskPane);
            }
        });
    }

    /**
     * Removes all taskPanes from view.
     */
    public void clearTasks() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taskViewerBox.getChildren().clear();
            }
        });

    }

    /**
     * Removes any taskPanes containing task from the view.
     * 
     * @param task
     *            Task to be removed
     */
    public void findAndRemoveTask(AbstractTask task) {
        for (TaskPane taskPane : taskPanes) {
            if (taskPane.getTasks().contains(task)) {
                if (taskPane.getTasks().size() < 2) {
                    taskPanes.remove(taskPane);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            taskViewerBox.getChildren().remove(taskPane);
                        }
                    });
                } else {
                    taskPane.removeFromStack(task);
                }

                return;
            }
        }
    }

    /**
     * Users are able to add new tasks to get done to the list of tasks (stack)
     * in the TaskViewer.
     * 
     * @param tasks
     *          Stack of tasks to add task to
     * @param task
     *          Task to add to stack
     */
    public void addToStackPane(ArrayList<AbstractTask> tasks,
            AbstractTask task) {
        for (TaskPane taskPane : taskPanes) {
            if (taskPane.getTasks().equals(tasks)) {
                taskPane.addToStack(task);
                return;
            }
        }
    }

    /**
     * Initialises and adds a delete button to the given HBox.
     * @param box
     * 		box to be assigned the created delete button.
     */
    private void addDeleteButton(HBox box) {
        ImageView deleteButton = new ImageView();
        deleteButton.setFitHeight(30);
        deleteButton.setPreserveRatio(true);
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setPickOnBounds(true);
        Platform.runLater(() -> Tooltip.install(deleteButton, new Tooltip(
                "Delete")));
        deleteButton.setOnMousePressed(event -> {
                for (Node node : taskViewerBox.getChildren()) {
                    TaskPane taskPane = (TaskPane) node;
                    if (taskPane.isSelected()) {
                        taskViewerController.removeTaskPane(
                                taskPane);
                    }
                }
            });
        box.getChildren().add(deleteButton);
    }

    /**
     * Initialises and adds move buttons to the given Hbox.
     * @param box
     * 		HBox to be assigned the created move buttons.
     */
    private void addMoveButtons(HBox box) {
        ImageView moveToTopButton = new ImageView();
        moveToTopButton.setFitHeight(30);
        moveToTopButton.setPreserveRatio(true);
        moveToTopButton.getStyleClass().add("move-to-top-button");
        moveToTopButton.setPickOnBounds(true);
        Platform.runLater(() -> Tooltip.install(moveToTopButton, new Tooltip(
                "Move to top")));
        moveToTopButton.setOnMousePressed(event -> {
                for (int i = taskViewerBox.getChildren().size() - 1; i >=0;
                        i--) {
                    if (taskViewerBox.getChildren().size() < 2) {
                        break;
                    }
                    TaskPane taskPane = (TaskPane)
                            taskViewerBox.getChildren().get(i);
                    if (taskPane.isSelected()) {
                        Platform.runLater(() -> {
                                taskViewerBox.getChildren().remove(taskPane);
                                taskViewerBox.getChildren().add(0, taskPane);
                            });
                        taskViewerController.moveToTopPane(
                                taskPane.getTasks());
                    }
                }
            });
        box.getChildren().add(moveToTopButton);

        ImageView moveToBottomButton = new ImageView();
        moveToBottomButton.setFitHeight(30);
        moveToBottomButton.setPreserveRatio(true);
        moveToBottomButton.getStyleClass().add("move-to-bottom-button");
        moveToBottomButton.setPickOnBounds(true);
        Platform.runLater(() -> Tooltip.install(moveToBottomButton,
                new Tooltip("Move to bottom")));
        moveToBottomButton.setOnMousePressed(event -> {
                for (Node node : taskViewerBox.getChildren()) {
                    if (taskViewerBox.getChildren().size() < 2) {
                        break;
                    }
                    TaskPane taskPane = (TaskPane) node;
                    if (taskPane.isSelected()) {
                        Platform.runLater(() -> {
                                taskViewerBox.getChildren().remove(taskPane);
                                taskViewerBox.getChildren().add(taskPane);
                            });
                        taskViewerController.moveToBottomPane(
                                taskPane.getTasks());
                    }
                }
            });
        box.getChildren().add(moveToBottomButton);
    }
    
    /**
     * Initialises and assigns macro recording buttons to the given hbox.
     * @param box
     * 		box to be given the created macro buttons.
     */
    private void addMacroButtons(HBox box) {
        ImageView recordButton = new ImageView();
        recordButton.setFitHeight(30);
        recordButton.setPreserveRatio(true);
        recordButton.getStyleClass().add("record-button");
        recordButton.setPickOnBounds(true);
        Platform.runLater(() -> Tooltip.install(recordButton, new Tooltip(
                "Record macro/stop recording")));
    	recordButton.setOnMouseClicked(event -> {
    	    if (recording) {
    	        taskViewerController.stopRecordingTasks();
    	        recording = false;
    	        recordButton.getStyleClass().remove("stop-button");
    	    } else {
    	        taskViewerController.startRecordingTasks();
    	        recording = true;
    	        recordButton.getStyleClass().add("stop-button");
    	    }
    	});
    	box.getChildren().add(recordButton);
    }

    /**
     * updates the worker dropdowns.
     */
	public void updateAgentDropdowns() {
		for (Node node : taskViewerBox.getChildren()) {
            TaskPane taskPane = (TaskPane) node;
            taskPane.updateAgents();
        }
		
	}
}
