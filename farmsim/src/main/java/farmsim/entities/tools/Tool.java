package farmsim.entities.tools;

import java.util.Hashtable;

import farmsim.entities.WorldEntity;
import common.resource.SimpleResource;
/**
 * Describes a tool that can be used by a peon to perform a task.
 * 
 * @author zenyth, yojimmbo, rachelcatchpoole
 *
 */
public class Tool extends WorldEntity {

    // Boolean representing if a tool cannot be used.
    private boolean isBroken;

    // Number representing the initial maximum durability of a tool.
    private double maxDurability;

    // Current durability of a tool. Tool becomes broken when this is 0.
    private double durability;

    // The speed at which a tool performs a task.
    private double speed;

    // Type of the tool
    private ToolType type;

    // The name of the image representing the tool.
    private String imageName;
    
    // The SimpleResource object for storage
    private SimpleResource toolResource;

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.2;

    public Tool(String imageName, double x, double y, double speed,
            double maxDurability, double durability, ToolType type) {
        super(imageName, x, y);
        this.isBroken = false;
        this.speed = speed;
        this.maxDurability = maxDurability;
        this.durability = durability;
        this.imageName = imageName;
        this.type = type;
    }

    /**
     * Gets the speed of the tool.
     * 
     * @return Double representation of the tool's speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Determines whether the tool is broken or not.
     * 
     * @return True if the tool is broken (0 durability) and false otherwise.
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * Determines the durability of the tool.
     * 
     * @return Double representation of the tool's durability.
     */
    public double getDurability() {
        return durability;
    }

    /**
     * Determines the maximum durability of the tool.
     * 
     * @return Double representation of the tool's maximum durability.
     */
    public double getMaxDurability() {
        return maxDurability;
    }

    /**
     * Returns the amount by which durability should be reduced for a tool.
     */
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

    /**
     * Decreases the durability of the tool. Durability cannot fall below 0.
     */
    public void decreaseDurability() {
        double durabilityLoss = this.getDurabilityLoss();
        if (durability > durabilityLoss) {
            durability -= durabilityLoss;
        } else {
            durability = 0;
            isBroken = true;
        }
    }

    /**
     * Restores the durability of the tool back to max. E.g. After repairs.
     */
    public void restoreDurability() {
        durability = maxDurability;
    }

    /**
     * Increase the durability of the tool. Maybe for small repairs.
     * 
     * @param x
     *            The amount that durability will be increased by
     */
    public void increaseDurability(double x) {
        if (durability < maxDurability) {
            durability += x;
        } else {
            durability = maxDurability;
        }
    }

    /**
     * Determines the ToolType of the tool.
     * 
     * @return ToolType of the tool; look in ToolType.java.
     */
    public ToolType getToolType() {
        return type;
    }

    /**
     * Determines the name of the image that the tool should be mapped to.
     * 
     * @return String representation of the name of the image file.
     */
    public String getImageName() {
        return imageName;
    }
        
    /**
     * A helpful method to get a SimpleResource for a tool 
     * @author hankijord
     * @return A SimpleResource object with the attributes of the tool as a hashtable
     */
    public SimpleResource getResource() {
    	Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("isBroken", String.valueOf(isBroken()));
        attributes.put("speed", String.valueOf(getSpeed()));
        attributes.put("durability", String.valueOf(getDurability()));
        attributes.put("imageName", String.valueOf(getImageName()));
        toolResource = new SimpleResource(getToolType().displayName(), attributes, 1);
        return toolResource;
    }

    @Override
    public void tick() {
        // Nothing to do
    }
}
