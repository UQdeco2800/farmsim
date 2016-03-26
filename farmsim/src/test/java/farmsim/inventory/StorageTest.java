package farmsim.inventory;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import org.junit.Test;

import farmsim.resource.SimpleResource;

public class StorageTest {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    @Test
    public void initialTest() {
    	Storage storage = new Storage();
 
        assertEquals(storage.getLevel(), 20);

    }
/*
    @Test
    public void addTest() {
    	Storage storage = new Storage();
    	ArrayList<SimpleResource> compare = new ArrayList<SimpleResource>();
    	
        //Test one resource
        SimpleResource item = new SimpleResource("Apple",hashtable,1);
        SimpleResource item1 = new SimpleResource("Banana",hashtable,1);
        compare.add(item);
        storage.addItem(item);

        assertEquals(compare.get(0),storage.getList().get(0));



        //Adding more quantity
        SimpleResource itemQ = new SimpleResource("Apple",hashtable,19);
        compare.remove(item);
        compare.add(new SimpleResource("Apple", hashtable, 20));
        storage.addItem(itemQ);

        assertEquals(compare.size(), storage.getList().size());
        assertEquals(compare.get(0), storage.getList().get(0));
        
        //Storage full
        storage.setLevel(1);
        SimpleResource itemR = new SimpleResource("Ringnuts",hashtable,9);
        assertEquals(1,storage.addItem(itemR));

        //More things
        
        compare.add(item1);
        storage.addItem(item1);
        assertEquals(compare.get(0), storage.getList().get(0)); 

        
    }

    @Test
    public void removeTest() {
    	Storage storage = new Storage();
    	ArrayList<SimpleResource> compare = new ArrayList<SimpleResource>();
    	
        //Remove too large a quantity of an item
        SimpleResource item = new SimpleResource("Apple",hashtable,19);
        storage.addItem(item);
        SimpleResource nonExistent = new SimpleResource("Blank",hashtable,19);
        assertEquals(storage.removeItem(item, 40), 1);
        assertEquals(storage.removeItem(nonExistent, 10), 3);

        //Remove some of an item
        compare.add(new SimpleResource("Apple",hashtable,18));
        storage.removeItem(item, 1);
        assertEquals(compare, storage.getList());
   

        //Remove nothing
        assertEquals(2,storage.removeItem(item, 0));

        //Remove all of one type of item
        compare.remove(0);
        storage.removeItem(item, 18);
        assertEquals(compare, storage.getList());


    }*/

    @Test
    public void testQuantity() {
    	Storage storage = new Storage();
        storage.setLevel(10);
        SimpleResource item1 = new SimpleResource("G",hashtable,19);
        SimpleResource item2 = new SimpleResource("Jk",hashtable,19);
        SimpleResource item3 = new SimpleResource("guts",hashtable,9);

        storage.addItem(item1);
        storage.addItem(item2);
        storage.addItem(item3);

        assertEquals(3, storage.getSize());

        storage.removeItem(item3, 9);
        storage.removeItem(item2, 19);
        storage.removeItem(item1, 19);
        assertEquals(0, storage.getSize());
    }

    
    @Test
    public void testGetList() {
    	Storage storage = new Storage();
        storage.setLevel(10);
    	SimpleResource item1 = new SimpleResource("Apple", hashtable, 20);
        SimpleResource item2 = new SimpleResource("Banana", hashtable, 10);
        SimpleResource item3 = new SimpleResource("Grape", hashtable, 9);
        
        storage.addItem(item1);
        storage.addItem(item2);
        storage.addItem(item3);
        
        ArrayList<SimpleResource> list = new ArrayList<SimpleResource>();
        list.add(item1);
        list.add(item2);
        list.add(item3);
        
        assertEquals(list, storage.getList());
    }
    
   
}

