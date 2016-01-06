package farmsim.entities.disease;



/**
 * Implementation of Influenza as one of the least severe diseases.
 * Influenza illness with constructor for default attributes.
 * Extends illness so infects Agents.
 * @author Kristi
 */
public class Influenza extends Illness {
	
	/**
	 * Default constructor for Influenza.
	 * Sets the severity, contagiousness, and lifetime of the disease in
	 * order for it to be used to infect peons.
	 * @param severity derived from the Illness class
	 * @param contagiousness derived from the Illness class
	 * @param severity derived from the Illness class
	 */
    public Influenza() {
        this.setSeverity(30);
        this.setContagiousness(50);
        this.setLifetime(10);
    }
    
    @Override
    public String getName() {
    	return "Influenza";
    }
    
}
