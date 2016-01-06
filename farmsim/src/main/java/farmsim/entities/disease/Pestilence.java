package farmsim.entities.disease;

import java.util.HashSet;

import common.disease.Disease;
import farmsim.entities.tileentities.crops.Crop;

/**
 *
 * Class for all types of Pestilence to inherit. DiseaseManager can do damage to
 * crops based on parameters defined here
 * @author adamcstacey
 *
 */
public class Pestilence extends Disease<Crop, Pesticide> {
    /**
     * General pestilence can effect any crop and is vulnerable to any pesticide
     */
	public Pestilence() {
		this.targets = new HashSet<Class<? extends Crop>>();
		this.targets.add(Crop.class);
		this.treatments = new HashSet<Class<? extends Pesticide>>();
		this.treatments.add(Pesticide.class);
	}
	
    public String getName() {
    	return "Pest";
    }
	
}
