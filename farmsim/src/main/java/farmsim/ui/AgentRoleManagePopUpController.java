package farmsim.ui;


import com.sun.javafx.collections.ImmutableObservableList;
import farmsim.GameManager;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.tasks.ChangeRoleTask;
import farmsim.tasks.TaskManager;
import farmsim.tasks.TrainingTask;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * @author hbsteel
 */
public class AgentRoleManagePopUpController implements Initializable {
    @FXML
    Label showing;
    @FXML
    ChoiceBox<String> role;

    @FXML
    ChoiceBox<String> trainRole;

    @FXML
    Button setArea;

    @FXML
    ListView<Agent> agentTable;

    private boolean unlockWorkerArea = false;

    private Rectangle workingArea;

    public void showAll(ActionEvent actionEvent) {
        showing.setText("(Showing all agents)");
        Object[] agents = AgentManager.getInstance().getAgents().toArray();
        Agent[] agentsArray =
                Arrays.copyOf(agents, agents.length, Agent[].class);
        ObservableList<Agent> list =
                new ImmutableObservableList<>(agentsArray);
        agentTable.setItems(list);
    }

    public void showSelection(ActionEvent actionEvent) {
        showing.setText("(Showing agents from a selection)");
        List<Agent> agents = AgentManager.getInstance().getAgents();
        List<Point> selection = GameManager.getInstance().getSelection();
        List<Agent> inSelection = new ArrayList<>();
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            if (agent != null && selection.contains(agent.getLocation())) {
                inSelection.add(agent);
            }
        }
        Object[] inSelectionArray = inSelection.toArray();
        Agent[] agentsArray =
                Arrays.copyOf(inSelectionArray, inSelectionArray.length,
                        Agent[].class);
        ObservableList<Agent> list =
                new ImmutableObservableList<>(agentsArray);
        agentTable.setItems(list);
    }



    public void setWorkersWorkingArea(ActionEvent actionEvent) {
        List<Point> selection = GameManager.getInstance().getSelection();
        Point topLeft = null, bottomRight = null;

        for (Point point : selection) {
            if (topLeft == null || (point.getX() < topLeft.getX() && point
                    .getY() < topLeft.getY())) {
                topLeft = point;
            }
            if (bottomRight == null || (point.getX() > bottomRight.getX() &&
                    point.getY() > bottomRight.getY())) {
                bottomRight = point;
            }
        }
        if (topLeft == null) {
            return;
        }
        unlockWorkerArea = false;
        int width = (int) bottomRight.getX() - (int) topLeft.getX();
        int height = (int) bottomRight.getY() - (int) topLeft.getY();
        workingArea = new Rectangle((int) topLeft.getX(),
                (int) topLeft.getY(), width, height);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set options for role
        role.getItems().clear();
        role.getItems().add("Any");
        role.getItems().addAll(Agent.getRoleTypeNames());
        // Set options for tasks
        trainRole.getItems().clear();
        trainRole.getItems().addAll(Agent.getRoleTypeNames());

        agentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        showAll(null);
    }

    public void applyButton(ActionEvent actionEvent) {
        boolean unlockWorkerRoles = false;
        Agent.RoleType roleType = null;
        // Get String from dropbdown boxes
        String roleBoxString = role.getValue();
        String trainBoxString = trainRole.getValue();
        // Check if text is Any otherwise get RoleType associated with string
        if (Objects.equals("Any", roleBoxString)) {
            unlockWorkerRoles = true;
        } else {
            roleType = Agent.getRoleTypeFromString(roleBoxString);
        }
        Agent.RoleType trainRoleType =
                Agent.getRoleTypeFromString(trainBoxString);
        // Get world and trainRole manager
        World world = WorldManager.getInstance().getWorld();
        TaskManager manager = TaskManager.getInstance();
        // Loop Through selected agents and apply changes
        ObservableList<Agent> selectedItems = agentTable
                .getSelectionModel()
                .getSelectedItems();
        for (int i = 0; i < selectedItems.size(); i++) {
            Agent agent = selectedItems.get(i);
            // Role logic
            if (unlockWorkerRoles) {
                agent.unlockRole();
            } else if (roleType != null) {
                if (roleType == agent.getCurrentRoleType()) {
                    agent.lockRole();
                } else {
                    ChangeRoleTask task = new ChangeRoleTask(roleType, world,
                            true);
                    task.setAllowedWorker(agent);
                    manager.addTask(task);
                }
            }

            // Set worker area
            if (unlockWorkerArea) {
                agent.areaUnlock();
            } else if (workingArea != null) {
                agent.setAreaLock(workingArea);
            }
            // Train worker
            if (trainRoleType != null) {
                TrainingTask task = new TrainingTask(TrainingTask
                        .BASE_DURATION, world, trainRoleType);
                // true);
                task.setAllowedWorker(agent);
                manager.addTask(task);
            }
        }
        // Empty role box string
        role.setValue("");
        trainRole.setValue("");
    }

    public void clearButton(ActionEvent actionEvent) {
        // Undo any worker area changes
        unlockWorkerArea = false;
        workingArea = null;

        // Reset choice boxes
        role.getSelectionModel().clearSelection();
        role.setValue(null);
        trainRole.getSelectionModel().clearSelection();
        trainRole.setValue(null);
    }

    public void clearWorkingArea(ActionEvent actionEvent) {
        unlockWorkerArea = true;
        workingArea = null;
    }
}
