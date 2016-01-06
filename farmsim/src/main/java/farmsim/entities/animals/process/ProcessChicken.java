package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;
import farmsim.entities.animals.Chicken;
import farmsim.entities.animals.FarmAnimalManager;
import farmsim.entities.animals.products.ChickenBreast;
import farmsim.entities.animals.products.Egg;
import farmsim.entities.animals.products.Feathers;

/**
 * A class for processing a chicken.
 *
 * @author gjavi1 for Team Floyd
 *
 */
public class ProcessChicken extends AbstractProcessAnimal {

    private Chicken chicken; //The animal being process
    private int successRate; //The number of products to produce

    /**
     * Class for processing a chicken which extends AbstractProcessAnimal class.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public ProcessChicken(Animal animal, Agent agent, Agent.RoleType roleType) {

        super(animal, agent, roleType);
        this.chicken = (Chicken)animal;
        this.successRate = getProbabilitySuccess();
    }

    /**
     * Method for doing a specific process, process numbers are as follows:
     *  1 - kills the animal;
     *  2 - get eggs from the animal;
     *  3 - get feathers from the animal;
     *
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
                return killChicken();
            case EGG_HANDLER:
                return getEggs();
            case SHEARER:
                return getFeathers();
            default:
        }
        return PROCESS_INVALID;
    }

    /**
     * Kills the chicken and produce some chicken breast then adds it to the
     * inventory.
     *
     *@return
     * Returns 1 when the process is successful, 2 if the process is not
     * possible because of the current level of the player, 0 otherwise.
     */
    private int killChicken() {
        if (agentLevel == 1) {
            return PROCESS_LOW_LEVEL;
        } else {
            killHelper();
            agent.addExperienceForRole(roleType, 8);
            return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for killing a chicken. Adds the output products on the
     * product list and then kills the animal.
     */
    private void killHelper() {
        for (int i=0; i < successRate; i++) {
            ChickenBreast chickenBreast = new ChickenBreast(chicken, agent);
            chickenBreast.produceProduct();
        }
        chicken.kill();
        FarmAnimalManager.getInstance().removeFarmAnimal(chicken);
    }

    /**
     * Produce some chicken eggs then adds it to the inventory.
     *
     * @return
     *  Returns 1 when the process is successful, 2 if the process is not
     *  possible because of the current level of the player, 0 otherwise.
     */
    private int getEggs() {
        for (int i=0; i < successRate; i++) {
            Egg egg = new Egg(chicken, agent);
            egg.produceProduct();
        }
        agent.addExperienceForRole(roleType, 5);
        return PROCESS_SUCCESS;
    }

    /**
     * Produce some chicken feathers then adds it to the inventory.
     *
     * @return
     *   Returns 1 when the process is successful, 2 if the process is not
     *   possible because of the current level of the player, 0 otherwise.
     */
    private int getFeathers() {
        switch (agentLevel) {
            case 1:
                return PROCESS_LOW_LEVEL;
            case 2:
                return PROCESS_LOW_LEVEL;
            case 3:
                return PROCESS_LOW_LEVEL;
            default:
                feathersHelper();
                agent.addExperienceForRole(roleType, 12);
                return PROCESS_SUCCESS;
        }
    }

    /**
     * Helper function for getting feathers from  a chicken. Adds the output
     * products on the product list and then kills the animal.
     */
    private void feathersHelper() {
        for (int i=0; i < successRate; i++) {
            Feathers feathers = new Feathers(chicken, agent);
            feathers.produceProduct();
        }
    }
}
