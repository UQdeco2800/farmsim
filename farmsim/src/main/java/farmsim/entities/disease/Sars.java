package farmsim.entities.disease;

/**
 * Implementation of SARS as the most contagious form of disease.
 * @author Kristi
 */
public class Sars extends Illness {

	/**
	 * Default constructor for Sars
	 * Sets the severity, contagiousness, and lifetime of the disease in
	 * order for it to be used to infect peons. 
	 * This method does not generate peons itself.
	 * 
	 * @param severity derived from the Illness class
	 * @param contagiousness derived from the Illness class
	 * @param severity derived from the Illness class
	 */
	public Sars() {
        this.setSeverity(60);
        this.setContagiousness(90);
        this.setLifetime(15);
    }
	
	@Override
    public String getName() {
    	return "Sars";
    }
	
}
