package farmsim.contracts;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.ui.Notification;
import farmsim.world.*;
import javafx.application.Platform;


/**
 * Contract Handler handles the current contracts the user has. It adds contracts to the user
 * and checks if a contract is fulfilled.
 * @author wondertroy
 */
public class ContractHandler {
	private Logger LOGGER = LoggerFactory.getLogger(ContractHandler.class);
    private Hashtable<Contract, Integer> currentContracts =
            new Hashtable<Contract, Integer>();

    
	/**
	* adds a contract into current contracts
	*
	* @return Boolean true if the contract was successfully added
	*/
    public Boolean addContract(Contract contract) {
    	World world = WorldManager.getInstance().getWorld();
    	if (currentContracts.size() < 3) {
    		Integer day = world.getTimeManager().getDays();
    		currentContracts.put(contract, day);
    		return true;
    	} else {
    		return false;
    	}
    }
	
	/**
	* @return Hashtable<Contract, Integer> table of current contracts.
	*/
    public Hashtable<Contract, Integer> getContracts() {
        return currentContracts;
    }
	
	/**
	* Checks current contract on if current day is due date for each contract.
	* require currentContracts.entrySet() is not null.
	* ensure every contract is fulfilled with startDay + interval = currentDay.
	*/
    public void checkContracts() {
    	World world = WorldManager.getInstance().getWorld();
        int currentDay = world.getTimeManager().getDays();
        for (Map.Entry<Contract, Integer> currentContract : currentContracts
                .entrySet()) {
            int startDay = currentContract.getValue();
            int interval = currentContract.getKey().getInterval();
            if (currentDay - (startDay + interval) == 0) {
            	notifyUser("Contract Due");
                fulfilContract(currentContract.getKey());
                currentContract.setValue(currentDay);
            } else if ((startDay + interval) - currentDay == 1) {
            	LOGGER.info("Contract Due Tomorrow");
            	notifyUser("A Contract Is Due Tomorrow");
            }
        }
    }

    /**
     * Attempts to fulfill Contract by removing Crop Resources. Fails if not e
     * 
     * @param contract
     */
    private void fulfilContract(Contract contract) {
    	World world = WorldManager.getInstance().getWorld();
        contract.decrementRepeatCount();
        if (contract.getRepeatCount() > 0) {
            // attempt to remove inventory
            if (world.getStorageManager().getCrops().getQuantity(
            			contract.getResourceType()) < contract.getAmount()) {
                // failed to supply crops - penalize and break contract.
            	LOGGER.info("Contract Failed");
            	notifyUser("Contract Failed");
                world.getMoneyHandler().subtractAmount(contract.getPenalty());
                removeContract(contract);
            } else {
            	// maybe give small payment?
            	LOGGER.info("contract delivered");
            	notifyUser("Contract Delivered");
            	world.getMoneyHandler().addAmount(150);
            	world.getStorageManager().getCrops().takeItem(
            				contract.getResourceType(), contract.getAmount());
            }
            
        } else {
            // the contract has been completed

            // add the reward to bank and remove contract.
        	LOGGER.info("contract completed");
        	notifyUser("Contract Completed");
            world.getMoneyHandler().addAmount(contract.getReward());
            currentContracts.remove(contract);
        }
    }
    
    private void notifyUser(String message) {
    	Platform.runLater(() -> {
			Notification.makeNotification("Contracts", message);
        });
    }
    
    /**
     * Remove the contract from current contracts.
     * 
     * @param contract
     */
    public void removeContract(Contract contract) {
    	currentContracts.remove(contract);
    }
    
    /**
     * Get the date of delivery of one contract.
     * 
     * @return the integer of date of delivery
     * 
     * @param contract: the contract that need to get delivery date.
     */
    public int getDeliveryDate(Contract contract) {
    	World world = WorldManager.getInstance().getWorld();
    	int currentDay = world.getTimeManager().getDays();
    	int startDay = currentContracts.get(contract);
    	int interval = contract.getInterval();
    	return (startDay + interval) - currentDay;
    }

    /**
     * Return the contract at an index from the hashtable.
     *
     * @param index of the contract
     *
     * @return contract at index
     */
    public Contract getContract(int index) {
        int i = 0;
        HashSet<Contract> contracts =
        		new HashSet<Contract>(currentContracts.keySet());
        for (Contract currentContract : contracts) {
            if (i == index) {
                return currentContract;
            }
            i++;
        }
        return null;
    }

}
