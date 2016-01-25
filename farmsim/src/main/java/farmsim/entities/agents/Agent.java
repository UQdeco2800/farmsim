package farmsim.entities.agents;

import com.sun.javafx.tk.Toolkit;
import farmsim.GameRenderer;
import common.resource.SimpleResource;
import common.resource.Tradeable;
import farmsim.Viewport;
import farmsim.entities.WorldEntity;
import farmsim.entities.disease.*;
import farmsim.entities.machines.*;
import farmsim.entities.tools.*;
import farmsim.inventory.Inventory;
import farmsim.inventory.SimpleResourceHandler;
import farmsim.render.Drawable;

import farmsim.tasks.AbstractTask;
import farmsim.tasks.AgentRoleTask;

import farmsim.tasks.TaskManager;
import farmsim.tasks.idle.IdleTask;
import farmsim.tiles.TileRegister;
import farmsim.util.NoPathException;
import farmsim.util.Animation.Animation;
import farmsim.wages.Wage;
import farmsim.world.WorldManager;
import javafx.geometry.Orientation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Rectangle;
import java.util.*;

/**
 * Agents are state-based entities that complete Tasks.
 *
 * @author Leggy
 */
public class Agent extends WorldEntity implements Drawable, Tradeable {

    //Create slf4j logger
    private static final Logger LOGGER =
            LoggerFactory.getLogger(Agent.class);
    // Disease tick at 1/DISEASE_TICKS
    private static final int DISEASE_TICKS = 50;
    private static int totalInfections = 0;
    // Amount to treat each infected crop next tick
    private static int treatmentRound = 0;
    private static int remainingTreatments = 0;
    // For use with the marketplace
    int id;
    // Used for anything that may want to tick slower
    private int tickIndex = 0;
    // Current animation for agent
    private Animation animation;
    // Diseased status
    private Illness disease = null;
    // The agent's rucksack/inventory
    private Inventory rucksack;
    // The tool the agent has equipped
    private Tool tool = null;
    // The machine the agent is using
    private Machine machine = null;
    // Orientation of worker
    private Orientation orientation;
    // Worker details
    private String name;
    private int age;
    private boolean female;
    // RoleType variables
    private RoleType currentRoleType;
    private List<AgentRole> roles = new ArrayList<>();
    // Role management
    private boolean isAreaLocked;
    private Rectangle lockedArea;
    private boolean roleTypeLocked;
    // Happiness of the worker 1-10
    private int happiness;
    private double speed;
    private AbstractTask task;
    private boolean isIdleTask;
    private AgentState state;
    private Map<String, String> marketplaceProperties = new HashMap<>();
    private Random rand;
    // wage of the worker
    private Wage wage;

    public Agent(String name, int x, int y, double speed) {
        super("agent", x, y);
        this.name = name;
        this.speed = speed;
        age = 0;
        // Random gender
        female = new Random().nextBoolean();
        initAgentVariables();
    }

    /**
     * Marketplace constructor for agent.
     *
     * @param x                     The x coordinate that the worker should be placed at.
     * @param y                     The y coordinate that the worker should be placed at.
     * @param speed                 The speed of the worker
     * @param marketplaceProperties Map of properties that should be provided from the marketplace.
     */
    public Agent(int x, int y, double speed,
                 Map<String, String> marketplaceProperties) {
        super("agent", x, y);
        this.speed = speed;
        this.marketplaceProperties = marketplaceProperties;
        name = marketplaceProperties.get("name");
        //TODO not sure how gender is stored yet workaround here
        String gender = marketplaceProperties.get("gender");
        female = gender.equalsIgnoreCase("female");
        setExistingWorkerRoleExperience();
        initAgentVariables();
    }

    /**
     * Method for getting a list of RoleType names. Useful for UI widgets
     * which need to select from the different RoleType
     *
     * @return list of strings with RoleType names
     */
    public static List<String> getRoleTypeNames() {
        ArrayList<String> list = new ArrayList<>();
        for (RoleType roleType : RoleType.values()) {
            list.add(roleType.displayName());
        }
        return list;
    }

    /**
     * Method for getting the RoleType corresponding to the given string.
     *
     * @param name The name of the RoleType.
     * @return RoleType the corresponds to the given name String or null, if
     * the string given does not match any RoleType's displayName's
     * @author hbsteel
     */
    public static RoleType getRoleTypeFromString(String name) {
        for (RoleType roleType : RoleType.values()) {
            if (Objects.equals(roleType.displayName(), name)) {
                return roleType;
            }
        }
        return null;
    }

    public static int getTotalInfections() {
        return totalInfections;
    }

    public static void setTreatmentRound(int amount) {
        treatmentRound = amount;
    }

    public static void setremainingTreatments(int amount) {
        remainingTreatments = amount;
    }

    public int getAge() {
        return age;
    }

    /**
     * Method to setup default values for agent variables
     */
    private void initAgentVariables() {
        this.task = null;
        this.rucksack = new Inventory();
        this.rand = new Random();
        this.roleTypeLocked = false;
        this.isAreaLocked = false;
        this.lockedArea = new Rectangle();
        initRoles();
        initRucksack();
        this.wage = new Wage(100, getHighestLevel());
        this.happiness = wage.getHappiness();
    }

    /**
     * Used to initialise the default SimpleResource items in the rucksack
     * for each specific worker type
     *
     * @author hankijord
     */
    private void initRucksack() {
        // Example of default items, needs to be updated with resources
        Tractor tractor = new Tractor(0, 0, 50);
    	Axe axe = new Axe(0, 0, 10);
        FishingRod rod = new FishingRod(0, 0, 10);
        Hammer hammer = new Hammer(0, 0, 10);
        Hoe hoe = new Hoe(0, 0, 10);
        Pickaxe pickaxe = new Pickaxe(0, 0, 0.1);
        Shovel shovel = new Shovel(0, 0, 10);
        Sickle sickle = new Sickle(0, 0, 10);
        WateringCan can = new WateringCan(0, 0, 10);
        SimpleResource apple = SimpleResourceHandler.getInstance().apple;

        switch (getCurrentRoleType()) {
            case FARMER:
                rucksack.addToRucksack(hoe.getResource());
                rucksack.addToRucksack(shovel.getResource());
                rucksack.addToRucksack(can.getResource());
                rucksack.addToRucksack(sickle.getResource());
                rucksack.addToRucksack(pickaxe.getResource());
                rucksack.addToRucksack(new SimpleResource(apple.getType(),
                        apple.getAttributes(), 10));
                equip(hoe.getToolType());
                break;
            case BUILDER:
                rucksack.addToRucksack(hammer.getResource());
                rucksack.addToRucksack(pickaxe.getResource());
                rucksack.addToRucksack(shovel.getResource());
                rucksack.addToRucksack(sickle.getResource());
                rucksack.addToRucksack(new SimpleResource(apple.getType(),
                        apple.getAttributes(), 10));
                equip(hammer.getToolType());
                break;
            case HUNTER:
                rucksack.addToRucksack(shovel.getResource());
                rucksack.addToRucksack(rod.getResource());
                rucksack.addToRucksack(pickaxe.getResource());
                rucksack.addToRucksack(sickle.getResource());
                rucksack.addToRucksack(axe.getResource());
                rucksack.addToRucksack(new SimpleResource(apple.getType(),
                        apple.getAttributes(), 10));
                equip(axe.getToolType());
                break;
            case MILKER:
                break;
            case BUTCHER:
                break;
            case EGG_HANDLER:
                break;
            case SHEARER:
                break;
        }
    }

    /**
     * Getter method for the female property
     *
     * @return bool value indicating if Agent is a female.
     */
    public boolean isFemale() {
        return female;
    }

    /**
     * Method for returning the String representation of the Agent's gender.
     *
     * @return String value of gender.
     */
    public String getGender() {
        if (female) {
            return "Female";
        } else {
            return "Male";
        }
    }

    /**
     * Simple getter for checking if agent is diseased.
     *
     * @return boolean value indicating if agent has a disease
     */
    public boolean isDiseased() {
        return disease != null;
    }

    /**
     * Infects the agent with a disease if the disease can infect the agent and
     * the agent is not already infected
     *
     * @param newDisease to infect the agent with
     */
    public void infect(Illness newDisease) {
        if (!isDiseased() && newDisease.canInfect(this)) {
            disease = newDisease;
            animation =
                    getRole(currentRoleType).getTravellingAnimation(orientation,
                            this.disease);
            ++totalInfections;
            LOGGER.info(
                    "Peon Infected, " + totalInfections + " total infections");
        }
    }

    /**
     * Treats the agent if it has a disease
     *
     * @param med
     */
    public void applyTreatment(Medicine med) {
        if (isDiseased()) {
            disease.alterHealth(-med.getPotency());
        }
    }

    /**
     * Method for initialising all of the roles for the agent.
     */
    private void initRoles() {
        // Setup spreadsheets and roles
        for (RoleType roleType : RoleType.values()) {
            AgentRoleTravellingSpriteSheets spriteSheets = new
                    AgentRoleTravellingSpriteSheets(getGender(), roleType);
            roles.add(new AgentRole(roleType,
                    spriteSheets));
        }
        currentRoleType = roles.get(rand.nextInt(7)).getRoleType();
    }

    /**
     * Method to read from the marketPlaceProperties variables and set any
     * prior experience for the different AgentRole objects
     */
    public void setExistingWorkerRoleExperience() {
        for (AgentRole role : roles) {
            String expStr = marketplaceProperties.get(role.marketPlaceString());
            if (expStr == null) {
                continue;
            }
            // try parse exp as int
            try {
                int exp = Integer.parseInt(expStr);
                if (exp >= 0 && exp <= 1500) {
                    role.setExperience(exp);
                }
            } catch (NumberFormatException e) {
                LOGGER.info("Invalid Agent roleType experience found, " +
                        "ignoring it.");
            }
        }
    }

    @Override
    public void tick() {
        ++tickIndex;
        if (tickIndex % DISEASE_TICKS == 0) {
            diseaseTick();
        }

        /**
         * If agent is completing an IdleTask try and get a real task.
         */
        if (task == null || isIdleTask) {
            AgentManager.getInstance().checkAgentTasks(this);
        }
        // Update the agent depending on the current state
        state.updateAgent(this);
        if (tickIndex == Integer.MAX_VALUE) {
            tickIndex = 0;
        }
    }

    private void diseaseTick() {
        spreadDisease(false);
        // probability of getting sick depends on season
        switch (WorldManager.getInstance().getWorld().getSeason()) {
            case "SUMMER":
                createDisease(0.01);
                break;
            case "AUTUMN":
                createDisease(0.05);
                break;
            case "WINTER":
                createDisease(0.1);
                break;
            case "SPRING":
                createDisease(0.05);
                break;
        }
        // also has a chance of getting sick relative to pollution level
        createDisease(WorldManager.getInstance().getWorld().getTile(
                getLocation()).getPollution());
        if (isDiseased()) {
            if (remainingTreatments > 0) {
                disease.alterHealth(-treatmentRound);
                --remainingTreatments;
            }
            disease.makeOlder();
            if ((disease.getAge() > disease.getLifetime()) ||
                    disease.getHealth() <= 0) {
                disease = null;
                animation = getRole(currentRoleType)
                        .getTravellingAnimation(orientation,
                                this.disease);
                --totalInfections;
                if (happiness < 10) {
                    ++happiness;
                }
                LOGGER.info("Disease cured, " + totalInfections +
                        " total infections");
            }
        }
    }

    /**
     * Spreads diseases to nearby agents based on distance
     * and the contagiousness of the disease
     * if guarantee is true then it will infect all agents in range
     * guarantee is mainly used for testing
     *
     * @param guarantee
     */
    public void spreadDisease(boolean guarantee) {
        if (!isDiseased()) {
            return;
        }
        Agent temp;
        for (int i = 0; i < AgentManager.getInstance().getAgents().size();
             ++i) {
            try {
                temp = AgentManager.getInstance().getAgents().get(i);
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                break;
            }
            if ((temp.getLocation().distance(this.getLocation()) <
                    disease.getSeverity() / 20) &&
                    ((rand.nextDouble() * 100 < disease.getContagiousness()) ||
                            guarantee)) {
                try {
                    temp.infect(this.disease.getClass().newInstance());
                    if (temp.getHappiness() > 1) {
                        temp.setHappiness(temp.getHappiness() - 1);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    if (e instanceof InstantiationException) {
                        LOGGER.error(
                                "InstantiationException in spread disease");
                    } else {
                        LOGGER.error(
                                "IllegalAccessException in spread disease");
                    }
                }
            }
        }
    }

    /**
     * Infects this Agent with a random disease with
     * probability likelihood
     *
     * @param likelihood
     */
    public void createDisease(double likelihood) {
        ArrayList<Class<? extends Illness>> illnesses = new ArrayList<>(
                Arrays.asList(BlackPlague.class, Influenza.class, Measles.class,
                        Sars.class));
        if (rand.nextDouble() < likelihood) {
            try {
                this.infect(illnesses.get(rand.nextInt(illnesses.size()))
                        .newInstance());
                if (happiness > 1) {
                    --happiness;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                if (e instanceof InstantiationException) {
                    LOGGER.error("InstantiationException in create disease");
                } else {
                    LOGGER.error("IllegalAccessException in create disease");
                }
            }
        }
    }


    public void moveTowardTaskLocation() {
        if (task instanceof IdleTask) {
            AgentManager agentManager = AgentManager.getInstance();
            agentManager.setIdleTask(this);
        } else {
            try {
                getLocation().moveToward(task.getLocation(), speed);
            } catch (IllegalArgumentException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    /**
     * Method to calculate and update the direction the agent is travelling in,
     * in radians.
     *
     * @return The direction that the worker is moving in.
     */
    public double getDirection() {
        double direction = 0;
        if (hasTask()) {
            double deltaX = getLocation().getX() - task.getLocation().getX();
            double deltaY = task.getLocation().getY() - getLocation().getY();
            if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) > 1) {
                direction = Math.atan2(deltaY, deltaX) + Math.PI;
            }
        }
        return direction;
    }

    /**
     * Gets the speed of the agent
     * If the agent is diseased then the speed
     * will be scaled by the severity of the disease
     *
     * @return
     */
    public double getSpeed() {
        if (isDiseased()) {
            return speed * ((float) (disease.getSeverity()) / 100.0);
        }
        return speed;
    }

    /**
     * Change the speed of the agent
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Getter method for name variables.
     *
     * @return The name of the Agent.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether agent has any tasks at hand
     * @return
     */
    public boolean hasTask() {
        return task != null;
    }

    /**
     * Method to check if the agent has an IdleTask.
     *
     * @return boolean value indicating if agent has an idle task
     */
    public boolean hasIdleTask() {
        if (task == null || !(task instanceof IdleTask)) {
            return false;
        }
        return true;
    }

    /**
     * get agent's task
     */
    public AbstractTask getTask() {
        return task;
    }

    /**
     * Sets the agent's task.
     * If the agent is diseased then it won't accept any new tasks
     *
     * @param task
     */
    public void setTask(AbstractTask task) {

        // TODO Error if we already have a task.
        this.task = task;
        if (task != null) {
            isIdleTask = task instanceof IdleTask;
            task.setWorker(this);
            equipToolForTask(task);
            this.state = new TravellingAgentState();

            /**
             * Why we need to set new roleType after task being assigned ?
             * I think agent only change roleType after it reach staff house
             *
             * Animation should be added under handling state ?
             * @author yiwen
             */
            // Calculate the orientation to task
            calculateOrientationToTask();
            // Get the relevant animation and start it
            animation = getRole(currentRoleType).getTravellingAnimation
                    (orientation,
                            disease);

            animation.reset();
            animation.start();
        }
    }

    public Inventory getRucksack() {
        return rucksack;
    }

    /**
     * Get the current equipped tools type.
     *
     * @return ToolType of the current tool, null if one isn't equipped
     */
    public ToolType getToolType() {
        if (tool != null) {
            return tool.getToolType();
        }
        return null;
    }
    
    /**
     * Get the current equipped machine's type.
     *
     * @return MachineType of the current machine, null if one isn't equipped
     */
    public MachineType getMachineType() {
        if (machine != null) {
            return machine.getMachineType();
        }
        return null;
    }

    /**
     * Un-equips any tool the current peon has equipped.
     */
    public void removeTool() {
        if (tool != null && tool.isBroken()) {
            this.removeResource(tool.getToolType().displayName());
            tool = null;
            return;
        }
        this.createNewResource();
        tool = null;
    }
    
    /**
     * Un-equips any machine the current peon has equipped.
     */
    public void removeMachine() {
        machine = null;
    }

    /**
     * Get the currently equipped tool
     *
     * @return Currently equipped tool.
     */
    public Tool getTool() {
        return tool;
    }
    
    /**
     * Get the currently equipped machine.
     *
     * @return Currently equipped machine.
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Equips a tool to the peon. The peon must have the tool as a simple
     * resource in their rucksack or this will fail. Call canEquip() and only
     * call this when it returns true.
     *
     * @param type Type of tool to equip.
     * @return True if it was equipped.
     */
    public boolean equip(ToolType type) {
        if (!this.canEquip(type)) {
            return false;
        }
        Map<String, String> attributes;
        switch (type.displayName()) {
            case "Axe":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new Axe(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Shovel":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new Shovel(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Fishing Rod":
                attributes = this.getToolResource(type).getAttributes();
                createNewResource();
                tool = new FishingRod(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Hammer":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new Hammer(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Hoe":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new Hoe(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Pickaxe":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new Pickaxe(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Sickle":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new Sickle(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
            case "Watering Can":
                createNewResource();
                attributes = this.getToolResource(type).getAttributes();
                tool = new WateringCan(getWorldX(), getWorldY(),
                        Double.parseDouble(attributes.get("durability")));
                return true;
        }
        return false;
    }
    
    public boolean equipMachine(MachineType type) {
        switch (type.displayName()) {
            case "Tractor":
                machine = new Tractor(getWorldX(), getWorldY(), 50);
                return true;
        }
        return false;
    }

    /**
     * Helper method to equip new machine.
     */
    private SimpleResource getMachineResource(MachineType type) {
        Iterator<SimpleResource> it = rucksack.getList().iterator();
        while (it.hasNext()) {
            SimpleResource item = it.next();
            if (item.getType().equals(type.displayName())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Helper method to create a new resource based on old machine stats.
     */
    private void createNewMachineResource() {
        if (machine == null) {
            return;
        }
        SimpleResource machineResource = machine.getResource();
        removeResource(machine.getMachineType().displayName());
        rucksack.addToRucksack(machineResource);
    }
    
    /**
     * Helper method to equip new tool.
     */
    private SimpleResource getToolResource(ToolType type) {
        Iterator<SimpleResource> it = rucksack.getList().iterator();
        while (it.hasNext()) {
            SimpleResource item = it.next();
            if (item.getType().equals(type.displayName())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Helper method to create a new resource based on old tool stats.
     */
    private void createNewResource() {
        if (tool == null) {
            return;
        }
        SimpleResource toolResource = tool.getResource();
        removeResource(tool.getToolType().displayName());
        rucksack.addToRucksack(toolResource);
    }

    /**
     * Helper function to remove a resource.
     *
     * @param name Name of the resource type to remove.
     */
    public void removeResource(String name) {
        Iterator<SimpleResource> it = rucksack.getList().iterator();
        while (it.hasNext()) {
            SimpleResource item = it.next();
            if (item.getType().equals(name)) {
                it.remove();
            }
        }
    }

    /**
     * Check if a type of tool can be equipped by an agent. The tool must be in
     * the rucksack.
     *
     * @param toolType Type of tool to check if can be equipped.
     * @return True if the peon has the tool in their rucksack, false otherwise.
     */
    public boolean canEquip(ToolType toolType) {
        ArrayList<SimpleResource> inventory = rucksack.getList();
        for (SimpleResource item : inventory) {
            if (item.getType().equals(toolType.displayName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a machine can be equipped by an agent. Machine must be in
     * the rucksack.
     *
     * @param machineType Type of machine to check if can be equipped.
     * @return True if the peon has the machine in their rucksack, false otherwise.
     */
    public boolean canEquipMachine(MachineType machineType) {
        ArrayList<SimpleResource> inventory = rucksack.getList();
        for (SimpleResource item : inventory) {
            if (item.getType().equals(machineType.displayName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method used to update direction and orientation for worker.
     */
    public Orientation calculateOrientationToTask() {
        double direction = getDirection();
        switch ((int) ((Math.toDegrees(direction) + 22.5) % 360) / 45) {
            case 0:
                orientation = Orientation.EAST;
                break;
            case 1:
                orientation = Orientation.NORTHEAST;
                break;
            case 2:
                orientation = Orientation.NORTH;
                break;
            case 3:
                orientation = Orientation.NORTHWEST;
                break;
            case 4:
                orientation = Orientation.WEST;
                break;
            case 5:
                orientation = Orientation.SOUTHWEST;
                break;
            case 6:
                orientation = Orientation.SOUTH;
                break;
            case 7:
                orientation = Orientation.SOUTHEAST;
                break;
        }
        return orientation;
    }

    @Override
    public void draw(Viewport v, GraphicsContext g) {
        animation.update();
        animation.renderAnimation(g, (getLocation().getX() - v.getX()) *
                        TileRegister.TILE_SIZE,
                (getLocation().getY() - v.getY()) * TileRegister.TILE_SIZE);
        if (tool != null) {
            g.save();
            g.drawImage(TileRegister.getInstance()
                            .getTileImage(getTool().getImageName()),
                    (getLocation().getX() - v.getX()) * TileRegister.TILE_SIZE,
                    (getLocation().getY() - 0.80 - v.getY()) *
                            TileRegister.TILE_SIZE);
            g.restore();
        }
        drawSelection(v,g);

        if (machine != null) {
            g.save();
            g.drawImage(TileRegister.getInstance()
                            .getTileImage("tractor"),
                    (getLocation().getX() - 0.90 - v.getX()) * TileRegister.TILE_SIZE,
                    (getLocation().getY() - 0.50 - v.getY()) * TileRegister.TILE_SIZE);
            g.restore();
        }
    }

    /**
     * @return The happiness of the worker (1-10)
     */
    public int getHappiness() {
        happiness = wage.getHappiness();
        return happiness;
    }

    /**
     * Should only be set when using a non permanent buff. otherwise use the
     * wage.getHappiness() method to re-set the happiness to the correct value.
     *
     * @param happiness The current happiness of the agent (1-10)
     */
    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    /**
     * Method that returns the implementation of AgentRole for the given roleType
     *
     * @param roleType the RoleType enum
     * @return an implementation of AgentRole corresponding to the RoleType enum
     * provided
     */
    private AgentRole getRole(RoleType roleType) {
        AgentRole returned = null;
        for (AgentRole agentRole : roles) {
            if (agentRole.getRoleType() == roleType) {
                returned = agentRole;
            }
        }
        return returned;
    }

    /**
     * Method to update and return animation for worker.
     *
     * @return SpriteSheetAnimation currently used for worker.
     */
    public Animation getAnimation() {
        animation.update();
        return animation;
    }

    /**
     * Setter method for setting a new aniamtion
     *
     * @param animation The animation for the Agent
     */
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    /**
     * Method to get the level for a given roleType.
     *
     * @param roleType The roleType to get level of.
     * @return The level of the roleType or -1 if an invalid roleType is provided
     */
    public int getLevelForRole(RoleType roleType) {
        AgentRole agentRole = getRole(roleType);
        if (agentRole != null) {
            return agentRole.getLevel();
        }
        return -1;
    }

    /**
     * Method to add experience to the given roleType
     *
     * @param roleType   The roleType to add experience to.
     * @param experience int value of experience to add
     */
    public void addExperienceForRole(RoleType roleType, int experience) {
        AgentRole agentRole = getRole(roleType);
        if (agentRole != null && agentRole.addExp(experience) == 1) {
            // may or may not
            wage.updateLevel(getHighestLevel());
            happiness = wage.getHappiness();
        }
    }


    /**
     * After a worker has completed the legy statue task they will get a temp
     * happiness boost.
     */
    public void IHaveSeenLeggy() {
        wage.setDayOfLeggy();
    }


    /**
     * Method for checking if a agent is eligible to perform a task.
     *
     * @param task The task that the agent may or may not be eligible to
     *             complete
     * @return boolean value indicating if the agent is eligible.
     * @author hbsteel
     */
    public boolean eligibleForTask(AbstractTask task) {
        return isRequiredRole(task) && hasRequiredTool(task) &&
                taskInWorkingArea(task);
    }
    
    /**
     * Checks if the worker has the tool required to complete the task, if it
     * requires one.
     * @param task
     *          Task to check if the worker has the tool for.
     * @return 
     *          True if the worker has the required tool or there is no
     *          requirement, false otherwise.
     */
    public boolean hasRequiredTool(AbstractTask task) {
        ToolType requiredTool = task.getRequiredTool();
        if ((requiredTool != null) && !canEquip(requiredTool)) {
            return false;
        }
        return true;
    }
    
    /**
     * Checks if the worker's role meets the task's requirements.
     * @param task
     *          Task to check if the worker meets the requirements for.
     * @return
     *          True if the worker meets the requirements, false otherwise.
     */
    public boolean isRequiredRole(AbstractTask task) {
        if (task instanceof AgentRoleTask) {
            AgentRoleTask agentRoleTask = (AgentRoleTask) task;
            RoleType roleType = agentRoleTask.relatedRole();
            if (roleType == null) {
                return false;
            }
            if (!roleType.equals(currentRoleType)) {
                return false;
            }
            if ((getLevelForRole(currentRoleType) < agentRoleTask
                    .minimumLevelRequired())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check if the given task is in the agents working area.
     * @param task The task which is being checked
     * @return boolean value indicating if task is in agents working area
     */
    public boolean taskInWorkingArea(AbstractTask task) {
        if(!isAreaLocked) {
            return true;
        }
        farmsim.util.Point location = task.getLocation();
        return lockedArea.contains(location.getX(), location.getY());
    }

    /**
     * Equips appropriate tool for the task, if possible. If there is a required
     * tool for the task, the agent equips it. Agents should only be assigned
     * tasks they have tool for. Also, if is a tool that decreases the task
     * duration, the agent equips that tool.
     *
     * @param task Task the agent is going to complete
     */
    private void equipToolForTask(AbstractTask task) {
        ToolType requiredTool = task.getRequiredTool();
        if (requiredTool != null) {
            if (this.canEquip(requiredTool)) {
                equip(requiredTool);
                return;
            }
        }
        ToolType bonusTool = task.getBonusTool();
        if ((bonusTool != null) && (canEquip(bonusTool))) {
            equip(bonusTool);
        }
    }

    /**
     * Method to determine current roleType of worker.
     *
     * @return AgentRole variable indicating the current roleType of the worker
     */
    public RoleType getCurrentRoleType() {
        return currentRoleType;
    }

    /**
     * Setter method for setting the Agents RoleType
     *
     * @param roleType The type of role that the agent should become.
     */
    public void setCurrentRoleType(RoleType roleType) {
        currentRoleType = roleType;
    }

    /**
     * Method to lock agent to a set area of a rectangle
     * @param workingArea rectangle area to lock worker in
     */
    public void setAreaLock(Rectangle workingArea) {
        this.isAreaLocked = true;
        this.lockedArea.setRect(workingArea);
    }

    /**
     * Finds the workers highest skill level.
     *
     * @return int value of the highest level.
     */
    public int getHighestLevel() {
        int highestLevel = 0;

        for (AgentRole role : roles) {
            if (role.getLevel() > highestLevel) {
                highestLevel = role.getLevel();
            }
        }
        return highestLevel;
    }

    /**
     * Gets the workers current Wage
     * @return workers wage.
     */
    public int getWage() {
        return wage.getWage();
    }

    /**
     * Method to set a worker's wage
     */
    public boolean setWage(int newWage) {
        if (wage.changeWage(newWage)) {
            return true;
        }
        return false;
    }

    /**
     * Method to check if the agent is has been locked into a specific role
     * by the player.
     *
     * @return boolean value indicating if the Agent is locked into a role
     */
    public boolean isRoleTypeLocked() {
        return roleTypeLocked;
    }

    /**
     * Lock the Agent into a specific role, to avoid receiving tasks that
     * require them to change role.
     */
    public void lockRole() {
        roleTypeLocked = true;
    }

    /**
     * Unlock the Agent's role, allowing it to change role automatically if
     * needed.
     */
    public void unlockRole() {
        roleTypeLocked = false;
    }

    @Override
    public Map<String, String> getAttributes() {
        //Save roleType experience to marketplaceProperties
        for (AgentRole role : roles) {
            marketplaceProperties
                    .put(role.marketPlaceString(), Integer.toString(role
                            .getExp()));
        }
        return marketplaceProperties;
    }

    @Override
    public int getQuantity() {
        return 1;
    }

    @Override
    public int getMaxQuantity() {
        return 1;
    }

    /**
     * Setter method for setting agent state
     *
     * @param state AgentState object
     */
    public void setState(AgentState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return name + ", " + currentRoleType.displayName();
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Returns a String containing the filename of an image of the agent.
     * @return
     */
    public String getImageFileName() {
        return "/agents/individual/" + currentRoleType.spriteSheetName()
                + getGender() + ".png";
    }

    public Rectangle getAreaLock() {
        return lockedArea;
    }

    public boolean isAreaLocked() {
        return isAreaLocked;
    }

    public void areaUnlock() {
        isAreaLocked = false;
    }

    enum Orientation {
        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    
    /**
     * enum for the different roles applicable to an agent
     * @author yiwen
     */
    public enum RoleType {
        FARMER("Farmer", "farmer"), HUNTER("Hunter", "hunter"), BUTCHER
                ("Butcher", "animalhandler"),
        SHEARER("Shearer", "animalhandler"), EGG_HANDLER("Egg handler",
                "animalhandler"),
        MILKER("Milker", "animalhandler"),
        BUILDER("Builder", "builder");

        private String displayName;
        private String spriteSheetName;

        RoleType(String displayName, String spriteSheetName) {
            this.displayName = displayName;
            this.spriteSheetName = spriteSheetName;
        }

        /**
         * display role type's name
         * @return name in string
         */
        public String displayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }

        /**
         * display sprite sheet name
         * @return sprite sheet name in stirng
         */
        public String spriteSheetName() {
            return spriteSheetName;
        }
    }
}
