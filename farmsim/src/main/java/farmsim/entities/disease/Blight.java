package farmsim.entities.disease;

import farmsim.entities.tileentities.crops.Apple;

/**
 * Implementation of Blight as a disease with a medium degree of
 * contagiousness and severity.
 * Blight pestilence with constructor for default attributes.
 * Extends pestilence so infects Crops.
 * @author jared
 */
public class Blight extends Pestilence {
	
	/**
	 * Default constructor for blight.
	 * Sets the severity, contagiousness, and lifetime of the disease in
	 * order for it to be used to infect crops.
	 */
	public Blight() {
		this.setSeverity(50);
		this.setContagiousness(50);
		this.setLifetime(100);
		this.targets.add(Apple.class);
	}
	
    @Override
    public String getName() {
    	return "Blight";
    }
}
