package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;
import farmsim.entities.animals.products.*;

/**
 * A class for processing a sheep.
 * 
 * @author gjavi1 for Team Floyd 
 *
 */
public class ProcessSheep extends AbstractProcessAnimal {

    private Sheep sheep; //The animal being process
    private int successRate; //The number of products to produce

    /**
     * Class for processing a sheep which extends AbstractProcessAnimal class.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public ProcessSheep(Animal animal, Agent agent, Agent.RoleType roleType) {

        super(animal, agent, roleType);
        this.sheep = (Sheep)animal;
        this.successRate = getProbabilitySuccess();
    }

    /**
     *  Method for doing a specific process, process numbers are as follows:
     *  1 - kills the animal;
     *  2 - get wool from the animal;
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
                return killSheep();
            case SHEARER:
                return getWool();
            default:
                return PROCESS_INVALID;
        }
    }

    /**
     * Kills the sheep and produce some lamb then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int killSheep() {
        switch (agentLevel) {
            case 1:
                return PROCESS_LOW_LEVEL;
            case 2:
                return PROCESS_LOW_LEVEL;
            case 3:
                return PROCESS_LOW_LEVEL;
            default:
                killHelper();
                agent.addExperienceForRole(roleType, 12);
                return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for killing a sheep. Adds the output products on the
     * product list and then kills the animal.
     */
    private void killHelper() {
        for (int i=0; i < successRate; i++) {
            Lamb lamb = new Lamb(sheep, agent);
            lamb.produceProduct();
        }
        sheep.kill();
        FarmAnimalManager.getInstance().removeFarmAnimal(sheep);
    }

    /**
     * Produce some wool from the sheep then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int getWool() {
       switch (agentLevel) {
            case 1:
                return PROCESS_LOW_LEVEL;
            case 2:
                return PROCESS_LOW_LEVEL;
            default:
                woolHelper();
                agent.addExperienceForRole(roleType, 10);
                return PROCESS_SUCCESS;
       }
    }

    /**
     * Helper function for getting wool from a sheep. Adds the output
     * products on the product list and then kills the animal.
     */
    private void woolHelper() {
        for (int i=0; i < successRate; i++) {
            Wool wool = new Wool(sheep, agent);
            wool.produceProduct();
        }
    }
}
