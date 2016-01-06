package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;
import farmsim.entities.animals.products.*;

/**
 * A class for processing a duck.
 * 
 * @author gjavi1 for Team Floyd 
 *
 */
public class ProcessDuck extends AbstractProcessAnimal {

    private Duck duck; //The animal being process
    private int successRate; //The number of products to produce

    /**
     * Class for processing a duck which extends AbstractProcessAnimal class.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public ProcessDuck(Animal animal, Agent agent, Agent.RoleType roleType) {

        super(animal, agent, roleType);
        this.duck = (Duck)animal;
        this.successRate = getProbabilitySuccess();
    }

    /**
     * Method for doing a specific process, process numbers are as follows:
     *  1 - kills the animal;
     *  2 - get eggs from the animal;
     *  3 - get feathers from the animal;
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
                return killDuck();
            case EGG_HANDLER:
                return getEggs();
            case SHEARER:
                return getFeathers();
            default:
        }
        return PROCESS_INVALID;
    }

    /**
     * Kills the duck and produce some duck breast then adds it to the
     * inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int killDuck() {
        if (agentLevel == 1) {
            return PROCESS_LOW_LEVEL;
        } else {
            killHelper();
            agent.addExperienceForRole(roleType, 8);
            return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for killing a duck. Adds the output products on the
     * product list and then kills the animal.
     */
    private void killHelper() {
        for (int i=0; i < successRate; i++) {
            DuckBreast duckBreast = new DuckBreast(duck, agent);
            duckBreast.produceProduct();
        }
        duck.kill();
        FarmAnimalManager.getInstance().removeFarmAnimal(duck);
    }

    /**
     * Produce some duck eggs then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int getEggs() {
        for (int i=0; i < successRate; i++) {
            Egg egg = new Egg(duck, agent);
            egg.produceProduct();
        }
        agent.addExperienceForRole(roleType, 5);
        return PROCESS_SUCCESS;
    }

    /**
    * Produce some duck feathers then adds it to the inventory.
    *
    * @return
    *   Returns 1 when the process is successful, 2 if the process is not
    *   possible because of the current level of the player, 0 otherwise.
    */
    private int getFeathers() {
        if (agentLevel < 4) {
            return PROCESS_LOW_LEVEL;
        } else {
            feathersHelper();
            agent.addExperienceForRole(roleType, 12);
            return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for getting feathers from a duck. Adds the output
     * products on the
     * product list and then kills the animal.
     */
    private void feathersHelper() {
        for (int i=0; i < successRate; i++) {
            Feathers feathers = new Feathers(duck, agent);
            feathers.produceProduct();
        }
    }
}
