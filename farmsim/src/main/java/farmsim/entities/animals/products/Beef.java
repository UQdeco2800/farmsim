package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents a beef
 * 
 * @author gjavi 
 *
 */

public class Beef extends AbstractAnimalProducts {

    public Beef(Cow animal, Agent agent) {
        super(animal, agent);
        productName = "Beef" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.beef);
    }

}
