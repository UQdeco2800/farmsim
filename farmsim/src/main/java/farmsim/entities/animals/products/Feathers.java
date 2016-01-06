package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents feathers
 * 
 * @author gjavi 
 *
 */

public class Feathers extends AbstractAnimalProducts {

    public Feathers(FarmAnimal animal, Agent agent) {
        super(animal, agent);
        productName = "Feathers" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.feathers);
    }

}