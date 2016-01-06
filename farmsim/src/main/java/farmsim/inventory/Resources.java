package farmsim.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Resources {
	Hashtable<String, String> crops = new Hashtable<String, String>();
	Hashtable<String, String> tools = new Hashtable<String, String>();
	ArrayList<String> seeds = new ArrayList<String>(Arrays.asList("Apple Seeds", "Banana Seeds", "Corn Seeds", "Cotton Seeds", "Lemon Seeds"
			, "Lettuce Seeds", "Mango Seeds", "Pear Seeds", "Strawberry Seeds", "Sugarcane Seeds"));
	ArrayList<String> general = new ArrayList<>();
	
	String unknown = "/inventory/unknown.png";
	public Resources() {
		
		crops.put("Apple", "/plantbuttons/apple.png");
		crops.put("Banana", "/plantbuttons/banana.png");
		crops.put("Corn", "/plantbuttons/corn.png");
		crops.put("Cotton", "/plantbuttons/cotton.png");
		crops.put("Lemon", "/plantbuttons/lemon.png");
		crops.put("Lettuce", "/plantbuttons/lettuce.png");
		crops.put("Mango", "/plantbuttons/mango.png");
		crops.put("Pear", "/plantbuttons/Strawberry.png");
		crops.put("Strawberry", "/plantbuttons/strawberry.png");
		crops.put("Sugarcane", "/plantbuttons/sugarcane.png");
		
		crops.put("Bacon", "/unknown.png");
		crops.put("Beef", "/animalprocessing/Beef-64x64.png");
		crops.put("ChickenBreast", "/animalprocessing/Chicken-64x64.png");
		crops.put("DuckBreast", "/animalprocessing/Duck-64x64.png");
		crops.put("Egg", "/animalprocessing/Cegg.png");
		crops.put("Feather", "/animalprocessing/ChickenFeather.png");
		crops.put("Lamb", "/animalprocessing/Lamb-64x64.png");
		crops.put("Milk", "/animalprocessing/Milk-64x64.png");
		crops.put("Wool", "/animalprocessing/Wool-64x64.png");
		
		tools.put("Axe", "/tools/axe.png");
		tools.put("Bucket", "/tools/bucket.png");
		tools.put("Fishing Rod", "");
		tools.put("Hammer", "/tools/hammer.png");
		tools.put("Hoe", "/tools/hoe.png");
		tools.put("Pickaxe", "/tools/pickaxe.png");
		tools.put("plough", "/tools/plough.png");
		tools.put("Shears", "/tools/shears.png");
		tools.put("Shovel", "/tools/shovel.png");
		tools.put("Sickle", "");
		tools.put("Watering Can", "");
	}
	
	public Hashtable<String, String> getCrops(){
		return crops;
	}
	
	public Hashtable<String, String> getTools(){
		return tools;
	}
	public ArrayList<String> getSeeds(){
		return seeds;
	}
	
	public ArrayList<String> getGeneral() {
	    return general;
	}
}
