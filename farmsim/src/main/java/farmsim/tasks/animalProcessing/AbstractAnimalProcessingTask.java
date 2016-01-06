package farmsim.tasks.animalProcessing;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;
import farmsim.tasks.AgentRoleTask;
import farmsim.tasks.MovingTask;
import farmsim.ui.Notification;
import farmsim.util.Point;
import farmsim.util.Sound;
import farmsim.world.World;

/**
 * Base class for all animal processing tasks
 */
public abstract class AbstractAnimalProcessingTask extends AgentRoleTask {

    // Instance of animal
    protected Animal animal;

    // Building
    protected AbstractAnimalProcessingBuilding building;

    //Role to level
    protected Agent.RoleType role;



    public AbstractAnimalProcessingTask(Point point, int duration,
                                        World world, int priority,
                                        String name, String id, Animal
                                                animal, AbstractAnimalProcessingBuilding building, Agent.RoleType role) {
        super(point, duration, world, name, id);
        this.animal = animal;
        this.building = building;
        this.role = role; //todo role or roletype
    }


}
