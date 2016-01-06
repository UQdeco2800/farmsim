package farmsim.entities.disease;

/**
 * Implementation of Black Plague as a disease with a medium degree of
 * contagiousness and severity.
 * Black plague illness with constructor for default attributes.
 * Extends illness so infects Agents.
 * @author kristi
 */
public class BlackPlague extends Illness {

	/**
	 * Default constructor for black plague.
	 * Sets the severity, contagiousness, and lifetime of the disease in
	 * order for it to be used to infect peons.
	 */
    public BlackPlague() {
        this.setSeverity(50);
        this.setContagiousness(50);
        this.setLifetime(10);
    }
    
    @Override
    public String getName() {
    	return "BlackPlague";
    }
}
