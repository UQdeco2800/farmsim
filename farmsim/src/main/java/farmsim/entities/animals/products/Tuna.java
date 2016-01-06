package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Fish;

/**
 * A class implementation which represents a tuna
 * 
 * @author gjavi 
 *
 */
public class Tuna extends AbstractAnimalProducts {

    /**
     * Creates a new Bacon.
     * @param animal
     *  The pig to being process
     * @param agent
     */
    public Tuna(Fish animal, Agent agent) {
        super(animal, agent);
        productName = "Tuna" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.tuna);
    }

}
