package farmsim.buildings.process;

import farmsim.buildings.AbstractAnimalProcessingBuilding;

/**
 * Created by homer5677 on 23/10/2015.
 */
public interface AnimalProcessingBuildingVisitor<T> {

    public T visit(AbattoirBuilding abattoirBuilding);
    public T visit(AnimalCoop animalCoop);
    public T visit(BarnBuilding barnBuilding);
    public T visit(ShearingShed shearingShed);


}
