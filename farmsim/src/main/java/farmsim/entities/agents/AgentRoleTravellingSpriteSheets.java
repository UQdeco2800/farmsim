package farmsim.entities.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import farmsim.tiles.TileRegister;
import farmsim.util.Animation.SpriteSheet;

/**
 * Class for handling all spriteSheets need for
 */
public class AgentRoleTravellingSpriteSheets {
    private static final int FRAME_DURATION = 200;
    private static final int SPRITESHEET_WIDTH = 4;
    private static final int SPRITESHEET_HEIGHT = 7;
    // The four walking sprite sheets for every class
    private SpriteSheet spriteSheet;
    private HashMap<String, SpriteSheet> diseasedSpriteSheets;

    public AgentRoleTravellingSpriteSheets(String gender, Agent.RoleType roleType) {
        spriteSheet = initSpriteSheet(roleType.spriteSheetName() + gender);
        diseasedSpriteSheets = new HashMap<String, SpriteSheet>();
        ArrayList<String> diseaseNames = new ArrayList<String>(Arrays.asList(
        		"BlackPlague", "Influenza", "Measles", "Sars"));
        // Not all roles have specific sprites.  This makes sure only available sprites are used
        ArrayList<String> roleNamesWithDiseaseSprites = new ArrayList<String>(Arrays.asList(
        		"animalhandler", "builder", "farmer", "hunter"));
        for (String name : diseaseNames) {
        	if (roleNamesWithDiseaseSprites.contains(roleType.spriteSheetName())) {
        		diseasedSpriteSheets.put(name,
                		initSpriteSheet(
                                roleType.spriteSheetName() + gender + name));
        	} else {
        		diseasedSpriteSheets.put(name,
                		initSpriteSheet("farmer" + gender + name)); 
        	}
        	
        }
    }

    public static SpriteSheet initSpriteSheet(String tileName) {
        TileRegister register = TileRegister.getInstance();
        return new SpriteSheet(register.getTileImage(tileName),
                SPRITESHEET_WIDTH, SPRITESHEET_HEIGHT, FRAME_DURATION);
    }

    public SpriteSheet getSpriteSheet(String diseaseName) {
        if (diseaseName != null) {
            return diseasedSpriteSheets.get(diseaseName);
        } else {
            return spriteSheet;
        }
    }
}
