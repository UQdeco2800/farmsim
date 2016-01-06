package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents an egg
 * 
 * @author gjavi 
 *
 */

public class Egg extends AbstractAnimalProducts {

    public Egg(FarmAnimal animal, Agent agent) {
        super(animal, agent);
        productName = "Egg" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.egg);
    }

}