package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents a duck breast
 * 
 * @author gjavi 
 *
 */

public class DuckBreast extends AbstractAnimalProducts {

    public DuckBreast(Duck animal, Agent agent) {
        super(animal, agent);
        productName = "DuckBreast" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.duckBreast);
    }

}