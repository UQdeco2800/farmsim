package farmsim.entities.disease;

/**
 * Implementation of Measles as a highly severe disease with low contagiouness.
 * Measles illness with constructor for default attributes.
 * Extends illness so infects Agents.
 * @author Kristi
 */
public class Measles extends Illness {
	
	/**
	 * Default constructor for Measles.
	 * Sets the severity, contagiousness, and lifetime of the disease in
	 * order for it to be used to infect peons. 
	 * 
	 * @param severity derived from the Illness class
	 * @param contagiousness derived from the Illness class
	 * @param severity derived from the Illness class
	 */
    public Measles() {
        this.setSeverity(70);
        this.setContagiousness(20);
        this.setLifetime(8);
    }

    @Override
    public String getName() {
    	return "Measles";
    }
    
}
