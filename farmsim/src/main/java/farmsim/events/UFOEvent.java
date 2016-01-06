package farmsim.events;

import farmsim.entities.animals.Animal;
import farmsim.events.statuses.StatusName;

/**
 * The UFO Event will summon a UFO to abduct an Animal.
 * 
 * @author bobri
 *
 */
public class UFOEvent extends AbstractEvent{
	
	private Animal victim;
	
	/**
	 * Creates a new UFO event that will abduct an animal
	 * @param victim
	 */
	UFOEvent(Animal victim){
		this.victim = victim;
	}

	/**
	 * Initiates the UFO event.
	 */
	@Override
	public void begin() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Gives this event a new Animal to target
	 * @param newVictim the new Animal to target
	 */
	public void changeVictim(Animal newVictim){
		this.victim =newVictim;
	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsTick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StatusName getName() {
		return StatusName.UFO;
	}

}
