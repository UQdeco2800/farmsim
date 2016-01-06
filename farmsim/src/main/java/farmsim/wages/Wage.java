package farmsim.wages;

import farmsim.world.WorldManager;

/**
 * @author BlakeEddie
 *
 *         <p> The Wage for each worker which will deal with the amount of
 *         money taken out of the players money each time it is nesasary.
 *         At this time3/09/2015 how the peions are hired are being worked
 *         out with habbitats and marketplace, this class may be able to change
 *         the Wage for the habbitat or just take out the amount of money
 *         and not actually send it to the habitat.
 *
 *         The money actually being taken out should be dealt with in the time
 *         code when it becomes a week or X time in a peion pay function.</p>
 *
 *         prices for peions
 *         http://deco2800.uqcloud.net/jenkins/job/Marketplace/javadoc/common/resource/SimpleOrder.html
 *         http://deco2800.uqcloud.net/jenkins/job/Marketplace/javadoc/common/client/GenericClient.html#getBuyOrders-java.lang.String-
 */
public class Wage {

    /**
     * The current Wage the worker will be payed.
     */
    protected int currentWage;

    /**
     * What the worker is expecting to be payed this will relate to a happiness
     * level of 8.
     */
    protected int expectedWage;
    protected int startingExpected;

    /**
     * Last time a worker has Seen leggy
     */
    protected int lastDay;

    /**
     * Determines whether the worker was created in game or through
     * the marketplace. true market false game generated.
     */
    protected boolean marker;

    /**
     * starting level for marketplace constructor
     */
    protected int level;

    /**
     * Initialises the Wage of a worker who has a skill level. if someone tries
     * to set a worker to a level outside the bounds it is automatically set to
     * 5 (most expensive) as a deterrent. Used for generating workers without
     * the marketplace.
     *
     * @param startingWage this is set to how much the worker will take out of
     * funds.
     * @param skillLevel skill level used to calculate expected Wage. between
     * 1-5.
     */
    public Wage(int startingWage, int skillLevel) {
        this.marker = false;
        this.currentWage = startingWage;
        this.lastDay = -1;
        // if skill level is invalid.
        if (skillLevel > 5 || skillLevel < 1) {
            setExpectedWage(5);
            return;
        }
        setExpectedWage(skillLevel);
    }

    /**
     * Initialises the Wage of a worker who has a skill level. if someone tries
     * to set a worker to a level outside the bounds it is automatically set to
     * 5 (most expensive) as a deterrent.
     *
     * @param fullAmount the full amount the marketplace has asked for.
     * @param hireTime how long they will be hired for.
     * @param skillLevel skill level used to calculate expected Wage. between
     * 1-5.
     */
    public Wage(String fullAmount, String hireTime, int skillLevel) {
        this.marker = true;
        this.level = skillLevel;
        this.currentWage = findAverageWage(fullAmount, hireTime, skillLevel);
        this.lastDay = -1;

    }

    /**
     * The habitat expects the amount to be payed thought the hire time.
     *
     * @param fullAmount the string representation of the lump sum.
     * @param hireTime the string representation of how long the worker is
     * staying for
     * @param skillLevel skill level used to calculate expected Wage. between
     * 1-5.
     *
     * @return if the two strings are valid return the weekly wage else
     * return 100;
     */
    private int findAverageWage(String fullAmount, String hireTime, int
            skillLevel) {
        int averageWage;
        int amount;
        int time;
        try {
            amount = Integer.parseInt(fullAmount);
            time = Integer.parseInt(hireTime);
            averageWage = amount / time;
            startingExpected = averageWage;
            setExpectedWage(averageWage, skillLevel);
        } catch (Exception e) {
            averageWage = 100;
            // if skill level is invalid.
            if (skillLevel > 5 || skillLevel < 1) {
                setExpectedWage(5);
            }
        }

        return averageWage;
    }


    /**
     * Wage getter method
     *
     * @return currentWage which is how much the worker is payed per X time.
     */
    public int getWage() {
        return currentWage;
    }

    /**
     * Calculated through the expected Wage and the actual Wage. this may be
     * changed if a decision is made that the workers pay is predetermined and
     * we shouldn't have there pay changeable even if only in our game.
     * Also checks when the last day they saw leggy was.
     *
     * @return the happiness of worker this is between 1 and 10.
     */
    public int getHappiness() {

        int difference = currentWage - expectedWage;
        // based off 100 dollars less being 1 and 100 dollars more being max.
        Double happiness =
                0.0001 * Math.pow(difference, 2) + 0.05 * difference + 5;

        if (lastDay != -1 && (WorldManager.getInstance().getWorld().
                getTimeManager().getDays() - lastDay) < 7) {
            happiness += 2;
        }

        if (happiness > 10) {
            happiness = 10.0;
        }

        if (happiness < 1) {
            happiness = 1.0;
        }

        // rounds down.
        return happiness.intValue();
    }

    /**
     * Change Wage within a bounds if this fails 1 is returned as a sign of
     * invalid change. setting a min but not a max, if the player is not
     *
     * @param newWage what the Wage is wanting to be set to.
     *
     * @return True if successful False otherwise.
     */
    public boolean changeWage(int newWage) {
        // this also deals with negative inputs.
        if (newWage < 50) {
            return false;
        }

        currentWage = newWage;
        return true;
    }

    /**
     * assuming workers can go down in level if they have changed professions.
     *
     * @param skillLevel the updated level.
     */
    public void updateLevel(int skillLevel) {
        // if skill level is invalid.
        if (skillLevel > 5 || skillLevel < 1) {
            return;
        }

        if (marker) {
            setExpectedWage(startingExpected, skillLevel);
        } else {
            setExpectedWage(skillLevel);
        }
    }


    /**
     * set expected Wage taking into consideration the skill level of the worker
     * should be updated anytime skill level is changed. big pay rise at first
     * level jump then slows down. maxing out at 400. function based off $100
     * being the expected value if skill is level 1.
     *
     * @param skillLevel how skilled the worker is.
     */
    private void setExpectedWage(int skillLevel) {
        // log base e function modeling Wage growth.
        Double expected = 100 + 187 * Math.log(skillLevel);
        if (expected > 400) {
            expectedWage = 400;
        }

        // rounds down.
        expectedWage = expected.intValue();
    }

    /**
     * set expected Wage taking into consideration the skill level of the
     * worker should be updated anytime skill level is changed. big pay rise
     * at first level jump then slows down. maxing out at 400.
     * function based off the marketplaces expected wage per week.
     *
     * being the expected value if skill is level 1.
     *
     * @param amount the marketplaces expected wage.
     * @param skillLevel how skilled the worker is.
     */
    private void setExpectedWage(int amount, int skillLevel) {
        // log base e function modeling Wage growth.
        Double expected = amount + 187 * Math.log(skillLevel - level + 1);
        if (expected > amount + 400) {
            expectedWage = amount + 400;
        }

        // rounds down.
        expectedWage = expected.intValue();
    }

    /**
     * sets the day worker has seen leggy statue. May he forever bring
     * happiness to deco farm workers.
     */
    public void setDayOfLeggy() {
        //deals with the world not being created.
        if (WorldManager.getInstance().getWorld() != null) {
            lastDay = WorldManager.getInstance().getWorld().
                    getTimeManager().getDays();
        }
    }
}
