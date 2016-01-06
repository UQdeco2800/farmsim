package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;
import farmsim.entities.animals.Fish;
import farmsim.entities.animals.products.Tuna;
import farmsim.inventory.SimpleResourceHandler;

/**
 * A class for processing a pig.
 * 
 * @author gjavi1 for Team Floyd 
 *
 */
public class ProcessFish extends AbstractProcessAnimal {

    private Fish fish; //The animal being process
    private int successRate; //The number of products to produce

    /**
     * Class for processing a pig which extends AbstractProcessAnimal class.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public ProcessFish(Animal animal, Agent agent, Agent.RoleType roleType) {
        super(animal, agent, roleType);
        this.fish = (Fish)animal;
        this.successRate = getProbabilitySuccess();
    }

    /**
     *  Method for doing a specific process, process numbers are as follows:
     *  1 - kills the animal;
     * @return
     *  Returns -1 when the process number is invalid, 1 when the process is
     *  successful, 2 if the process is not possible because of the current
     *  level of the player, 0 otherwise.
     */
    @Override
    public int getProducts() {
        if (successRate == 0) {
            return PROCESS_FAILED;
        }
        if (super.roleType == Agent.RoleType.FARMER) {
            return killFish();
        } else {
            return PROCESS_INVALID;
        }
    }

    /**
     * Kills the fish and produce some tuna then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int killFish() {
        SimpleResourceHandler products = SimpleResourceHandler.getInstance();
        agent.getRucksack().removeFromRucksack(products.fish, 1);
        for (int i=0; i < successRate; i++) {
            Tuna tuna = new Tuna(fish, agent);
            tuna.produceProduct();
        }
        fish.kill();
        agent.addExperienceForRole(Agent.RoleType.FARMER, 10);
        return PROCESS_SUCCESS;
    }

}
