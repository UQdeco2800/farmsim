package farmsim.entities.disease;

/**
 * Implementation of a 'diseased easter egg'.
 * @author Kristi
 */
public class DiseasedEasterEgg extends Illness {

	/**
	 * Default constructor for the diseased easter egg.
	 * Sets the severity, contagiousness, and lifetime of the disease in
	 * order for it to be used to infect peons. 
	 * This method does not generate peons itself.
	 * 
	 * @param severity derived from the Illness class
	 * @param contagiousness derived from the Illness class
	 * @param severity derived from the Illness class
	 */
	public DiseasedEasterEgg() {
        this.setSeverity(20);
        this.setContagiousness(0);
        this.setLifetime(100);
    }
	
    @Override
    public String getName() {
    	return "Easter";
    }
	
}
