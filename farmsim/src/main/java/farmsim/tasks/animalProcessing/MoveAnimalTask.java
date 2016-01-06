package farmsim.tasks.animalProcessing;

import farmsim.entities.animals.Animal;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.MovingTask;
import farmsim.util.Point;
import farmsim.world.World;

/**
 *
 */
public class MoveAnimalTask extends AbstractTask {

    private Animal animal;
    private int duration = 10; //some default value???

    public MoveAnimalTask(Point point, World world, int priority, String name, String id, Animal animal) {
        super(point, 10,world,name,id);
        this.animal = animal;

    }



    public void moveAnimalTowardLocation(Point location) {
        animal.moveToward(location);
    }

    @Override
    public AbstractTask copy() {
        return null; //Not sure on this
    }

    @Override
    public void preTask() {

    }

    @Override
    public void postTask() {

        System.out.println("DONE THIS");
    }
}
