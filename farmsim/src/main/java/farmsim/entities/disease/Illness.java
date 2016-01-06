package farmsim.entities.disease;

import java.util.HashSet;

import common.disease.Disease;
import farmsim.entities.agents.Agent;

/**
 * Top level concrete implementation of an agent affecting disease
 * No added functionality or specialty
 * @author adamcstacey
 *
 */
public class Illness extends Disease<Agent, Medicine> {
	
	/**
	 * Constructor for default Illness
	 * A general illness can target any agent
	 */
	public Illness() {
		this.targets = new HashSet<Class<? extends Agent>>();
		this.targets.add(Agent.class);
		this.treatments = new HashSet<Class<? extends Medicine>>();
		this.treatments.add(Medicine.class);
	}
	
	public String getName() {
		return "Illness";
	}
}
