package farmsim.contracts;

import common.resource.SimpleResource;

/**
* Implementation of a contract. Contracts are an optional
* task in the game to gain money by obtaining a certain amount of a 
* simple resource periodically.
* If a user fails to fulfil the contract they are punished
* by losing money.
*/
public class Contract {

    private String contractGiver;
    private SimpleResource resourceType;
    private int amount;
    private int repeatCount;
    private int interval;
    private int reward;
    private int penalty;
    private String description;
    private int expiryDate;
	
	/**
	* Sets up a contract with is made of a contract giver, a resource type,
	* an amount of resource, a count of the amount of repeats in the contract,
	* an interval of time till contract is due, a reward for completion,
	* a penalty for failing a contract, a discription and an expiry date.
	*/
    public Contract(String contractGiver, SimpleResource resourceType, int amount,
            int repeatCount, int interval, int reward, int penalty,
            String description, int expiryDate) {
        this.contractGiver = contractGiver;
        this.resourceType = resourceType;
        this.amount = amount;
        this.repeatCount = repeatCount;
        this.interval = interval;
        this.reward = reward;
        this.penalty = penalty;
        this.description = description;
        this.expiryDate = expiryDate;
    }

	/**
	* @return String name of contract giver.
	*/
    public String getContractGiver() {
        // Returns the contract giver.
        return contractGiver;
    }

	/**
	* @return SimpleResource type to be delivered in contract
	*/
    public SimpleResource getResourceType() {
        // Returns the resource type
        return resourceType;
    }

	/**
	* @return int amount of resources to provide
	*/
    public int getAmount() {
        // Returns the amount of resources to provide
        return amount;
    }

	/**
	* @return int amount of times resource amount to be raised
	*/
    public int getRepeatCount() {
        // Returns the amount of times the payment is repeated
        return repeatCount;
    }

	/**
	* decrement the amount of repeated after each time interval
	* is reached.
	*/
    public void decrementRepeatCount() {
        repeatCount--;
    }

	/**
	* @return int the interval of repeats
	*/
    public int getInterval() {
        // Returns the interval of repeats
        return interval;
    }

	/**
	* @return int the reward to be paid to the user
	* if contract is fulfilled.
	*/
    public int getReward() {
        // Returns the reward amount
        return reward;
    }
	
	/**
	* @return int penalty to user if failure of contract
	*/
    public int getPenalty() {
        // Returns the penalty amount
        return penalty;
    }

	/**
	* @return String description of contract
	*/
    public String getDescription() {
        // Returns the description of the contract
        return description;
    }

	/**
	* @return int expiry of contract
	*/
    public int getExpiryDate() {
        // Returns the expiry date
        return expiryDate;
    }
}
