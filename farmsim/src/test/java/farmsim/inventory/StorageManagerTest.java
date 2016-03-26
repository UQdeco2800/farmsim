package farmsim.inventory;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import org.junit.Test;

import farmsim.resource.SimpleResource;

public class StorageManagerTest {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    Storage testStorage = new Storage();
    SimpleResourceHandler handler = SimpleResourceHandler.getInstance();
    @Test
    public void addTest() {
    	StorageManager storageMan = new StorageManager();
    	Storage crops = new Storage();
    	Storage tools = new Storage();
        //add existing item
    	SimpleResource apple = handler.apple;
    	SimpleResource mango = handler.mango;
    	SimpleResource item2 = new SimpleResource("Axe", hashtable, 1);
        crops.addItem(apple);
        crops.addItem(mango);
        tools.addItem(item2);
        assertEquals(storageMan.getCrops().getList().get(0), crops.getList().get(0));
        assertEquals(storageMan.getTools().getList().get(0), tools.getList().get(0)); 
    }

    @Test
    public void removeTest() {
    	StorageManager storageMan = new StorageManager();
    	Storage crops = new Storage();
    	Storage tools = new Storage();
        //add existing item
    	SimpleResource item = new SimpleResource("Apple", hashtable, 10);
    	SimpleResource item2 = new SimpleResource("Axe", hashtable, 10);
        storageMan.addItem(item);
        storageMan.addItem(item2);
        crops.addItem(item);
        tools.addItem(item2);
    	
        storageMan.removeItem(item, 5);
       
        storageMan.removeItem(item2, 5);
        crops.removeItem(item, 5);
        tools.removeItem(item2, 5);
     //   assertEquals(storageMan.getCrops().getList().size(), crops.getList().size());
     //   assertEquals(storageMan.getTools().getList().size(), tools.getList().size());
    }

   

}
