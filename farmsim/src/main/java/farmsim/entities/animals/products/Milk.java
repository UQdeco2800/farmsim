package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents a milk
 * 
 * @author gjavi 
 *
 */

public class Milk extends AbstractAnimalProducts {

    public Milk(Cow animal, Agent agent) {
        super(animal, agent);
        productName = "Milk" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.milk);
    }

}