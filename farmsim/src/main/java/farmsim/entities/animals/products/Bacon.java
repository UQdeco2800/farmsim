package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;

/**
 * A class implementation which represents a bacon
 * 
 * @author gjavi 
 *
 */
public class Bacon extends AbstractAnimalProducts {

    /**
     * Creates a new Bacon.
     * @param animal
     *  The pig to being process
     * @param agent
     */
    public Bacon(Pig animal, Agent agent) {
        super(animal, agent);
        productName = "Bacon" + animal.getIdentifier();
    }

    @Override
    public void produceProduct() {
        agent.getRucksack().addToRucksack(product.bacon);
    }

}
