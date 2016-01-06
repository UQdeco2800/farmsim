package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

public class Wool extends AbstractAnimalProducts {

    public Wool(Sheep animal, Agent agent) {
        super(animal, agent);
        productName = "Wool" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.wool);
    }

}

