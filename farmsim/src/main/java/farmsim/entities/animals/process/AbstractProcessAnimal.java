package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;

/**
 * 
 * An abstract class for processing a farm animal.
 * 
 * @author gjavi1 for Team Floyd
 *
 */
public abstract class AbstractProcessAnimal {

    protected Animal animal; // the animal currently being process
    protected Agent agent; // the agent doing the process

    //Exit status for an invalid process
    protected static int PROCESS_INVALID = -1;
    //Exit status for a successful process
    protected static int PROCESS_SUCCESS = 0;
    //Exit status for an unsuccessful process
    protected static int PROCESS_FAILED = 1;
    //Exit status for an unsuccessful process
    protected static int PROCESS_LOW_LEVEL = 2;

    /* Variables for the Agent doing the current process */
    protected int agentLevel;
    protected Agent.RoleType roleType;

    /* The probability for getting products */
    private static int probability;

    /**
     * An abstract class for processing a farm animal.
     *
     * @param animal
     *  The animal to be process.
     * @param agent
     *  The agent doing the process.
     */
    public AbstractProcessAnimal(Animal animal, Agent agent, Agent.RoleType
            roleType) {

        this.animal = animal;
        this.agent = agent;
        this.roleType = roleType;
        this.agentLevel = agent.getLevelForRole(roleType);
        calculateProbabilitySuccess();
    }

    /**
     * Returns probability of success of a process.
     *
     * @return
     *  The probability of a successful process
     */
    public int getProbabilitySuccess() {
        return probability;
    }
    
    /**
     *  Calculates the probability success for the process.
     *  
     */
    private void calculateProbabilitySuccess() {
        double animalAge = animal.getAge();
        if (animalAge < 34) {
            youngAnimal();
        } else if (animalAge < 66) {
            adultAnimal();
        } else {
           oldAnimal();
        }
    }
    
    /**
     * Probability calculation for a young farm animal.
     * 
     */
    private void youngAnimal() {
        int prob = (int)(Math.random() * 100);
        if (prob < 40) {
            probability = (prob % 3) + agentLevel;
        } else {
            probability = 0;
        }
    }

    /**
     * Probability calculation for a adult farm animal.
     * 
     */
    private void adultAnimal() {
        int prob = (int)(Math.random() * 100);
        if (prob < 60) {
            probability = (prob % 15) + agentLevel;
       } else {
           probability = 0;
       }
    }

    /**
     * Probability calculation for a Old farm animal.
     * 
     */
    private void oldAnimal() {
       int prob = (int)(Math.random() * 100);
       if (prob < 50) {
           probability = (prob % 5) + agentLevel;
       } else {
           probability = 0;
       }
    }

    /**
     * Method for doing a specific process to a farm animal.
     *
     * @return
     *   Returns 1 when the process is successful, 2 if the process is not
     *   possible because of the current level of the player, 0 otherwise.
     */
    public abstract int getProducts();
}
