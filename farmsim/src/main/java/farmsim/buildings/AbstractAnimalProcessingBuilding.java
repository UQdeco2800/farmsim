package farmsim.buildings;

import farmsim.buildings.process.AbattoirBuilding;
import farmsim.buildings.process.AnimalProcessingBuildingVisitor;
import farmsim.tasks.animalProcessing.TestTask;
import farmsim.ui.*;
import farmsim.util.Point;
import farmsim.world.World;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;

/**
 * The Animal Processing Building is a building that allows users to complete animal processing actions.
 * Animal Processing Actions include both:
 * - Retrieving Animal Products (e.g. Milk, Eggs, Feathers, etc)
 * - Slaughtering an Animal for its meat (e.g. Chicken Breast, Lamb, etc)
 * The animal processing skill of the worker directly influences available actions, time taken to complete and chance of successful processing.
 * The health, age and sex of the particular animal directly influence the quality (amount) received on completion of an action.
 *
 * @author original author llste modified by gjavi for Team Floyd
 */
public abstract class AbstractAnimalProcessingBuilding extends
        AbstractBuilding {
    private PopUpWindow popup;
    private TestTask testTask;
    public AbstractAnimalProcessingBuilding(World world, int width, int
            height, String imageName) {
        super(world, width, height, imageName);
    }


    @Override
    public void onDoubleClick(Point location) {
        return;
        /*PopUpWindowManager manager = PopUpWindowManager.getInstance();
        if (popup == null) {
            popup = new AnimalProcessingPopUp2();
        } else if (manager.containsPopUpWindow(popup)) {
            // don't add the popup window more than once.
            return;
        }
        manager.addPopUpWindow(popup);*/
    }


    public void test() {

        //popup = new AnimalProcessingPopUp2();

        PopUpWindowManager manager = PopUpWindowManager.getInstance();
        if (popup == null) {
            popup = new AnimalProcessingPopUp2();
        } else if (PopUpWindowManager.getInstance().containsPopUpWindow(popup)) { //was manager
            // don't add the popup window more than once.
            return;
        }
        PopUpWindowManager.getInstance().addPopUpWindow(popup);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public abstract void accept(AnimalProcessingBuildingVisitor v);
}
