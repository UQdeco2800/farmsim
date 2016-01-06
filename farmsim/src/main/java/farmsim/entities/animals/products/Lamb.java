package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents a lamb
 * 
 * @author gjavi 
 *
 */

public class Lamb extends AbstractAnimalProducts {

    public Lamb(Sheep animal, Agent agent) {
        super(animal, agent);
        productName = "Lamb" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.lamb);
    }

}