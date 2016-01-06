package farmsim.buildings.process;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.ui.PopUpWindow;
import farmsim.world.World;

/**
 * The Animal Processing Building is a building that allows users to complete animal processing actions.
 * Animal Processing Actions include both:
 *      - Retrieving Animal Products (e.g. Milk, Eggs, Feathers, etc)
 *      - Slaughtering an Animal for its meat (e.g. Chicken Breast, Lamb, etc)
 * The animal processing skill of the worker directly influences available actions, time taken to complete and chance of successful processing.
 * The health, age and sex of the particular animal directly influence the quality (amount) received on completion of an action.
 *
 * @author original author llste modified by gjavi for Team Floyd
 */
public class AbattoirBuilding extends AbstractAnimalProcessingBuilding {
    public static final String SPRITE_LOCATION =
            "/buildings/abattoir.png";
    public static final int WIDTH = 4;
    public static final int HEIGHT = 3;
    private PopUpWindow popup;

    public AbattoirBuilding(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }


    @Override
    public void accept(AnimalProcessingBuildingVisitor v) {
        v.visit(this);
    }
}
