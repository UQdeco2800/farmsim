package farmsim.tasks.animalProcessing;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.MovingTask;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * Task for milking a cow
 */
public class MilkCowTask extends AbstractAnimalProcessingTask  {
    private AbstractAnimalProcessingBuilding building;


    public MilkCowTask(Point point, int duration,
                       World world, int priority,
                       String name, String id,
                       Animal animal,AbstractAnimalProcessingBuilding building, Agent.RoleType role) {
        super(point, duration, world, priority, name, id, animal,building,role);
        this.building = building;
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.MILKER;
    }

    @Override
    public int experienceGained() {
        return 1;
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
        //building.test(0,0);
        //building.test((int)building.getWorldX(),(int)building.getWorldY());
    }


}
