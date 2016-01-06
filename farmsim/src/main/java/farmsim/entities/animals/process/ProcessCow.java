package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;
import farmsim.entities.animals.products.*;

/**
 * A class for processing a cow.
 *
 * @author gjavi1 for Team Floyd 
 *
 */
public class ProcessCow extends AbstractProcessAnimal {

    private Cow cow; //The animal being process
    private int successRate; //The number of products to produce

    /**
     * Class for processing a cow which extends AbstractProcessAnimal class.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public ProcessCow(Animal animal, Agent agent, Agent.RoleType roleType) {

        super(animal, agent, roleType);
        this.cow = (Cow)animal;
        this.successRate = getProbabilitySuccess();
    }

    /**
     *  Method for doing a specific process, process numbers are as follows:
     *  1 - kills the animal;
     *  2 - get milk from the animal;
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
        switch (super.roleType) {
            case BUTCHER:
                return killCow();
            case MILKER:
                return getMilk();
            default:
                return PROCESS_INVALID;
        }
    }

    /**
     * Kills the cow and produce some beef then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int killCow() {
        if (agentLevel == 1) {
            return PROCESS_LOW_LEVEL;
        } else {
            killHelper();
            agent.addExperienceForRole(roleType, 10);
            return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for killing a cow. Adds the output products on the
     * product list and then kills the animal.
     */
    private void killHelper() {
        for (int i=0; i < successRate; i++) {
            Beef beef = new Beef(cow, agent);
            beef.produceProduct();
        }
        cow.kill();
        FarmAnimalManager.getInstance().removeFarmAnimal(cow);
    }

    /**
     * Produce some Cow milk then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int getMilk() {
        if (agentLevel == 1) {
            return PROCESS_LOW_LEVEL;
        } else {
            milkHelper();
            agent.addExperienceForRole(roleType, 8);
            return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for getting some milk from a cow. Adds the output
     * products on the product list and then kills the animal.
     */
    private void milkHelper() {
        for (int i=0; i < successRate; i++) {
            Milk milk = new Milk(cow, agent);
            milk.produceProduct();
        }
    }
}
