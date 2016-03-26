package farmsim.inventory;
import farmsim.entities.tools.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.Test;

import farmsim.resource.SimpleResource;

public class InventoryTest {
    ArrayList<SimpleResource> compareRucksack = new ArrayList<SimpleResource>();
    Hashtable<String,String> hashtable = new Hashtable<String, String>();
    Inventory rucksack = new Inventory();

    @Test
    public void initialTest() {
        rucksack = new Inventory();
        assertEquals(rucksack.getSize(), 0); //Empty test
    }
    
    /**
     * Tests the setEquippedTool() and getEquippedTool() method of Inventory
     */
    @Test
    public void equippedToolTest() {
    	rucksack = new Inventory();
    	rucksack.addToRucksack((new Axe(0,0,10)).getResource());
    	rucksack.addToRucksack((new Shovel(0,0,10)).getResource());
    	rucksack.addToRucksack((new Hammer(0,0,10)).getResource());
    	rucksack.setEquippedTool(ToolType.AXE);
    	
    	assertEquals(rucksack.getEquippedTool(), ToolType.AXE);
    }
    
    /**
     * Tests the getSize() method of Inventory
     */
    @Test
    public void getSizeTest() {
    	rucksack = new Inventory();
    	rucksack.addToRucksack(new SimpleResource("Banana", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Apple", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Cake", hashtable, 10));
    	
    	assertEquals(rucksack.getSize(), 3);
    }
    
    /**
     * Tests the getResource() method of Inventory
     */
    @Test
    public void getResourceTest() {
    	rucksack = new Inventory();
    	assertEquals(rucksack.getResource("Banana"), null);
    	rucksack.addToRucksack(new SimpleResource("Banana", hashtable, 10));
    	assertEquals(rucksack.getResource("Banana"), new SimpleResource("Banana", hashtable, 10));
    	assertEquals(rucksack.getResource("banana"), new SimpleResource("Banana", hashtable, 10));
    	assertEquals(rucksack.getResource("BANANA"), new SimpleResource("Banana", hashtable, 10));
    	assertEquals(rucksack.getResource("apple"), null);
    }
    
    /**
     * Tests the containsItem() method of Inventory
     */
    @Test
    public void containsItemTest() {
    	rucksack = new Inventory();
    	assertEquals(rucksack.containsItem(new SimpleResource("Banana", hashtable, 5)), false);
    	
    	rucksack.addToRucksack(new SimpleResource("Banana", hashtable, 10));
    	assertEquals(rucksack.containsItem(new SimpleResource("Banana", hashtable, 5)), true);
    }
    
    /**
     * Tests the isFull() method of Inventory
     */
    @Test
    public void isFullTest() {
    	rucksack = new Inventory();
    	assertEquals(rucksack.isFull(), false);

    	rucksack.addToRucksack(new SimpleResource("Banana", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Apple", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Cake", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Melon", hashtable, 10));
    	
    	rucksack.addToRucksack(new SimpleResource("Fruit 1", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Fruit 2", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Fruit 3", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Fruit 4", hashtable, 10));
    	
    	assertEquals(rucksack.isFull(), true);
    }
    
    /**
     * Tests the getList() method of Inventory
     */
    @Test
    public void getListTest() {
    	rucksack = new Inventory();
    	rucksack.addToRucksack(new SimpleResource("Banana", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Apple", hashtable, 10));
    	rucksack.addToRucksack(new SimpleResource("Cake", hashtable, 10));
    	
    	compareRucksack = new ArrayList<SimpleResource>();
    	compareRucksack.add(new SimpleResource("Banana", hashtable, 10));
    	compareRucksack.add(new SimpleResource("Apple", hashtable, 10));
    	compareRucksack.add(new SimpleResource("Cake", hashtable, 10));
    	
    	assertEquals(rucksack.getList(), compareRucksack);
    }

    /**
     * Tests adding single items to the Inventory
     * Also tests adding items to items that already exist in the rucksack
     */
    @Test
    public void addTest() {
    	// Adding single items
    	SimpleResource item2 = new SimpleResource("Banana", hashtable, 10);
        SimpleResource item = new SimpleResource("Apple", hashtable, 1);
        compareRucksack.add(item);
        compareRucksack.add(item2);

        rucksack.addToRucksack(item);
        rucksack.addToRucksack(item2);

        assertEquals(compareRucksack, rucksack.getList());
        
        // Adding to stack
        SimpleResource itemQ = new SimpleResource("Apple", hashtable, 19);
        compareRucksack.remove(item);
        compareRucksack.remove(item2);
        compareRucksack.add(new SimpleResource("Apple", hashtable, 20));
        compareRucksack.add(item2);
        rucksack.addToRucksack(itemQ);

        assertEquals(compareRucksack.size(), rucksack.getSize());
    }
    
    /**
     * Tests that the addToRucksack method does not add when the stack is full
     */
    @Test
    public void addToFullRucksackTest(){
    	rucksack = new Inventory();
        compareRucksack = new ArrayList<SimpleResource>();
        String[] fruit = {"Apple", "Banana", "Cherry", "Orange", "Kiwi", "Coconut", "Lemon", "Lime", "Watermelon", "Dragonfruit"};
        
        for (int i = 0; i<fruit.length; i++){ // Attempt to add 10 fruits
        	rucksack.addToRucksack(new SimpleResource(fruit[i], hashtable, 5));
        }
        
        for (int i = 0; i<8; i++){ // Add 8 fruits to array
        	compareRucksack.add(new SimpleResource(fruit[i], hashtable, 5));
        }
        
        assertEquals(rucksack.getSize(), 8);
        assertEquals(rucksack.getList(), compareRucksack);
    }

    /**
     * Tests removing a certain quantity of an item
     */
    
    @Test
    public void removePartTest() {
    	rucksack = new Inventory();
    	compareRucksack = new ArrayList<SimpleResource>();
    	
    	SimpleResource apples = new SimpleResource("Apple", hashtable, 20);
    	rucksack.addToRucksack(apples);
    	rucksack.removeFromRucksack(apples, 5);
    	
    	compareRucksack.add(new SimpleResource("Apple", hashtable, 15));
    	
    	assertEquals(rucksack.getList(), compareRucksack);
    }
    
    /**
     * Tests the removing a resource with quantity 0 has no effect
     */
    
    @Test
    public void removeNothingTest() {
    	rucksack = new Inventory();
    	compareRucksack = new ArrayList<SimpleResource>();
    	
    	// Remove nothing
    	SimpleResource apples = new SimpleResource("Apple", hashtable, 20);
    	rucksack.addToRucksack(apples);
    	assertEquals(rucksack.removeFromRucksack(apples, 0), 2);
    	    	
    	// Remove non-existent resource
    	SimpleResource blank = new SimpleResource("blank", hashtable, 20);
    	assertEquals(rucksack.removeFromRucksack(blank, 10), 3);
    }
    
    /**
     * Tests that removing a quantity of an item too large is not possible
     */
    
    @Test
    public void removeTooLargeTest() {
    	rucksack = new Inventory();
    	compareRucksack = new ArrayList<SimpleResource>();
    	
    	//Remove too large a quantity of an item
        SimpleResource itemR = new SimpleResource("Apple",hashtable,19);
        rucksack.addToRucksack(itemR);
        assertEquals(rucksack.removeFromRucksack(itemR, 40), 1); //error code 1
    }
}
