package farmsim.entities.disease;

import java.util.HashSet;

import common.disease.Treatment;

/**
 * Class for all types of pesticides to inherit. DiseaseManager can do damage to
 * pests based on parameters defined here
 * 
 */
public class Pesticide extends Treatment<Pestilence> {
	
	protected static int treatmentPoints = 0;
	
	/**
	 * General pesticides can affect any pestilence by default
	 */
	public Pesticide() {
		this.targets = new HashSet<Class<? extends Pestilence>>();
		this.targets.add(Pestilence.class);
	}
	
	public static int getTreatmentPoints() {
		return treatmentPoints;
	}
	
	/**
	 * Alters treatmentPoints by diff.  treatmentPoints will not go lower than 0
	 * @param diff
	 */
	public static void alterTreatmentPoints(int diff) {
		treatmentPoints += diff;
		if (treatmentPoints < 0) {
			treatmentPoints = 0;
		}
	}
}
