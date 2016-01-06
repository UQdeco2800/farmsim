package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;


/**
 * A class for processing a animals.
 * 
 * @author gjavi1 for Team Floyd 
 *
 */
public class ProcessingFactory {
    private static AbstractProcessAnimal process;

    private ProcessingFactory() {
        // Do nothing here
    }

    public static AbstractProcessAnimal startProcess(Animal animal, Agent
            agent, Agent.RoleType roleType) {
        birdsHelperProcess(animal, agent, roleType);
        mammalsHelperProcess(animal, agent, roleType);
        if (animal instanceof Fish) {
            process = new ProcessFish(animal, agent, roleType);
        } else {
            process = null;
        }
        return process;
    }

    private static void birdsHelperProcess(Animal animal, Agent
            agent, Agent.RoleType roleType) {
        if (animal instanceof Chicken) {
            process = new ProcessChicken(animal, agent, roleType);
        } else if (animal instanceof Duck) {
            process = new ProcessDuck(animal, agent, roleType);
        }
    }

    private static void mammalsHelperProcess(Animal animal, Agent
            agent, Agent.RoleType roleType) {
        if (animal instanceof Pig) {
            process = new ProcessPig(animal, agent, roleType);
        } else if (animal instanceof Sheep) {
            process = new ProcessSheep(animal, agent, roleType);
        } else if (animal instanceof Cow) {
            process = new ProcessCow(animal, agent, roleType);
        }
    }
}
