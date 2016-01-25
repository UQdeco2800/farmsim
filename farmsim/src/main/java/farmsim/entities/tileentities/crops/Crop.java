package farmsim.entities.tileentities.crops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.entities.disease.Blight;
import farmsim.entities.disease.Pesticide;
import farmsim.entities.disease.Pestilence;
import farmsim.entities.tileentities.TileEntity;
import farmsim.tiles.Tile;

import farmsim.world.WorldManager;
import javafx.application.Platform;
import farmsim.tiles.TileProperty;
import farmsim.ui.Notification;

/**
 * An abstract class containing the functionality for crops.
 *
 */
public abstract class Crop extends TileEntity {

    private long startTime;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(Crop.class);
    protected int stage;
    protected int harvestQuantity;
    protected String posEnviros;
    protected String negEnviros;
    protected int currentLevel;
    protected String[] stages;
    protected String[] deadStages;
    // Growth times are measured from the initial start time, not relatively.
    protected int[] stageTimes;
    protected boolean isDead;
    protected int tickIndex = 0;
    protected Pestilence pest = null;
    protected static final int DISEASE_TICKS = 50;
    protected static int totalInfections = 0;
    // Amount to treat each infected crop next tick
    protected static int treatmentRound = 0;
    protected static int remainingTreatments = 0;
    private Random rand;
    protected boolean waterNotified = false;

    protected long advance = 0;

    public Crop(String plantName, Tile parent) {
        super(plantName, parent);
        this.startTime = System.currentTimeMillis();
        this.rand = new Random();
        stage = 0;
        stages = new String[4];
        deadStages = new String[3];
        stageTimes = new int[5];
        isDead = false;
        // retrieve the attributes for the required plant
        Map<String, Object> properties =
                DatabaseHandler.getInstance().getPlantData(plantName);
        currentLevel = (int) properties.get("Clevel");
        stageTimes[0] = (int) properties.get("Gtime1");
        stageTimes[1] = (int) properties.get("Gtime2");
        stageTimes[2] = (int) properties.get("Gtime3");
        stageTimes[3] = (int) properties.get("Gtime4");
        stageTimes[4] = (int) properties.get("Gtime5");
        harvestQuantity = (int) properties.get("Hquantity");
        stages[0] = (String) properties.get("StNames1");
        stages[1] = (String)properties.get("StNames2");
        stages[2] = (String)properties.get("StNames3");
        stages[3] = (String)properties.get("StNames4");
        deadStages[0] = (String)properties.get("DeadStage1");
        deadStages[1] = (String)properties.get("DeadStage2");
        deadStages[2] = (String)properties.get("DeadStage3");
        posEnviros = (String) properties.get("Penvironment");
        negEnviros = (String) properties.get("Nenvironment");
        handleBiome(plantName);
    }

    /**
     * Change the maturity of the plant if enough time has changed and kill the
     * plant if it does not have water.
     */
    @Override
    public void tick() {
        tickDiseaseProperties();
        tickWaterProperties();
        /*
         * If there is a stage afterward we can change to, keep going, otherwise
         * don't bother.
         */
        if (stage < stages.length - 1 && !isDead) {
            if (System.currentTimeMillis() + advance
                    - startTime > stageTimes[stage + 1]) {
                stage++;
                if(stage == stages.length - 1) {
                    Platform.runLater(() ->
                                    Notification.makeNotification("Crops", "/crops/" +
                                            type + "3.png", type + " mature")
                    );
                }
            }
        } else if (stage == stages.length - 1 && (System.currentTimeMillis()
                - startTime > stageTimes[stage + 1])) {
            isDead = true;
            stage -= 1;
        }
        if (tickIndex == Integer.MAX_VALUE) {
            tickIndex = 0;
        }
    }

    /**
     * Performs all of the water management actions required on a tick.
     */
    private void tickWaterProperties() {
        if (getParent().getWaterLevel() > (float)0.09 && getParent().getWaterLevel() < (float)0.095 && !waterNotified) {
            Platform.runLater(() ->
                            Notification.makeNotification("Crops", "Watering required")
            );
            waterNotified = true;
        }
        if (getParent().getWaterLevel() > (float)0.095) {
            waterNotified = false;
        }
        // kill the plant if the water level is 0
        if (Float.floatToRawIntBits(getParent().getWaterLevel()) == 0 && !isDead) {
            isDead = true;
            stage -= 1;
        }
    }

    /**
     * Performs all the disease management actions required on a tick.
     */
    private void tickDiseaseProperties() {
        ++tickIndex;
        if (tickIndex % DISEASE_TICKS == 0) {
            spreadDisease();
            createDisease(0.1);
            if (isDiseased()) {
                if (remainingTreatments > 0) {
                    pest.alterHealth(-treatmentRound);
                    --remainingTreatments;
                }
                pest.makeOlder();
                if ((pest.getAge() > pest.getLifetime()) ||
                        pest.getHealth() <= 0) {
                    pest = null;
                    --totalInfections;
                    LOGGER.info("Crop treated: " + totalInfections + " total infections");
                }
            }
        }
    }

    /**
     * Advances the growth of the crop artificially.
     * @param time to advance the crop.
     */
    public void advanceGrowth(long time) {
        advance += time;
    }

    /**
     * Get the current stage of the crop.
     *
     * @return
     *      a string representing the current growth stage.
     */
    @Override
    public String getTileType() {
        if(isDead) {
            if(stage < 0) {
                return stages[0];
            }
            return deadStages[stage];
        }
        return stages[stage];
    }

    /**
     * Get the name of the plant (the plant type).
     *
     * @return
     *      A string representing the plant name.
     */
    public String getName() {
        return super.getTileType();
    }

    /**
     * gets the number of crops when harvested.
     *
     * @return
     *      An int of the quantity of crops harvested.
     */
    public int getQuantity() {
        if (isDiseased()) {
            return (int) (harvestQuantity * ((float) (pest.getSeverity()) / 100.0));
        }
        return harvestQuantity;
    }

    /**
     * Returns whether the plant is alive or dead.
     *
     * @return
     *      True if the plant is dead, false if it is alive.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Simple getter for checking if crop is diseased.
     *
     * @return boolean value indicating if crop has a disease
     */
    public boolean isDiseased() {
        return pest != null;
    }

    /**
     * Infects the crop with a disease if the disease can infect the crop and
     * the crop is not already infected
     *
     * @param newPest to infect the crop with
     */
    public void infect(Pestilence newPest) {

    }

    /**
     * Updates crop properties based on the Biome
     * the crop was planted in.
     *
     * @param plantName
     *     Name of the crop (Plant Type)
     */
    public void handleBiome(String plantName) {
        this.getParent().getProperty(TileProperty.BIOME);
        String biome = this.getParent().getProperty(TileProperty.BIOME).toString();
        if (biome.equals(posEnviros)) {
            for (int i = 0; i < stageTimes.length; i++) {
                stageTimes[i] = stageTimes[i]/2;
            }
        } else if (biome.equals(negEnviros)) {
            for (int i = 0; i < stageTimes.length; i++) {
                stageTimes[i] = stageTimes[i] * 2;
            }
        }

        if (("FOREST").equals(biome)) {
            if (isOrchard(plantName)) {
                harvestQuantity = (int) Math.round(harvestQuantity * 1.3);
            } else {
                harvestQuantity = (int) Math.round(harvestQuantity * 0.7);
            }
        }
    }

    /**
     * Treats the agent if it has a disease
     * @param cure
     */
    public void applyTreatment(Pesticide cure) {
        if (isDiseased()) {
            pest.alterHealth(-cure.getPotency());
        }
    }

    /**
     * Infects this crop with a disease.
     * @param likelihood
     *      the chance that the crop will become diseased.
     */
    public void createDisease(double likelihood) {
        ArrayList<Class<? extends Pestilence>> pests = new ArrayList<>(
                Arrays.asList(Blight.class));
        if (rand.nextDouble() < likelihood) {
            try {
                this.infect(pests.get(rand.nextInt(pests.size())).newInstance());
            } catch (InstantiationException e) {
                LOGGER.error(e.getMessage());
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * Spreads the disease of an infected crop to adjacent crops.
     */
    public void spreadDisease() {
        if (!isDiseased()) {
            return;
        }
        TileEntity temp;
        double x, y, xPos, yPos;
        xPos = this.getWorldX();
        yPos = this.getWorldY();
        for (x = xPos - (this.pest.getContagiousness() / 10);
             x <= xPos + (this.pest.getContagiousness() / 10); ++x) {
            for (y = yPos - (this.pest.getContagiousness() / 10);
                 y <= yPos + (this.pest.getContagiousness() / 10); ++y) {
                if ((x > 0) && (y > 0) &&
                        (x < WorldManager.getInstance().getWorld().getWidth()) &&
                        (y < WorldManager.getInstance().getWorld().getHeight())) {
                    temp = WorldManager.getInstance().getWorld().getTile(
                            (int) x, (int) y).getTileEntity();
                    if ((temp != null) && (temp instanceof Crop)) {
                        try {
                            ((Crop) temp).infect(this.pest.getClass().newInstance());
                        } catch (InstantiationException e) {
                            LOGGER.error(e.getMessage());
                        } catch (IllegalAccessException e) {
                            LOGGER.error(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    /**
     * Gives the total number of crops that are infected.
     * @return
     *      the number of infected crops.
     */
    public static int getTotalInfections() {
        return totalInfections;
    }

    /**
     * Specifies the amount of treatment that should be given to a crop each 
     * tick.
     * @param amount
     *      the treatment that should be given per tick.
     */
    public static void setTreatmentRound(int amount) {
        treatmentRound = amount;
    }

    /**
     * The number of treatments that are left.
     * @param amount
     *      the number of remaining treatments.
     */
    public static void setremainingTreatments(int amount) {
        remainingTreatments = amount;
    }

    /**
     * Returns a Boolean based on the type
     * of the crop (Orchard or not)
     *
     * @param plantName
     *     Name of the crop (Plant Type)
     *
     * @return
     *     true if crop is an Orchard
     *     false if crop is not an Orchard.
     */
    public boolean isOrchard(String plantName) {

        if (plantName.equals("Apple") || plantName.equals("Banana") ||
                plantName.equals("Mango")){
            return true;
        } else if (plantName.equals("Lemon")|| plantName.equals("Pear")){
            return true;
        }
        return false;
    }
}
