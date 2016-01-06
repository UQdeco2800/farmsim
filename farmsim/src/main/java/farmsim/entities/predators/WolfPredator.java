package farmsim.entities.predators;

import farmsim.Viewport;
import farmsim.entities.animals.Animal;
import farmsim.entities.animals.FarmAnimalManager;

/**
 * A Wolf animal to attack farm animals
 * 
 * @author r-portas
 */
public class WolfPredator extends Predator {

	private FarmAnimalManager farmAnimalManager;
	private Animal currentTarget;
	
    public WolfPredator(double x, double y, int health, double speed) {
        super(x, y, health, speed, 50, "wolf");
        farmAnimalManager = FarmAnimalManager.getInstance();
        currentTarget = null;
        startTask();
    }

    private Animal findClosestAnimal(){
    	
    	Animal minAnimal = null;
    	double minDistance = 0;
    	
    	for (Animal animal : farmAnimalManager.getFarmAnimals()){
    		double distance = location.distance(animal.getLocation());
    		
    		if (animal.getLifeStatus() && attackable(animal.getLocation())){	
	    		if (minAnimal == null || distance < minDistance){
	    			minAnimal = animal;
	    			minDistance = distance;
	    		}
    		}
    	}
    	
    	return minAnimal;
    }
    
    private void startTask() {
        currentTarget = findClosestAnimal();
        
    }

    private void handleAttack(){
    	if (attackCounter > attackSpeed){
    		if (currentTarget != null && currentTarget.getHealth() > 0){
    			currentTarget.kill();
    			startTask();
        	}
    		
    		attackCounter = 0;
    	}
    }
    
    public void tick(Viewport viewport) {
    	// Update the animal's location
    	if (currentTarget != null){
        	destination = currentTarget.getLocation();
        }
    	
        if (location != null && destination != null) {
            if (location.equals(destination) && attackable(destination)) {
            	attackCounter++;
                handleAttack();
            } else {
                moveToward(destination);
            }
        }
    }
}
