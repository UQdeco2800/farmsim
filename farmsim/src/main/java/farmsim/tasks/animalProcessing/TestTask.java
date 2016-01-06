package farmsim.tasks.animalProcessing;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.buildings.AbstractBuilding;
import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.AgentRoleTask;
import farmsim.tasks.MovingTask;
import farmsim.ui.AnimalProcessingSelectionController;
import farmsim.ui.Notification;
import farmsim.util.Sound;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;

/**
 * A task for agents to build buildings.
 */
public class TestTask extends AgentRoleTask implements MovingTask {
    private static final int BASE_PRIORITY = 1;
    private static final String ID = "build";

    AbstractAnimalProcessingBuilding building;
    Animal animal;
    Agent agent;
    AnimalProcessingSelectionController controller;
    private Notification notification;


    public TestTask(AbstractAnimalProcessingBuilding building, Animal animal, Agent agent, AnimalProcessingSelectionController controller) {
        super(animal.getLocation(), 10,
                building.getWorld(), "Processing " + building, ID);
        this.building = building;
        this.animal = animal;
        this.agent  = agent;
        this.controller = controller;
    }

    @Override
    public void preTask() {

    }

    @Override
    public void postTask() {
        Platform.runLater(() -> {
            notification.makeNotification(animal.getAnimalType().name() + " Process", "Processing complete.");
            building.test();

        });
    }

    @Override
    public Agent.RoleType relatedRole() {
        return agent.getCurrentRoleType();
    }

    @Override
    public int experienceGained() {
        return 50;
    }

    @Override
    public AbstractTask copy() {
        return new TestTask(building,animal,agent,controller);
    }

    @Override
    public void updateTaskLocation() {
        location = animal.getLocation();
    }
}
