package farmsim.entities.animals;

import java.util.*;

/**
 * AgentManager is responsible for the managing of FarmAnimals (see
 * {@link FarmAnimal}, including the distribution of their Tasks.
 */
public class FarmAnimalManager {

    private static final FarmAnimalManager INSTANCE = new FarmAnimalManager();

    private List<FarmAnimal> farmAnimals;

    private FarmAnimalManager() {
        farmAnimals = Collections.synchronizedList(new ArrayList<>());
    }

    public static FarmAnimalManager getInstance() {
        return INSTANCE;
    }

    public void tick() {
        for (FarmAnimal farmAnimal : farmAnimals) {
            farmAnimal.tick();
        }
    }

    public void addFarmAnimal(FarmAnimal farmAnimal) {
    }

    public void removeFarmAnimal(FarmAnimal farmAnimal) {
        farmAnimals.remove(farmAnimal);
    }

    public void removeFarmAnimal(UUID id) {
        for (FarmAnimal farmAnimal : farmAnimals) {
            if (farmAnimal.getIdentifier() == id) {
                farmAnimals.remove(farmAnimal);
            }
        }
    }

    public List<FarmAnimal> getFarmAnimals() {
        return farmAnimals;
    }
}
