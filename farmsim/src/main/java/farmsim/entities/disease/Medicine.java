package farmsim.entities.disease;

import java.util.HashSet;

import common.disease.Treatment;

/**
 * Class for all types of Medicines to inherit. DiseaseManager can do damage to
 * illnesses based on parameters defined here
 */
public class Medicine extends Treatment<Illness> {
	
	protected static int treatmentPoints = 0;
	
	/**
	 * Constructor for default Medicine
	 * A general medicine can treat any Illness
	 */
	public Medicine() {
		this.targets = new HashSet<Class<? extends Illness>>();
		this.targets.add(Illness.class);
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
