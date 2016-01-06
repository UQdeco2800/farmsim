package farmsim.inventory;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import common.resource.SimpleResource;

/**
 * Singleton Storage instance used for storing managing items
 * 
 * @author gelbana
 *
 */
public class Storage extends Observable {
	Logger LOGGER = LoggerFactory.getLogger(Storage.class);
	private ArrayList<SimpleResource> itemList = new ArrayList<SimpleResource>();
	private HashMap<String, Integer> trackingList = new HashMap<String, Integer>();
	private Resources resources = new Resources();
	private int currentLevel;
	int[] toolLevel = { 10, 20, 30, 40, 50 }; // To be implemented
	ArrayList<Integer> storageLevel; // To be implemented

	public Storage() {
		currentLevel = 20; // To be implemented
	}

	/**
	 * Adds item to the storage. If storage is full, prints error to console and
	 * returns non zero
	 *
	 * @return 0 if successful, else 1
	 */
	public int addItem(SimpleResource item) {
		int stack = 0; // Flag whether to add to existing stack or not
		if (itemList.size() < currentLevel) {
			for (int i = 0; i < itemList.size(); i++) { // Check for existing
				if (itemList.get(i).equalsType(item)) {
					itemList.get(i).addToStack(item); // Add to stack
					stack = 1;
					trackUpdate(); // Update tracking list
					setChanged();
				    notifyObservers();
					return 0;
				}
			}
			if (stack != 1) { // If not in storage, add
				itemList.add(item);
				trackUpdate(); // Update tracking list
				setChanged();
			    notifyObservers();
				return 0;
			}
		} else { // Error if storage is full
			LOGGER.info("Storage is full");
			return 1;
		}
		return 1; 
	}

	/**
	 * Returns quantity of specified item in the storage. If it does not exist
	 * returns 0
	 * 
	 * @return quantity of item in storage
	 */
	public int getQuantity(SimpleResource item) {
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getType().equals(item.getType())) { // Search for item
				return itemList.get(i).getQuantity();
			}
		}
		LOGGER.info("Item is not in storage");
		return 0;

	}

	/**
	 * Returns list
	 * 
	 * @return list of items
	 */
	public ArrayList<SimpleResource> getList() {
		return itemList;

	}

	/**
	 * 
	 * @return Returns quantity of items requested. If too many requested,
	 *         returns max. If item does not exist, return null. NOT WORKING AT
	 *         THE MOMENT
	 */
	public SimpleResource takeItem(SimpleResource item, int quantity) {
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).equals(item)) {
				setChanged();
			    notifyObservers();
				return itemList.get(i).split(quantity);
			}
		}
		LOGGER.info("Item is not in storage");
		return null;
	}

	/**
	 * Gets amount of items in storage
	 * 
	 * @return number of items in storage
	 */
	public int getSize() {
		return itemList.size();
	}

	/**
	 * Get current level of storage
	 * 
	 * @return current level of storage UNIMPLEMENTED
	 */
	public int getLevel() {
		return this.currentLevel;
	}

	/**
	 * Set level of storage
	 */
	public void setLevel(int level) {
		if (level > 0) {
			this.currentLevel = level;
		}
	}

	/**
	 * Removes item from storage with specified quantity. IT GETS DESTROYED.
	 * 
	 * @return 0 if successful, non zero if unsuccessful
	 */
	public int removeItem(SimpleResource item, int quantity) {
		int existingQuantity = 0;

		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getType().equals(item.getType())) { // Storage contains item
				existingQuantity = itemList.get(i).getQuantity();
				if (quantity == 0) {
					return 2;
				} else if (existingQuantity < quantity){
					return 1;
				} else if (quantity == existingQuantity) {
					itemList.remove(i);
					setChanged();
				    notifyObservers();
					return 0;
				} else {
					itemList.set(i, itemList.get(i).split(existingQuantity - quantity));
					setChanged();
				    notifyObservers();
					return 0;
				}
			}
		}
		return 3;
	}
	
	public boolean containsItem(SimpleResource item) {
	    for (int i = 0; i < itemList.size(); i++) {
	        if (itemList.get(i).equalsType(item) && itemList.get(i).getQuantity() >= item.getQuantity()) {
	            return true;
	        }
	    }
	    return false;
	}

	/**
	 * Swap two items in the item list. Used for storage UI purposes.
	 */
	public void swapItem(int position1, int position2) {
		Collections.swap(itemList, position1, position2);
	}

	/**
	 * Add item to tracking list to be display on tab for easy access to 
	 * tracked resource count
	 * 
	 * @return 0 is successful, 1 if already exists or is full
	 */
	public int trackAdd(SimpleResource item) {
		String addType = item.getType();
		int quantity = item.getQuantity();

		if (!trackingList.containsKey(addType) && trackingList.size() < 4) {
			trackingList.put(addType, quantity);
			return 0;
		}
		return 1;

	}

	/**
	 * Removes item from tracking list if it exists
	 * 
	 * @return 0 if successful, 1 otherwise
	 */
	public int trackRemove(SimpleResource item) {
		String addType = item.getType();
		int quantity = item.getQuantity();

		if (trackingList.containsKey(addType)) {
			trackingList.remove(addType, quantity);
			return 0;
		}
		return 1;

	}
	/**
	 * Updates tracking list when item is added or removed
	 */
	private void trackUpdate() {
		for (SimpleResource item : itemList) {
			if (trackingList.containsKey(item.getType()))
				trackingList.replace(item.getType(), item.getQuantity());
		}
	}
}
