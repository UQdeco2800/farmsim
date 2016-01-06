package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents a chicken breast
 * 
 * @author gjavi 
 *
 */

public class ChickenBreast extends AbstractAnimalProducts {

    public ChickenBreast(Chicken animal, Agent agent) {
        super(animal, agent);
        productName = "ChickenBreast" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.chickenBreast);
    }

}
