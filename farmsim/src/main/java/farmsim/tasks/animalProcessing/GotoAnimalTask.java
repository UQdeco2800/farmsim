package farmsim.tasks.animalProcessing;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.buildings.process.AbattoirBuilding;
import farmsim.entities.animals.Animal;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.MovingTask;
import farmsim.ui.AnimalProcessingPopUp2;
import farmsim.ui.PopUpWindow;
import farmsim.ui.PopUpWindowManager;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.ui.Notification;
/**
 * Task for getting worker to animal
 */
public class GotoAnimalTask extends AbstractTask implements MovingTask {

    private Animal animal;
    private int pointX=0;
    private int pointY=0;
    AbstractAnimalProcessingBuilding building;
    private Notification notification;



    public GotoAnimalTask(Point point, World world, int priority,
                          String name, String id, Animal animal,AbstractAnimalProcessingBuilding building) {
        super(point, 0, world, name, id);

        this.animal = animal;
        this.pointX = (int) point.getX();
        this.pointY = (int) point.getY();
        this.building = building;
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
        //ßbuilding.test();
        //notification.makeNotification("Testing","Testing abc");
        //new task
        //notification
    }




    @Override
    public void updateTaskLocation() {
        // update task to follow animal
        location = animal.getLocation();
    }
}
