package farmsim.inventory;

import java.util.*;
import farmsim.resource.SimpleResource;
/**
 * Storage manager for handling storage class functions.
 *
 *
 **/
public class StorageManager {
    Storage crops;
    Storage tools;
    Resources resources = new Resources();
    Storage seeds;
    SimpleResourceHandler resourceHandler = SimpleResourceHandler.getInstance();
    Storage general;
    /**
     * creates instance of StorageManager.
     *
     **/
   public StorageManager() {
	   crops = new Storage();
	   tools = new Storage();
	   seeds = new Storage();

	   crops.addItem(resourceHandler.apple);
	   crops.addItem(resourceHandler.mango);
	   tools.addItem(new SimpleResource("Axe",new Hashtable<String,String>(), 1));
	   general = new Storage();
   }

    /**
     * Create SimpleResource hashstring from string.
     * @return new SimpleResource
     */
    SimpleResource getItem(){ 
        return new SimpleResource(" ",new Hashtable<String,String>()); 
    }

    /**
     * Add SimpleResource to storage.
     * @param item
     * @return isSeed if item is of type Seed.
     */
    public int addItem(SimpleResource item) {
    	int isSeed = 0;
        if (resources.getCrops().containsKey(item.getType())) {
        	crops.addItem(item);
        } else if (resources.getTools().containsKey(item.getType())) {
        	tools.addItem(item);
        } else if (resources.getSeeds().contains(item.getType())) {
        	isSeed = seeds.addItem(item); 
        }
        return isSeed;
    }

    /**
     * Remove item from storage.
     * @param item
     * @return
     */
    public int removeItem(SimpleResource item) {
        return removeItem(item, item.getQuantity());
    }

    /**
     * Remove quantity of item from storage.
     * @param item
     * @param quantity
     * @return value of error for error checking.
     */

    public int removeItem(SimpleResource item, int quantity){
    	int error = 0;
    	if (resources.getCrops().containsKey(item.getType())) {
        	error = crops.removeItem(item, quantity);
        } else if (resources.getTools().containsKey(item.getType())) { 
        	error = tools.removeItem(item, quantity);
	    } else if (resources.getSeeds().contains(item.getType())) {
	    	error = seeds.removeItem(item, quantity);
	    } else if (general.containsItem(item)) {
	        error = general.removeItem(item, quantity);
	    }
    	return error;
    }

    /**
     * Check if item is in storage.
     * @param item
     * @return 0 if item is in storage 1 if item is not in storage.
     */
    
    public boolean containsItem(SimpleResource item) {
        return crops.containsItem(item) || 
                tools.containsItem(item) || 
                seeds.containsItem(item) || 
                general.containsItem(item);
    }

    /**
     * Return crops storage.
     * @return crops
     */
    public Storage getCrops() {
    	return crops;
    }

    /**
     * Return tools storage.
     * @return tools
     */
    public Storage getTools() {
    	return tools;
    }

    /**
     * Return seeds storage.
     * @return seeds
     */
    public Storage getSeeds() {
    	return seeds;
    }

    /**
     * Return general storage.
     * @return general
     */
    public Storage getGeneral() {
        return general;
    }

    /**
     * Take item from storage.
     * @param item
     * @param quantity
     */
    void takeItem(SimpleResource item, int quantity){
    	if (resources.getCrops().containsKey(item.getType())) {
        	crops.takeItem(item, quantity);
        } else if (resources.getTools().containsKey(item.getType())) {
        	tools.takeItem(item, quantity);
         } else if (resources.getSeeds().contains(item.getType())) {
	    	seeds.takeItem(item, quantity);
	    }
    }

    /**
     * Return quantity of item in storage
     * @param item
     * @param g
     * @return quantity if item is in storage, 1 if ite does not exist.
     */

    int getQuantity(SimpleResource item, Storage g){
        if(g.getQuantity(item) != 0) {
            int quantity_return = g.getQuantity(item);
            return quantity_return;
        }
        //System.err.println("Item not in storage"); @Todo -> replace with logger!!!!
        return 1;
    }


}
