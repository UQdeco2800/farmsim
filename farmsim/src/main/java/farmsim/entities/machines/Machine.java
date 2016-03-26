package farmsim.entities.machines;

import java.util.Hashtable;

import farmsim.resource.SimpleResource;

import farmsim.entities.WorldEntity;

/**
 * Describes a machine that can be used by a peon to perform a task.
 * 
 * @author zenyth, yojimmbo, rachelcatchpoole
 *
 */

public class Machine extends WorldEntity {

    // Boolean representing whether a machine is usable or not (broken)
    private boolean isBroken;

    // Double representation of the maximum durability of a machine.
    private double maxDurability;

    // Double representation of current durability of a machine. Broken = 0
    private double durability;

    // The speed factor at which a machine performs a task.
    private double speed;

    // Type of machine; look in MachineType.java
    private MachineType type;

    // A string representation of the file name of the image for the machine
    private String imageName;
    
    // The SimpleResource object for storage
    private SimpleResource machineResource;
    
 // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.2;

    public Machine(String imageName, double x, double y, double speed,
            double maxDurability, double durability, MachineType type) {
        super(imageName, x, y);
        this.isBroken = false;
        this.speed = speed;
        this.maxDurability = maxDurability;
        this.durability = durability;
        this.type = type;
        this.imageName = imageName;
    }

    /**
     * Gets the speed of the machine.
     * 
     * @return Double representation of the tool's speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Determines whether the machine is broken or not.
     * 
     * @return True if the machine is broken (0 durability) and false otherwise.
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * Determines the durability of the machine.
     * 
     * @return Double representation of the machine's durability.
     */
    public double getDurability() {
        return durability;
    }

    /**
     * Determines the maximum durability of the machine.
     * 
     * @return Double representation of the machine's maximum durability.
     */
    public double getMaxDurability() {
        return maxDurability;
    }

    // Placeholder: remember to implement decrease/increase/restore durability
    // here
    
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
     * Returns the amount by which durability should be reduced for a tool.
     */
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

    /**
     * Determines the MachineType of the tool.
     * 
     * @return MachineType of the tool; look in MachineType.java.
     */
    public MachineType getMachineType() {
        return type;
    }

    /**
     * Determines the name of the image that the machine should be mapped to.
     * 
     * @return String representation of the name of the image file.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * A helpful method to get a SimpleResource for a machine. 
     * 
     * @return A SimpleResource object with the attributes of the machine as a hashtable.
     */
    public SimpleResource getResource() {
    	Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("isBroken", String.valueOf(isBroken()));
        attributes.put("speed", String.valueOf(getSpeed()));
        attributes.put("durability", String.valueOf(getDurability()));
        attributes.put("imageName", String.valueOf(getImageName()));
        machineResource = new SimpleResource(getMachineType().displayName(), attributes, 1);
        return machineResource;
    }
    
    @Override
    public void tick() {
        // Nothing to do here
    }
}
