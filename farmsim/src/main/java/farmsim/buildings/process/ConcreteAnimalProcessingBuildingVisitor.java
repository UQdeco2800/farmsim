package farmsim.buildings.process;

import farmsim.ui.AnimalProcessingSelectionController;
import javafx.scene.control.Button;


/**
 * Created by homer5677 on 23/10/2015.
 */
public class ConcreteAnimalProcessingBuildingVisitor implements AnimalProcessingBuildingVisitor {

    AnimalProcessingSelectionController controller;



    @Override
    public AbattoirBuilding visit(AbattoirBuilding abattoirBuilding) {
        return abattoirBuilding;
    }

    @Override
    public AnimalCoop visit(AnimalCoop animalCoop) {
        return animalCoop;
    }

    @Override
    public BarnBuilding visit(BarnBuilding barnBuilding) {
        return barnBuilding;
    }

    @Override
    public ShearingShed visit(ShearingShed shearingShed) {
        return shearingShed;
    }


/*
    //Todo should be on vistor interface>
    private Button associateButton(AbattoirBuilding abattoirBuilding){

    }*/
}
