package farmsim.tiles;

import farmsim.util.ImageHelper;
import farmsim.world.World;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static farmsim.util.ImageHelper.blend;
import static farmsim.util.ImageHelper.subtract;

/**
 * The Tile class holds information about an instance of a tile -- however there
 * is information that is common to every Tile of a certain type (for example,
 * their name, sprite image, type ID, etc). This class manages information of a
 * tile type and removes the requirement of having to know or store the tile
 * type itself. It allows for easy retrieval of tile type information either
 * through the name (as a string) or through the tile type which can be
 * retrieved from the instance method Tile.getTileType().
 * <p>
 * This class is a singleton -- to get an instance of this class, you must call
 * TileRegister.getInstance(); you cannot instantiate it.
 *
 * @author Anonymousthing
 */
public class TileRegister {

    public static final int TILE_SIZE = 32;
    private static TileRegister INSTANCE = new TileRegister();
    /*
     * We have two registers that have the same values; one is keyed by name,
     * and the other is keyed by tile type. It adds a bit of redundancy
     * regarding TileTypeInfo fields but makes it easy to retrieve the
     * TileTypeInfo since we can retrieve via either key.
     */
    // As we're a static instance we use a ConcurrentHashMap to prevent
    // potential threading issues
    private ConcurrentHashMap<Integer, TileTypeInfo> tileTypeRegister;
    private ConcurrentHashMap<String, TileTypeInfo> nameRegister;
    private int currentCount;

    /**
     * Returns the instance of {@link TileRegister}.
     *
     * @return Returns an instance of TileRegister.
     */
    public static TileRegister getInstance() {
        return INSTANCE;
    }

    public static void setInstance(TileRegister tileRegister) {
        INSTANCE = tileRegister;
    }

    public void registerTiles() {
        tileTypeRegister = new ConcurrentHashMap<>();
        nameRegister = new ConcurrentHashMap<>();
        currentCount = 0;

        addTile("empty", "/empty.png", Color.BLACK);

        addTerrainTile("dirt", "/ground/dirt.png", Color.BROWN);
        addTerrainTile("arid", "/ground/arid.png", Color.TAN);
        addTerrainTile("grass", "/ground/grass.png",
                new Color(0.5, 0.67, 0.29, 1.0));
        addTerrainTile("grass2", "/ground/grass2.png",
                new Color(0.51, 0.68, 0.32, 1.0));
        addTerrainTile("grass3", "/ground/grass3.png",
                new Color(0.53, 0.71, 0.33, 1.0));

        addTile("snow", "/ground/snow.png", Color.WHITE);
        addTile("ice", "/weather/ice1.png", new Color(0.9, 1.0, 0.9, 1.0));

        addTile("gold", "/gold.png", Color.GOLD);

        // Agent/Worker spritesheets
        Color diseased = new Color(0.3, 0.9, 0.1, 1.0);
        //Farmer
        addTile("farmerMale", "/agents/farmerMale.png", Color.YELLOW);
        addTile("farmerFemale", "/agents/farmerFemale.png", Color.PURPLE);
        addTile("farmerMaleWave", "/agents/farmerwavespritesheet.png",
                Color.YELLOW);
        addTile("farmerFemaleWave", "/agents/femalefarmerwavespritesheet.png",
                Color
                        .PURPLE);
        addTile("farmerMaleBlackPlague", "/agents/" +
        		"farmerMaleBlackPlague.png", diseased);
        addTile("farmerMaleInfluenza", "/agents/" +
        		"farmerMaleInfluenza.png", diseased);
        addTile("farmerMaleMeasles", "/agents/" +
        		"farmerMaleMeasles.png", diseased);
        addTile("farmerMaleSars", "/agents/" +
        		"farmerMaleSars.png", diseased);
        addTile("farmerFemaleBlackPlague", "/agents/" +
        		"farmerFemaleBlackPlague.png", diseased);
        addTile("farmerFemaleInfluenza", "/agents/" +
        		"farmerFemaleInfluenza.png", diseased);
        addTile("farmerFemaleMeasles", "/agents/" +
        		"farmerFemaleMeasles.png", diseased);
        addTile("farmerFemaleSars", "/agents/" +
        		"farmerFemaleSars.png", diseased);


        addTile("builderMale", "/agents/builderMale.png", Color.YELLOW);
        addTile("builderFemale", "/agents/builderFemale.png", Color.PURPLE);
        addTile("builderMaleWave", "/agents/builderwavespritesheet.png",
                Color.YELLOW);
        addTile("builderFemaleWave", "/agents/femalebuilderwavespritesheet.png",
                Color
                        .PURPLE);
        addTile("builderMaleBlackPlague", "/agents/" +
        		"builderMaleBlackPlague.png", diseased);
        addTile("builderMaleInfluenza", "/agents/" +
        		"builderMaleInfluenza.png", diseased);
        addTile("builderMaleMeasles", "/agents/" +
        		"builderMaleMeasles.png", diseased);
        addTile("builderMaleSars", "/agents/" +
        		"builderMaleSars.png", diseased);
        addTile("builderFemaleBlackPlague", "/agents/" +
        		"builderFemaleBlackPlague.png", diseased);
        addTile("builderFemaleInfluenza", "/agents/" +
        		"builderFemaleInfluenza.png", diseased);
        addTile("builderFemaleMeasles", "/agents/" +
        		"builderFemaleMeasles.png", diseased);
        addTile("builderFemaleSars", "/agents/" +
        		"builderFemaleSars.png", diseased);

        addTile("animalhandlerMale", "/agents/animalhandlerMale.png",
                Color.YELLOW);
        addTile("animalhandlerFemale", "/agents/animalhandlerFemale.png",
                Color.PURPLE);
        addTile("animalhandlerMaleWave",
                "/agents/animalhandlerwavespritesheet.png",
                Color.YELLOW);
        addTile("animalhandlerFemaleWave",
                "/agents/femaleanimalhandlerwavespritesheet.png",
                Color
                        .PURPLE);
        addTile("animalhandlerMaleBlackPlague", "/agents/" +
                "animalhandlerMaleBlackPlague.png", diseased);
        addTile("animalhandlerMaleInfluenza", "/agents/" +
                "animalhandlerMaleInfluenza.png", diseased);
        addTile("animalhandlerMaleMeasles", "/agents/" +
                "animalhandlerMaleMeasles.png", diseased);
        addTile("animalhandlerMaleSars", "/agents/" +
                "animalhandlerMaleSars.png", diseased);
        addTile("animalhandlerFemaleBlackPlague", "/agents/" +
                "animalhandlerFemaleBlackPlague.png", diseased);
        addTile("animalhandlerFemaleInfluenza", "/agents/" +
                "animalhandlerFemaleInfluenza.png", diseased);
        addTile("animalhandlerFemaleMeasles", "/agents/" +
                "animalhandlerFemaleMeasles.png", diseased);
        addTile("animalhandlerFemaleSars", "/agents/" +
                "animalhandlerFemaleSars.png", diseased);

        addTile("hunterMale",
                "/agents/hunterMale.png", Color.YELLOW);
        addTile("hunterFemale",
                "/agents/hunterFemale.png", Color.PURPLE);
        addTile("hunterMaleWave", "/agents/hunterwavespritesheet.png",
                Color.YELLOW);
        addTile("hunterFemaleWave", "/agents/femalehunterwavespritesheet.png",
                Color
                        .PURPLE);
      
        addTile("hunterMaleBlackPlague", "/agents/" +
                "hunterMaleBlackPlague.png", diseased);
        addTile("hunterMaleInfluenza", "/agents/" +
                "hunterMaleInfluenza.png", diseased);
        addTile("hunterMaleMeasles", "/agents/" +
                "hunterMaleMeasles.png", diseased);
        addTile("hunterMaleSars", "/agents/" +
                "hunterMaleSars.png", diseased);
        addTile("hunterFemaleBlackPlague", "/agents/" +
                "hunterFemaleBlackPlague.png", diseased);
        addTile("hunterFemaleInfluenza", "/agents/" +
                "hunterFemaleInfluenza.png", diseased);
        addTile("hunterFemaleMeasles", "/agents/" +
                "hunterFemaleMeasles.png", diseased);
        addTile("hunterFemaleSars", "/agents/" +
                "hunterFemaleSars.png", diseased);

        addTerrainTile("ploughed_dirt", "/ground/ploughed_dirt.png",
                Color.BROWN);
        addTerrainTile("seed", "/crops/seed.png", Color.GREEN);
        addTile("plant", "/crops/plant.png", Color.GREENYELLOW);
        addTerrainTile("crop_circle_dirt", "/ground/crop_circle_dirt.png",
                Color.BROWN);

        addTile("predator", "/predator.png", Color.GREY);
        addTile("predatorWolf", "/wolf.png", Color.GREY);


        addTile("wolfUpLeft0", "/predators/wolf/UpLeft0.png", Color.GREY);
        addTile("wolfUpLeft1", "/predators/wolf/UpLeft1.png", Color.GREY);
        addTile("wolfUpRight0", "/predators/wolf/UpRight0.png", Color.GREY);
        addTile("wolfUpRight1", "/predators/wolf/UpRight1.png", Color.GREY);
        addTile("wolfDownLeft0", "/predators/wolf/DownLeft0.png", Color.GREY);
        addTile("wolfDownLeft1", "/predators/wolf/DownLeft1.png", Color.GREY);
        addTile("wolfDownRight0", "/predators/wolf/DownRight0.png", Color.GREY);
        addTile("wolfDownRight1", "/predators/wolf/DownRight1.png", Color.GREY);

        addTile("bearUpLeft0", "/predators/bear/upLeft0.png", Color.GREY);
        addTile("bearUpLeft1", "/predators/bear/upLeft1.png", Color.GREY);
        addTile("bearUpRight0", "/predators/bear/upRight0.png", Color.GREY);
        addTile("bearUpRight1", "/predators/bear/upRight1.png", Color.GREY);
        addTile("bearDownLeft0", "/predators/bear/downLeft0.png", Color.GREY);
        addTile("bearDownLeft1", "/predators/bear/downLeft1.png", Color.GREY);
        addTile("bearDownRight0", "/predators/bear/downRight0.png", Color.GREY);
        addTile("bearDownRight1", "/predators/bear/downRight1.png", Color.GREY);

        addTile("moleUpLeft0", "/predators/mole/mole.png", Color.GREY);
        addTile("moleUpLeft1", "/predators/mole/mole1.png", Color.GREY);
        addTile("moleUpRight0", "/predators/mole/mole.png", Color.GREY);
        addTile("moleUpRight1", "/predators/mole/mole1.png", Color.GREY);
        addTile("moleDownLeft0", "/predators/mole/mole.png", Color.GREY);
        addTile("moleDownLeft1", "/predators/mole/mole1.png", Color.GREY);
        addTile("moleDownRight0", "/predators/mole/mole.png", Color.GREY);
        addTile("moleDownRight1", "/predators/mole/mole1.png", Color.GREY);

        addTile("rabbitUpLeft0", "/predators/rabbit/left0.png", Color.GREY);
        addTile("rabbitUpLeft1", "/predators/rabbit/left1.png", Color.GREY);
        addTile("rabbitUpRight0", "/predators/rabbit/right0.png", Color.GREY);
        addTile("rabbitUpRight1", "/predators/rabbit/right1.png", Color.GREY);
        addTile("rabbitDownLeft0", "/predators/rabbit/left0.png", Color.GREY);
        addTile("rabbitDownLeft1", "/predators/rabbit/left1.png", Color.GREY);
        addTile("rabbitDownRight0", "/predators/rabbit/right0.png", Color.GREY);
        addTile("rabbitDownRight1", "/predators/rabbit/right1.png", Color.GREY);

        addTile("alligatorUpLeft0", "/predators/alligator/upLeft0.png", Color.GREY);
        addTile("alligatorUpLeft1", "/predators/alligator/upLeft1.png", Color.GREY);
        addTile("alligatorUpRight0", "/predators/alligator/upRight0.png", Color.GREY);
        addTile("alligatorUpRight1", "/predators/alligator/upRight1.png", Color.GREY);
        addTile("alligatorDownLeft0", "/predators/alligator/downLeft0.png", Color.GREY);
        addTile("alligatorDownLeft1", "/predators/alligator/downLeft1.png", Color.GREY);
        addTile("alligatorDownRight0", "/predators/alligator/downRight0.png", Color.GREY);
        addTile("alligatorDownRight1", "/predators/alligator/downRight1.png", Color.GREY);

        addTile("fire", "/fire.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("fire2", "/fire2.png", new Color(1.0, 0.15, 0.1, 1.0));
        addTile("fire3", "/fire3.png", new Color(1.0, 0.05, 0, 1.0));
        
        //Tools
        addTile("axe", "/tools/axe.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("fishingrod", "/tools/fishingrod.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("hammer", "/tools/hammer.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("hoe", "/tools/hoe_bubble.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("pickaxe", "/tools/pickaxe_bubble.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("shovel", "/tools/shovel_bubble.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("sickle", "/tools/shears_bubble.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("shears", "/tools/shears_bubble.png", new Color(1.0, 0.1, 0.1, 1.0));
        addTile("watering_can", "/tools/bucket.png", new Color(1.0, 0.1, 0.1, 1.0));
        
        //Machines
        addTile("tractor", "/machines/tractor.png", new Color(1.0, 0.1, 0.1, 1.0));

        // Unusable Tiles
        addTile("tree", "/crops/tree.png", Color.DARKGREEN);
        addTile("treeAutumn", "/crops/treeAutumn1.png", Color.DARKRED);
        addTile("treeAutumn2", "/crops/treeAutumn2.png", Color.GOLD);
        addTile("treeWinter", "/crops/treeWinter.png", Color.LIGHTGREY);
        addTile("treeWinter2", "/crops/treeWinter2.png", Color.LIGHTGREY);


        //overlay tiles

        addTile("zero", "/Opacity/zero.png", Color.LIGHTGREY);
        addTile("ten", "/Opacity/ten.png", Color.LIGHTGREY);
        addTile("twenty", "/Opacity/twenty.png", Color.LIGHTGREY);
        addTile("thirty", "/Opacity/thirty.png", Color.LIGHTGREY);
        addTile("forty", "/Opacity/forty.png", Color.LIGHTGREY);
        addTile("fifty", "/Opacity/fifty.png", Color.LIGHTGREY);
        addTile("sixty", "/Opacity/sixty.png", Color.LIGHTGREY);
        addTile("seventy", "/Opacity/seventy.png", Color.LIGHTGREY);
        addTile("eighty", "/Opacity/eighty.png", Color.LIGHTGREY);
        addTile("ninety", "/Opacity/ninety.png", Color.LIGHTGREY);
        addTile("full", "/Opacity/full.png", Color.LIGHTGREY);

        addTile("rocks", "/environment/rocks.png", Color.SLATEGREY);

        addTile("building", "/transparent.png", Color.BEIGE);

        addTile("buildingCaution", "/buildings/caution.png", Color.YELLOWGREEN);
        addTile("buildingCautionBlank", "/buildings/cautionblank.png",
                Color.YELLOWGREEN);
        addTile("buildingCautionTape", "/buildings/cautiontape.png",
                Color.YELLOWGREEN);
        addTile("buildingRP1", "/buildings/rockandplantfirst.png",
                Color.YELLOWGREEN);
        addTile("buildingRP2", "/buildings/rockandplantsecond.png",
                Color.YELLOWGREEN);

        // plant Graphics

        addTile("Sugarcane1", "/crops/Sugarcane1.png", Color.YELLOWGREEN);
        addTile("Sugarcane2", "/crops/Sugarcane2.png", Color.YELLOWGREEN);
        addTile("Sugarcane3", "/crops/Sugarcane3.png", Color.YELLOWGREEN);
        addTile("Sugarcane1dead", "/crops/Sugarcane1dead.png", Color.BROWN);
        addTile("Sugarcane2dead", "/crops/Sugarcane2dead.png", Color.BROWN);
        addTile("Sugarcane3dead", "/crops/Sugarcane3dead.png", Color.BROWN);
        

        addTile("Apple1", "/crops/Apple1.png", Color.RED);
        addTile("Apple2", "/crops/Apple2.png", Color.RED);
        addTile("Apple3", "/crops/Apple3.png", Color.RED);
        addTile("Apple1dead", "/crops/Apple1dead.png", Color.BROWN);
        addTile("Apple2dead", "/crops/Apple2dead.png", Color.BROWN);
        addTile("Apple3dead", "/crops/Apple3dead.png", Color.BROWN);

        addTile("Banana1", "/crops/Banana1.png", Color.YELLOW);
        addTile("Banana2", "/crops/Banana2.png", Color.YELLOW);
        addTile("Banana3", "/crops/Banana3.png", Color.YELLOW);
        addTile("Banana1dead", "/crops/Banana1dead.png", Color.BROWN);
        addTile("Banana2dead", "/crops/Banana2dead.png", Color.BROWN);
        addTile("Banana3dead", "/crops/Banana3dead.png", Color.BROWN);

        addTile("Corn1", "/crops/Corn1.png", Color.YELLOW);
        addTile("Corn2", "/crops/Corn2.png", Color.YELLOW);
        addTile("Corn3", "/crops/Corn3.png", Color.YELLOW);
        addTile("Corn1dead", "/crops/Corn1dead.png", Color.BROWN);
        addTile("Corn2dead", "/crops/Corn2dead.png", Color.BROWN);
        addTile("Corn3dead", "/crops/Corn3dead.png", Color.BROWN);

        addTile("Strawberry1", "/crops/Strawberry1.png", Color.RED);
        addTile("Strawberry2", "/crops/Strawberry2.png", Color.RED);
        addTile("Strawberry3", "/crops/Strawberry3.png", Color.RED);
        addTile("Strawberry1dead", "/crops/Strawberry1dead.png", Color.BROWN);
        addTile("Strawberry2dead", "/crops/Strawberry2dead.png", Color.BROWN);
        addTile("Strawberry3dead", "/crops/Strawberry3dead.png", Color.BROWN);

        addTile("Pear1", "/crops/Pear1.png", Color.GREENYELLOW);
        addTile("Pear2", "/crops/Pear2.png", Color.GREENYELLOW);
        addTile("Pear3", "/crops/Pear3.png", Color.GREENYELLOW);
        addTile("Pear1dead", "/crops/Pear1dead.png", Color.BROWN);
        addTile("Pear2dead", "/crops/Pear2dead.png", Color.BROWN);
        addTile("Pear3dead", "/crops/Pear3dead.png", Color.BROWN);

        addTile("Mango1", "/crops/Mango1.png", Color.ORANGERED);
        addTile("Mango2", "/crops/Mango2.png", Color.ORANGERED);
        addTile("Mango3", "/crops/Mango3.png", Color.ORANGERED);
        addTile("Mango1dead", "/crops/Mango1dead.png", Color.BROWN);
        addTile("Mango2dead", "/crops/Mango2dead.png", Color.BROWN);
        addTile("Mango3dead", "/crops/Mango3dead.png", Color.BROWN);

        addTile("Lettuce1", "/crops/Lettuce1.png", Color.LIGHTGREEN);
        addTile("Lettuce2", "/crops/Lettuce2.png", Color.LIGHTGREEN);
        addTile("Lettuce3", "/crops/Lettuce3.png", Color.LIGHTGREEN);
        addTile("Lettuce1dead", "/crops/Lettuce1dead.png", Color.BROWN);
        addTile("Lettuce2dead", "/crops/Lettuce2dead.png", Color.BROWN);
        addTile("Lettuce3dead", "/crops/Lettuce3dead.png", Color.BROWN);

        addTile("Lemon1", "/crops/Lemon1.png", Color.YELLOW);
        addTile("Lemon2", "/crops/Lemon2.png", Color.YELLOW);
        addTile("Lemon3", "/crops/Lemon3.png", Color.YELLOW);
        addTile("Lemon1dead", "/crops/Lemon1dead.png", Color.BROWN);
        addTile("Lemon2dead", "/crops/Lemon2dead.png", Color.BROWN);
        addTile("Lemon3dead", "/crops/Lemon3dead.png", Color.BROWN);

        addTile("Cotton1", "/crops/Cotton1.png", Color.LIGHTGREY);
        addTile("Cotton2", "/crops/Cotton2.png", Color.LIGHTGREY);
        addTile("Cotton3", "/crops/Cotton3.png", Color.LIGHTGREY);
        addTile("Cotton1dead", "/crops/Cotton1dead.png", Color.BROWN);
        addTile("Cotton2dead", "/crops/Cotton2dead.png", Color.BROWN);
        addTile("Cotton3dead", "/crops/Cotton3dead.png", Color.BROWN);

        addTile("deadcrop", "/crops/deadcrop.png", Color.BROWN);
        addTile("water", "/environment/water.png", Color.BLUE);
        addTile("waterPolluted", "/environment/waterPolluted.png",
                Color.CHARTREUSE);
        addTile("waterPolluted2", "/environment/waterPolluted2.png",
                Color.CHARTREUSE);

        addTileDirections("water", "/environment/water.png", 0.1, Color.BLUE);

        // Selection Graphics
        addTile("selectionLT", "/selection/selectionLT.png",
                Color.YELLOW); // Left Top
        addTile("selectionRT", "/selection/selectionRT.png",
                Color.YELLOW); // Right Top
        addTile("selectionCT", "/selection/selectionCT.png",
                Color.YELLOW); // Centre Top
        addTile("selectionLB", "/selection/selectionLB.png",
                Color.YELLOW); // Left Bottom
        addTile("selectionRB", "/selection/selectionRB.png",
                Color.YELLOW); // Right Bottom
        addTile("selectionCB", "/selection/selectionCB.png",
                Color.YELLOW); // Centre Bottom
        addTile("selectionLC", "/selection/selectionLC.png",
                Color.YELLOW); // Left Centre
        addTile("selectionRC", "/selection/selectionRC.png",
                Color.YELLOW); // Right Centre
        addTile("selectionC", "/selection/selectionC.png",
                Color.YELLOW); // Centre
        addTile("selectionS", "/selection/selectionS.png",
                Color.YELLOW); // Single
        addTile("selectionSCT", "/selection/selectionSCT.png",
                Color.YELLOW); // Single Column
        // Top
        addTile("selectionSCC", "/selection/selectionSCC.png",
                Color.YELLOW); // Single Column
        // Centre
        addTile("selectionSCB", "/selection/selectionSCB.png",
                Color.YELLOW); // Single Column
        // Bottom
        addTile("selectionSRL", "/selection/selectionSRL.png",
                Color.YELLOW); // Single Row
        // Left
        addTile("selectionSRC", "/selection/selectionSRC.png",
                Color.YELLOW); // Single Row
        // Centre
        addTile("selectionSRR", "/selection/selectionSRR.png",
                Color.YELLOW); // Single Row
        // Right

        /* ----- FarmAnimal Graphics ----- */

        // Heart
        addTile("heart8", "/farmanimals/heart8.png", Color.RED);
        addTile("heart16", "/farmanimals/heart16.png", Color.RED);

        /* Cow */

        // Adult
        addTile("cowDownLeft0", "/farmanimals/cow/DownLeft0.png",
                Color.DARKGREY);
        addTile("cowDownLeft1", "/farmanimals/cow/DownLeft1.png",
                Color.DARKGREY);
        addTile("cowDownRight0", "/farmanimals/cow/DownRight0.png",
                Color.DARKGREY);
        addTile("cowDownRight1", "/farmanimals/cow/DownRight1.png",
                Color.DARKGREY);
        addTile("cowUpLeft0", "/farmanimals/cow/UpLeft0.png", Color.DARKGREY);
        addTile("cowUpLeft1", "/farmanimals/cow/UpLeft1.png", Color.DARKGREY);
        addTile("cowUpRight0", "/farmanimals/cow/UpRight0.png", Color.DARKGREY);
        addTile("cowUpRight1", "/farmanimals/cow/UpRight1.png", Color.DARKGREY);

        // Dead Adult
        addTile("cowDead0", "/farmanimals/cow/dead0.png", Color.DARKGREY);
        addTile("cowDead1", "/farmanimals/cow/dead1.png", Color.DARKGREY);
        addTile("cowDead2", "/farmanimals/cow/dead2.png", Color.DARKGREY);

        // Baby
        addTile("cowBabyDownLeft0", "/farmanimals/cow/babyDownLeft0.png",
                Color.DARKGREY);
        addTile("cowBabyDownLeft1", "/farmanimals/cow/babyDownLeft1.png",
                Color.DARKGREY);
        addTile("cowBabyDownRight0", "/farmanimals/cow/babyDownRight0.png",
                Color.DARKGREY);
        addTile("cowBabyDownRight1", "/farmanimals/cow/babyDownRight1.png",
                Color.DARKGREY);
        addTile("cowBabyUpLeft0", "/farmanimals/cow/babyUpLeft0.png",
                Color.DARKGREY);
        addTile("cowBabyUpLeft1", "/farmanimals/cow/babyUpLeft1.png",
                Color.DARKGREY);
        addTile("cowBabyUpRight0", "/farmanimals/cow/babyUpRight0.png",
                Color.DARKGREY);
        addTile("cowBabyUpRight1", "/farmanimals/cow/babyUpRight1.png",
                Color.DARKGREY);
        
        /* Pig */

        // Adult
        addTile("pigDownLeft0", "/farmanimals/pig/DownLeft0.png", Color.PINK);
        addTile("pigDownLeft1", "/farmanimals/pig/DownLeft1.png", Color.PINK);
        addTile("pigDownRight0", "/farmanimals/pig/DownRight0.png", Color.PINK);
        addTile("pigDownRight1", "/farmanimals/pig/DownRight1.png", Color.PINK);
        addTile("pigUpLeft0", "/farmanimals/pig/UpLeft0.png", Color.PINK);
        addTile("pigUpLeft1", "/farmanimals/pig/UpLeft1.png", Color.PINK);
        addTile("pigUpRight0", "/farmanimals/pig/UpRight0.png", Color.PINK);
        addTile("pigUpRight1", "/farmanimals/pig/UpRight1.png", Color.PINK);

        // Dead Adult
        addTile("pigDead0", "/farmanimals/pig/dead0.png", Color.PINK);
        addTile("pigDead1", "/farmanimals/pig/dead1.png", Color.PINK);
        addTile("pigDead2", "/farmanimals/pig/dead2.png", Color.PINK);

        // Baby
        addTile("pigBabyDownLeft0", "/farmanimals/pig/babyDownLeft0.png",
                Color.PINK);
        addTile("pigBabyDownLeft1", "/farmanimals/pig/babyDownLeft1.png",
                Color.PINK);
        addTile("pigBabyDownRight0", "/farmanimals/pig/babyDownRight0.png",
                Color.PINK);
        addTile("pigBabyDownRight1", "/farmanimals/pig/babyDownRight1.png",
                Color.PINK);
        addTile("pigBabyUpLeft0", "/farmanimals/pig/babyUpLeft0.png",
                Color.PINK);
        addTile("pigBabyUpLeft1", "/farmanimals/pig/babyUpLeft1.png",
                Color.PINK);
        addTile("pigBabyUpRight0", "/farmanimals/pig/babyUpRight0.png",
                Color.PINK);
        addTile("pigBabyUpRight1", "/farmanimals/pig/babyUpRight1.png",
                Color.PINK);

        /* Sheep */

        // Adult
        addTile("sheepDownLeft0", "/farmanimals/sheep/DownLeft0.png",
                Color.LIGHTGREY);
        addTile("sheepDownLeft1", "/farmanimals/sheep/DownLeft1.png",
                Color.LIGHTGREY);
        addTile("sheepDownRight0", "/farmanimals/sheep/DownRight0.png",
                Color.LIGHTGREY);
        addTile("sheepDownRight1", "/farmanimals/sheep/DownRight1.png",
                Color.LIGHTGREY);
        addTile("sheepUpLeft0", "/farmanimals/sheep/UpLeft0.png",
                Color.LIGHTGREY);
        addTile("sheepUpLeft1", "/farmanimals/sheep/UpLeft1.png",
                Color.LIGHTGREY);
        addTile("sheepUpRight0", "/farmanimals/sheep/UpRight0.png",
                Color.LIGHTGREY);
        addTile("sheepUpRight1", "/farmanimals/sheep/UpRight1.png",
                Color.LIGHTGREY);

        // Dead Adult
        addTile("sheepDead0", "/farmanimals/sheep/dead0.png", Color.LIGHTGREY);
        addTile("sheepDead1", "/farmanimals/sheep/dead1.png", Color.LIGHTGREY);
        addTile("sheepDead2", "/farmanimals/sheep/dead2.png", Color.LIGHTGREY);

        // Baby
        addTile("sheepBabyDownLeft0", "/farmanimals/sheep/babyDownLeft0.png",
                Color.LIGHTGREY);
        addTile("sheepBabyDownLeft1", "/farmanimals/sheep/babyDownLeft1.png",
                Color.LIGHTGREY);
        addTile("sheepBabyDownRight0", "/farmanimals/sheep/babyDownRight0.png",
                Color.LIGHTGREY);
        addTile("sheepBabyDownRight1", "/farmanimals/sheep/babyDownRight1.png",
                Color.LIGHTGREY);
        addTile("sheepBabyUpLeft0", "/farmanimals/sheep/babyUpLeft0.png",
                Color.LIGHTGREY);
        addTile("sheepBabyUpLeft1", "/farmanimals/sheep/babyUpLeft1.png",
                Color.LIGHTGREY);
        addTile("sheepBabyUpRight0", "/farmanimals/sheep/babyUpRight0.png",
                Color.LIGHTGREY);
        addTile("sheepBabyUpRight1", "/farmanimals/sheep/babyUpRight1.png",
                Color.LIGHTGREY);

        /* Duck */

        addTile("duckDownLeft0", "/farmanimals/duck/downLeft0.png",
                Color.YELLOW);
        addTile("duckDownLeft1", "/farmanimals/duck/downLeft1.png",
                Color.YELLOW);
        addTile("duckDownRight0", "/farmanimals/duck/downRight0.png",
                Color.YELLOW);
        addTile("duckDownRight1", "/farmanimals/duck/downRight1.png",
                Color.YELLOW);
        addTile("duckUpLeft0", "/farmanimals/duck/upLeft0.png",
                Color.YELLOW);
        addTile("duckUpLeft1", "/farmanimals/duck/upLeft1.png",
                Color.YELLOW);
        addTile("duckUpRight0", "/farmanimals/duck/upRight0.png",
                Color.YELLOW);
        addTile("duckUpRight1", "/farmanimals/duck/upRight1.png",
                Color.YELLOW);

        // Dead Adult
        addTile("duckDead0", "/farmanimals/duck/downLeft0.png", Color.YELLOW);
        addTile("duckDead1", "/farmanimals/duck/downLeft0.png", Color.YELLOW);
        addTile("duckDead2", "/farmanimals/duck/downLeft0.png", Color.YELLOW);

        /* Chicken */

        addTile("chickenDownLeft0", "/farmanimals/chicken/downLeft0.png",
                Color.WHITE);
        addTile("chickenDownLeft1", "/farmanimals/chicken/downLeft1.png",
                Color.WHITE);
        addTile("chickenDownRight0", "/farmanimals/chicken/downRight0.png",
                Color.WHITE);
        addTile("chickenDownRight1", "/farmanimals/chicken/downRight1.png",
                Color.WHITE);
        addTile("chickenUpLeft0", "/farmanimals/chicken/upLeft0.png",
                Color.WHITE);
        addTile("chickenUpLeft1", "/farmanimals/chicken/upLeft1.png",
                Color.WHITE);
        addTile("chickenUpRight0", "/farmanimals/chicken/upRight0.png",
                Color.WHITE);
        addTile("chickenUpRight1", "/farmanimals/chicken/upRight1.png",
                Color.WHITE);

        // Dead Adult
        addTile("chickenDead0", "/farmanimals/chicken/dead0.png", Color.WHITE);
        addTile("chickenDead1", "/farmanimals/chicken/dead1.png", Color.WHITE);
        addTile("chickenDead2", "/farmanimals/chicken/dead2.png", Color.WHITE);


        /* Fencing */
        addTile("fenceBL", "/fencing/bottomleftcorner.png", Color.BROWN);
        addTile("fenceBR", "/fencing/bottomrightcorner.png", Color.BROWN);
        addTile("fenceH", "/fencing/horizontal.png", Color.BROWN);
        addTile("fenceP", "/fencing/post.png", Color.BROWN);
        addTile("fenceTL", "/fencing/topleftcorner.png", Color.BROWN);
        addTile("fenceTR", "/fencing/toprightcorner.png", Color.BROWN);
        addTile("fenceV", "/fencing/verticalfence.png", Color.BROWN);
        addTile("fenceT", "/fencing/top.png", Color.BROWN);
        addTile("fenceGateClosed", "/fencing/gate.png", Color.BROWN);
        addTile("fenceGateOpen", "/fencing/opengate.png", Color.BROWN);




        addTile("left", "/left.png", Color.ALICEBLUE);
        addTile("right", "/right.png", Color.ALICEBLUE);
        addTile("up", "/up.png", Color.ALICEBLUE);
        addTile("down", "/down.png", Color.ALICEBLUE);
    }

    /**
     * Adds a tile with a gradient overlay to the register for each subset of
     * directions
     *
     * @param name          The name of the tile
     * @param tileImageName The file name of the corresponding image
     */
    private void addTileDirections(String name, String tileImageName,
                                   double bias, Color pixelColour) {

        addTile(name + "N", tileImageName, "N", bias, pixelColour);
        addTile(name + "E", tileImageName, "E", bias, pixelColour);
        addTile(name + "S", tileImageName, "S", bias, pixelColour);
        addTile(name + "W", tileImageName, "W", bias, pixelColour);

        addTile(name + "NE", tileImageName, "NE", bias, pixelColour);
        addTile(name + "NS", tileImageName, "NS", bias, pixelColour);
        addTile(name + "NW", tileImageName, "NW", bias, pixelColour);
        addTile(name + "ES", tileImageName, "ES", bias, pixelColour);
        addTile(name + "EW", tileImageName, "EW", bias, pixelColour);
        addTile(name + "SW", tileImageName, "SW", bias, pixelColour);

        addTile(name + "ESW", tileImageName, "ESW", bias, pixelColour);
        addTile(name + "NSW", tileImageName, "NSW", bias, pixelColour);
        addTile(name + "NEW", tileImageName, "NEW", bias, pixelColour);
        addTile(name + "NES", tileImageName, "NES", bias, pixelColour);

        addTile(name + "NESW", tileImageName, "NESW", bias, pixelColour);

    }

    /**
     * Adds a tile with a gradient overlay to the register for a direction/s
     *
     * @param name          The name of the tile
     * @param tileImageName The file name of the corresponding image
     * @param directions    The directions to overlay the gradient in - NESW
     */
    private void addTile(String name, String tileImageName, String directions,
                         double bias, Color pixelColour) {
        if (nameRegister.containsKey(name)) {
            throw new RuntimeException(
                    "Attempted to add an already registered TileTypeInfo \""
                            + name + "\" to a TileRegister");
        }
        Image img = new Image(getClass().getResource(tileImageName).toString());
        BufferedImage buffer = SwingFXUtils.fromFXImage(img, null);
        Image grad = new Image(
                getClass().getResource("/overlays/grad.png").toString());
        BufferedImage gradBuffer = SwingFXUtils.fromFXImage(grad, null);

        if (directions.contains("N")) {
            buffer = blend(buffer, grad, bias);
        }
        if (directions.contains("E")) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.PI / 2, grad.getWidth() / 2,
                    grad.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(transform,
                    AffineTransformOp.TYPE_BILINEAR);
            gradBuffer = op.filter(gradBuffer, null);
            buffer = blend(buffer, gradBuffer, bias);
        }
        if (directions.contains("S")) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(2 * Math.PI / 2, grad.getWidth() / 2,
                    grad.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(transform,
                    AffineTransformOp.TYPE_BILINEAR);
            gradBuffer = op.filter(gradBuffer, null);
            buffer = blend(buffer, gradBuffer, bias);
        }
        if (directions.contains("W")) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(3 * Math.PI / 2, grad.getWidth() / 2,
                    grad.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(transform,
                    AffineTransformOp.TYPE_BILINEAR);
            gradBuffer = op.filter(gradBuffer, null);
            buffer = blend(buffer, gradBuffer, bias);
        }

        img = SwingFXUtils.toFXImage(buffer, null);

        TileTypeInfo tile =
                new TileTypeInfo(currentCount++, name, img, pixelColour);
        nameRegister.put(tile.tileName, tile);
        tileTypeRegister.put(tile.tileType, tile);
    }

    /**
     * Adds a tile to the register.
     *
     * @param name          The name of the tile
     * @param tileImageName The file name of the corresponding image
     */
    private TileTypeInfo addTile(String name, String tileImageName,
                                 Color pixelColour) {
        if (nameRegister.containsKey(name)) {
            throw new RuntimeException(
                    "Attempted to add an already registered TileTypeInfo \""
                            + name + "\" to a TileRegister");
        }
        TileTypeInfo tile = new TileTypeInfo(currentCount++, name,
                new Image(getClass().getResource(tileImageName).toString()),
                pixelColour);
        nameRegister.put(tile.tileName, tile);
        tileTypeRegister.put(tile.tileType, tile);
        return tile;
    }

    private void addTerrainTile(String name, String tileImageName,
                                Color pixelColour) {
        if (nameRegister.containsKey(name)) {
            throw new RuntimeException(
                    "Attempted to add an already registered TileTypeInfo \""
                            + name + "\" to a TileRegister");
        }
        TileTypeInfo tile = new TileTypeInfo(currentCount++, name,
                new Image(getClass().getResource(tileImageName).toString()),
                pixelColour);

        String[] directions = {"N", "E", "S", "W",
                "NE", "NS", "NW",
                "ES", "EW",
                "SW",
                "NSW", "NEW", "NES",
                "ESW",
                "NESW"};
        for (String direction : directions) {
            Image image =
                    new Image(getClass().getResource(tileImageName).toString());
            Image grad = new Image(
                    getClass().getResource("/overlays/elevation_top.png")
                            .toString());
            BufferedImage gradBuffer;
            if (direction.contains("N")) {
                image = SwingFXUtils
                        .toFXImage(subtract(image, grad, 0.05), null);
            }
            if (direction.contains("E")) {
                gradBuffer = SwingFXUtils.fromFXImage(grad, null);
                AffineTransform transform = new AffineTransform();
                transform.rotate(Math.PI / 2, image.getWidth() / 2,
                        image.getHeight() / 2);
                AffineTransformOp op = new AffineTransformOp(transform,
                        AffineTransformOp.TYPE_BILINEAR);
                gradBuffer = op.filter(gradBuffer, null);
                image = SwingFXUtils.toFXImage(
                        subtract(SwingFXUtils.fromFXImage(image, null),
                                gradBuffer, 0.1), null);
            }
            if (direction.contains("S")) {
                gradBuffer = SwingFXUtils.fromFXImage(grad, null);
                AffineTransform transform = new AffineTransform();
                transform.rotate(2 * Math.PI / 2, image.getWidth() / 2,
                        image.getHeight() / 2);
                AffineTransformOp op = new AffineTransformOp(transform,
                        AffineTransformOp.TYPE_BILINEAR);
                gradBuffer = op.filter(gradBuffer, null);
                image = SwingFXUtils.toFXImage(
                        subtract(SwingFXUtils.fromFXImage(image, null),
                                gradBuffer, 0.175), null);
            }
            if (direction.contains("W")) {
                gradBuffer = SwingFXUtils.fromFXImage(grad, null);
                AffineTransform transform = new AffineTransform();
                transform.rotate(3 * Math.PI / 2, image.getWidth() / 2,
                        image.getHeight() / 2);
                AffineTransformOp op = new AffineTransformOp(transform,
                        AffineTransformOp.TYPE_BILINEAR);
                gradBuffer = op.filter(gradBuffer, null);
                image = SwingFXUtils.toFXImage(
                        subtract(SwingFXUtils.fromFXImage(image, null),
                                gradBuffer, 0.1), null);
            }

            tile.addDirection(direction, image);
        }

        nameRegister.put(tile.tileName, tile);
        tileTypeRegister.put(tile.tileType, tile);
    }

    /**
     * Retrieves the Image of the specified tile
     *
     * @param name      The name of the tile
     * @param elevation The elevation of the tile - used for shading
     * @return The sprite Image of the tile
     */
    public Image getTileImage(String name, int elevation) {
        if (!nameRegister.containsKey(name))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo \"" + name
                            + "\" from a TileRegister");
        return nameRegister.get(name).elevationImages
                .get(elevation - World.MIN_ELEVATION);
    }

    /**
     * Retrieves the Image of the specified tile
     *
     * @param tileType  The tile type of the tile
     * @param elevation The elevation of the tile - used for shading
     * @return The sprite Image of the tile
     */
    public Image getTileImage(int tileType, int elevation) {
        if (!tileTypeRegister.containsKey(tileType))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo of tile type \""
                            + tileType + "\" from a TileRegister");
        return tileTypeRegister.get(tileType).elevationImages
                .get(elevation - World.MIN_ELEVATION);
    }

    /**
     * Retrieves the Image of the specified tile
     *
     * @param tileType The tile type of the tile
     * @return The sprite Image of the tile
     */
    public Image getTileImage(int tileType, int elevation, boolean N, boolean E,
                              boolean S,
                              boolean W) {
        if (!tileTypeRegister.containsKey(tileType))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo of tile type \""
                            + tileType + "\" from a TileRegister");

        String direction = "";
        if (N) {
            direction = direction.concat("N");
        }
        if (E) {
            direction = direction.concat("E");
        }
        if (S) {
            direction = direction.concat("S");
        }
        if (W) {
            direction = direction.concat("W");
        }
        if (N || E || S || W) {
            return tileTypeRegister.get(tileType).directionImages.get(direction)
                    .get(elevation - World.MIN_ELEVATION);
        } else {
            return getTileImage(tileType);
        }
    }

    /**
     * Retrieves the Image of the specified tile using default elevation
     *
     * @param name The name of the tile
     * @return The sprite Image of the tile
     */
    public Image getTileImage(String name) {
        if (!nameRegister.containsKey(name))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo \"" + name
                            + "\" from a TileRegister");
        return nameRegister.get(name).tileImage;
    }

    /**
     * Retrieves the Image of the specified tile using default elevation
     *
     * @param tileType The tile type of the tile
     * @return The sprite Image of the tile
     */
    public Image getTileImage(int tileType) {
        if (!tileTypeRegister.containsKey(tileType))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo of tile type \""
                            + tileType + "\" from a TileRegister");
        return tileTypeRegister.get(tileType).tileImage;
    }

    /**
     * Gets the tile type of a tile from a given name
     *
     * @param name The name of the tile
     * @return The tile type of the tile
     */
    public int getTileType(String name) {
        if (!nameRegister.containsKey(name))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo \"" + name
                            + "\" from a TileRegister");
        return nameRegister.get(name).tileType;
    }

    /**
     * Gets the name of a tile given a tile type
     *
     * @param type The tile type
     * @return The name of the tile
     */
    public String getTileName(int type) {
        if (!tileTypeRegister.containsKey(type))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo \"" + type
                            + "\" from a TileRegister");
        return tileTypeRegister.get(type).tileName;
    }

    public Color getTileColour(String name) {
        if (!nameRegister.containsKey(name))
            throw new RuntimeException(
                    "Attempted to access non-registered TileTypeInfo \"" + name
                            + "\" from a TileRegister");
        return nameRegister.get(name).tileColour;
    }

    private class TileTypeInfo {
        public int tileType;
        public String tileName;
        private Image tileImage;
        private Color tileColour;
        private ArrayList<Image> elevationImages;
        private Map<String, Map<Integer, Image>> directionImages;

        public TileTypeInfo(int tileType, String tileName, Image tileImage,
                            Color tileColour) {
            this.tileType = tileType;
            this.tileName = tileName;
            this.tileImage = tileImage;
            this.tileColour = tileColour;
            this.elevationImages = new ArrayList<>();
            this.directionImages = new HashMap<>();

            /* Generate images for each elevation */
            generateElevation(tileImage);
        }

        public void generateElevation(Image tileImage) {
            int range = Math.abs(World.MAX_ELEVATION - World.MIN_ELEVATION);
            for (int i = 0; i <= range; i++) {
                BufferedImage tile = SwingFXUtils.fromFXImage(tileImage, null);
                tile = ImageHelper.editBrightness(tile,
                        (float) i / (Math.abs(
                                World.MIN_ELEVATION - World.MAX_ELEVATION) *
                                2));
                tile = ImageHelper.editSaturation(tile,
                        (float) i / (Math.abs(
                                World.MIN_ELEVATION - World.MAX_ELEVATION) *
                                2));
//                tile = ImageHelper.editHue(tile, (float) (range - i) / range);
                elevationImages.add(i, SwingFXUtils.toFXImage(tile, null));
            }
        }

        public void addDirection(String direction, Image tileImage) {
            Map<Integer, Image> temp = new HashMap<>();
            int range = Math.abs(World.MAX_ELEVATION - World.MIN_ELEVATION);
            for (int i = 0; i <= range; i++) {
                BufferedImage tile = SwingFXUtils.fromFXImage(tileImage, null);
                tile = ImageHelper.editBrightness(tile,
                        (float) i / (Math.abs(
                                World.MIN_ELEVATION - World.MAX_ELEVATION) *
                                2));
                tile = ImageHelper.editSaturation(tile,
                        (float) i / (Math.abs(
                                World.MIN_ELEVATION - World.MAX_ELEVATION) *
                                2));
//                tile = ImageHelper.editHue(tile, (float) (range - i) / range);
                temp.put(i, SwingFXUtils.toFXImage(tile, null));
            }
            directionImages.put(direction, temp);
        }
    }
}
