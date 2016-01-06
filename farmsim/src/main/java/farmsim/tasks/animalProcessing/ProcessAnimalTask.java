package farmsim.tasks.animalProcessing;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentRole;
import farmsim.entities.animals.Animal;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.MovingTask;
import farmsim.ui.Notification;
import farmsim.util.Point;
import farmsim.util.Sound;
import farmsim.world.World;

/**
 * @author homer5677 - Team Floyd
 */
public class ProcessAnimalTask extends AbstractAnimalProcessingTask implements MovingTask {

    //private Sound animalProcessingSound; = new Sound("construction.mp3"); //to do change
    private Notification notification;

    public ProcessAnimalTask(Point point, int duration, World world, int priority, String name, String id, Animal animal, AbstractAnimalProcessingBuilding building,Agent.RoleType role) {
        super(point, duration, world, priority, name, id, animal, building,role);

    }

    @Override
    public void updateTaskLocation() {

    }

    @Override
    public Agent.RoleType relatedRole() {
        return null;
    }

    @Override
    public int experienceGained() {
        return 0;
    }

    @Override
    public AbstractTask copy() {
        return null;
    }

    @Override
    public void preTask() {

    }

    @Override
    public void postTask() {

    }
}
