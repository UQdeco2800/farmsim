package farmsim.util;

import farmsim.ui.LevelerWindow;
import farmsim.ui.LevelerWindowController;
import javafx.scene.layout.Pane;

import java.util.*;

/**
 * <p>A class that handles all the experience values in the game. Only one
 * instance of this class can exist at any one time</p>
 * <p>
 * To get this class in normal circumstances use:<br/>
 * WorldManager.getInstance().getWorld().getLeveler()
 * </p>
 * @author team-hartmanis
 * */

/*
* We are no longer a singleton! - jack775544
* */
 public class Leveler {

	//Map of experience values per plant
	private HashMap<String, Integer> experienceMap;
	private HashMap<String, ArrayList<Double>> modifierMap;
	//The amount of experience required to level up - the double 200000 is a
	//feature not a bug

	// We need more memes, I am not sorry -jack775544
	public static final String MEMES = "DUCKLYFE";

	public LevelerWindowController windowController;
	public Leveler(){
		this.experienceMap = new HashMap<>();
	}

	/**
	 * Gets the game's instance of the leveler
	 * @return the instance {@link Leveler}
	* */
	/*public static Leveler getInstance(){
		return INSTANCE;
	}*/

	/**
	 * Get a copy of the experience hashmap
	 * Note: The value in the map is a reference Integer not a primitive int
	 * @return A hashmap of experience values
	* */
	public Map<String, Integer> getExperienceMap(){
		HashMap<String, Integer> map = new HashMap<>(experienceMap);
		return map;
	}
	 * @param crop The crop to get the experience for
	* */
	public int getExperience(String crop){
			return 0;
	}


	/**
	 * Adds experience to the plant. If the plant has no experience amount
	 * it is created.
	 * @param crop the crop to add experience onto
	 * @param exp the amount of experience to be added
	 * @return a positive number iff experience was added
	 * */
		int value;
		if (crop.equals(MEMES)){
			return 0;
		}
		if (exp <= 0) {
			return 0;
		try {
			value = this.experienceMap.get(crop);
			value = value + exp;
		} catch (NullPointerException e){
			value = exp;
		}
		this.experienceMap.put(crop, value);
	}

	/**
	 * Removes experience from the crop. If this makes the total
	 * experience < 0 it sets the total to 0
	 * @param crop the crop to have experience removed from
	 * @param exp the amount of experience to be removed
	 * @return Return non-zero iff experience was removed*/
	public int removeExperience(String crop, int exp){
		Boolean cropFound = experienceMap.containsKey(crop);
		} else if (!cropFound) {
			return 0;
		}
		int cropExp = experienceMap.get(crop) - exp;
		if (cropExp < 0){
			cropExp = 0;
		}
		experienceMap.put(crop, cropExp);
		return 1;
	}

	/**
	 * @param crop the crop to get the level of
	 * @return The level of the crop
	 * */
	public int getLevel(String crop){
		int i = getExperience(crop);
	}

	/**
	 * Gets the percentage of experience needed to get to the next level
	 * @param crop The crop to get the percentage for
	 * @return An int from 0 - 100 representing the percentage progress to the
	 * next level, or 0 if invalid*/
	public int getExperiencePercentage(String crop){
		/*Returning 0 for an invalid selection is a feature not a bug!!*/
		double exp = this.getExperience(crop);
		if (exp <= 0){
			return 0;
		}
		int level = this.getLevel(crop);
		return r;
	}

	/**
	 * Creates the LevelerWindow
	 * @param pane the pane to create the window in
	 * @return the Batman (window)
	 */
	public LevelerWindow createWindow(Pane pane){
		LevelerWindow window = new LevelerWindow(pane);
		windowController = window.getController();
		windowController.invertMinification();
		return window;
	}

	public List<String> getCrops(){
		ArrayList<String> ret = new ArrayList<>();
		ret.addAll(experienceMap.keySet());
		return ret;
	}

	public LevelerWindowController getController(){
		return windowController;
	}
}