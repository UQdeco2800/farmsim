package farmsim.inventory;

import java.util.*;

import farmsim.entities.tools.ToolType;
import common.resource.SimpleResource;

/**
 * An Inventory/Rucksack class for an agent's personal storage of items
 * 
 * @author hankijord
 *
 */
public class Inventory extends Observable implements Iterable<SimpleResource> {
	// Static size of the rucksack
    private static final int INVENTORY_SIZE = 8;
    
    private SimpleResource equippedItem;
    
    // Rucksack array to store SimpleResources
    private ArrayList<SimpleResource> inventoryArray;
    
    /**
     * Initialises a new inventory
     */
    public Inventory() {
        inventoryArray = new ArrayList<SimpleResource>();
    }

    /**
     * Add a SimpleResource to the rucksack.
     * 
     * @param SimpleResource resource
     * @return error code (0 = ok, 1 = rucksack is full)
     */
    public int addToRucksack(SimpleResource resource) {
        int stack = 0; // Flag whether to add to existing stack or not
		if (inventoryArray.size() < INVENTORY_SIZE) {
			for (int i = 0; i < inventoryArray.size(); i++) { // Check for existing
				if (inventoryArray.get(i).equalsType(resource)) {
					inventoryArray.get(i).addToStack(resource); // Add to stack
					stack = 1;
					handleObservers();
					return 0;
				}
			}
			if (stack != 1) { // If not in rucksack, add
				inventoryArray.add(resource);
				handleObservers();
				return 0;
			}
		} else {
			// Rucksack is full
			return 1;
		}
		return 1;
    }

    /**
     * Remove a SimpleResource from the rucksack, along with specifiying a
     * quantity to remove
     * 
     * @param SimpleResource resource
     * @param int quantity
     * @return error code (0 = ok, 1 = not enough items, 2 = Cannot remove 0 items,
     * 						 3 = Resource does not exist in the rucksack)
     */
    public int removeFromRucksack(SimpleResource resource, int quantity) {
    	int existingQuantity = 0;

		for (int i = 0; i < inventoryArray.size(); i++) {
			if (inventoryArray.get(i).getType().equals(resource.getType())) { // Rucksack contains item
				existingQuantity = inventoryArray.get(i).getQuantity();
				if (existingQuantity < quantity) {
					// Not enough items
					return 1;
				} else if (quantity == 0) {
					// Cannot remove 0 items
					return 2;
				} else if (quantity == existingQuantity) {
					//Removed resource
					inventoryArray.remove(i);
					handleObservers();
					return 0;
				} else {
					//Removed part of resource
					inventoryArray.set(i, inventoryArray.get(i).split(existingQuantity - quantity));
					handleObservers();
					return 0;
				}
			}
		}
		return 3; //Resource does not exist in Rucksack
    }
    
    /**
     * Sets the equipped tool to be the specified tool type
     * @param ToolType
     */
    public void setEquippedTool(ToolType tool){
    	for (SimpleResource resource: inventoryArray){
    		if(tool.displayName() == resource.getType()){
    			equippedItem = resource;
    			handleObservers();
    		}
    	}
    }
    
    /**
     * Gets the equipped tool type from Rucksack
     * @return ToolType
     */
    public ToolType getEquippedTool(){
    	if (equippedItem == null){return null;}
    	SimpleResource resource = getResource(equippedItem.getType());
    	for (ToolType tool: ToolType.values()){
    		if(tool.displayName().equals(resource.getType())){
    			return tool;
    		}
    	}
    	return null;
    }
    
    /**
     * Get a SimpleResource from its type string
     * Not case-sensitive
     * @return SimpleResource
     */
    public SimpleResource getResource(String type) {
    	for (SimpleResource resource: inventoryArray){
    		if (resource.getType().toLowerCase().equals(type.toLowerCase())){
    			return resource;
    		}
    	}
        return null;
    }
    
    /**
     * Check if a tool SimpleResource is in the rucksack
     * @return boolean (returns true if item is in rucksack)
     */
    public boolean containsTool(ToolType type) {
    	for (SimpleResource resource: inventoryArray){
    		if (resource.getType().equals(type.displayName())){
    			return true;
    		}
    	}
        return false;
    }
    
    /**
     * Check if a SimpleResource is in the rucksack
     * @return boolean (returns true if item is in rucksack)
     */
    public boolean containsItem(SimpleResource item) {
    	for (SimpleResource resource: inventoryArray){
    		if (resource.getType().equals(item.getType())){
    			return true;
    		}
    	}
        return false;
    }
    
    /**
     * Check if the rucksack is full
     * @return boolean (returns true if rucksack is full)
     */
    public boolean isFull() {
        return (inventoryArray.size() == INVENTORY_SIZE);
    }
    
    /**
     * Get the size of the rucksack.
     * @return int size
     */
    public int getSize() {
        int i = 0;
        i = inventoryArray.size();
        return i;
    }
    
    /**
     * Get the rucksack as an ArrayList.
     * @return ArrayList rucksack
     */
    public ArrayList<SimpleResource> getList() {
		return inventoryArray;
	}
    
    /**
     * Notifies the rucksack observer that the rucksack contents has changed and must be
     * updated
     */
    public void handleObservers() {
        setChanged();
        notifyObservers();
    }
    
    /**
     * Iterate through the rucksack
     */
    @Override
    public Iterator<SimpleResource> iterator() {
        return inventoryArray.iterator();
    }

    /**
     * Provides a string of items in the rucksack
     */
    @Override
    public String toString() {
        String str = "";
        for (SimpleResource resource : inventoryArray) {
            str += resource.toString();
            str += System.getProperty("line.separator");
        }
        return str;
    }
}
