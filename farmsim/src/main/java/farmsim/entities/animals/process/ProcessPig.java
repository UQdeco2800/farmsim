package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;
import farmsim.entities.animals.products.*;

/**
 * A class for processing a pig.
 * 
 * @author gjavi1 for Team Floyd 
 *
 */
public class ProcessPig extends AbstractProcessAnimal {

    private Pig pig; //The animal being process
    private int successRate; //The number of products to produce

    /**
     * Class for processing a pig which extends AbstractProcessAnimal class.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public ProcessPig(Animal animal, Agent agent, Agent.RoleType roleType) {

        super(animal, agent, roleType);
        this.pig = (Pig)animal;
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
        if (super.roleType == Agent.RoleType.BUTCHER) {
            return killPig();
        } else {
            return PROCESS_INVALID;
        }
    }

    /**
     * Kills the pig and produce some bacon then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int killPig() {
        switch (agentLevel) {
            case 1:
                return PROCESS_LOW_LEVEL;
            case 2:
                return PROCESS_LOW_LEVEL;
            case 3:
                return PROCESS_LOW_LEVEL;
            default:
                baconHelper();
                agent.addExperienceForRole(Agent.RoleType.BUTCHER, 10);
                return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for killing a pig. Adds the output products on the
     * product list and then kills the animal.
     */
    private void baconHelper() {
        for (int i=0; i < successRate; i++) {
            Bacon bacon = new Bacon(pig, agent);
            bacon.produceProduct();
        }
        pig.kill();
        FarmAnimalManager.getInstance().removeFarmAnimal(pig);
    }
}
