/**
 *
 */
package farmsim.inventory;

import java.util.Hashtable;
import common.resource.SimpleResource;

/**
 * @author wondertroy
 *
 * a singleton class that has public objects to use.
 */
public class SimpleResourceHandler {

    private static final SimpleResourceHandler INSTANCE = new SimpleResourceHandler();

    //Crops
    public SimpleResource apple;
    public SimpleResource banana;
    public SimpleResource lettuce;
    public SimpleResource mango;
    public SimpleResource sugarCane;
    public SimpleResource plant;
    private Hashtable<String, String> appleProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> bananaProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> lettuceProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> mangoProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> sugarCaneProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> plantProperties
            = new Hashtable<String, String>();

    //Produce
    public SimpleResource bacon;
    public SimpleResource beef;
    public SimpleResource chickenBreast;
    public SimpleResource duckBreast;
    public SimpleResource egg;
    public SimpleResource feathers;
    public SimpleResource fish;
    public SimpleResource tuna;
    public SimpleResource lamb;
    public SimpleResource milk;
    public SimpleResource wool;
    private Hashtable<String, String> baconProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> beefProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> chickenBreastProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> duckBreastProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> eggProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> feathersProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> fishProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> lambProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> milkProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> woolProperties
            = new Hashtable<String, String>();
    private Hashtable<String, String> tunaProperties
            = new Hashtable<String, String>();


    private SimpleResourceHandler(){
        //Crops
        appleProperties.put("Value:", "1");
        apple = new SimpleResource("Apple", appleProperties);
        bananaProperties.put("Value:", "1");
        banana = new SimpleResource("Banana", bananaProperties);
        lettuceProperties.put("Value:", "1");
        lettuce = new SimpleResource("Lettuce", lettuceProperties);
        mangoProperties.put("Value:", "1");
        mango = new SimpleResource("Mango", mangoProperties);
        appleProperties.put("Value:", "1");
        sugarCane = new SimpleResource("Sugar Cane", sugarCaneProperties);
        plantProperties.put("Value:", "1");
        plant = new SimpleResource("Apple", plantProperties);

        //Produce
        baconProperties.put("Value:", "1");
        bacon = new SimpleResource("Bacon", baconProperties);
        beefProperties.put("Value:", "1");
        beef = new SimpleResource("Beef", beefProperties);
        chickenBreastProperties.put("Value:", "1");
        chickenBreast = new SimpleResource("ChickenBreast", chickenBreastProperties);
        duckBreastProperties.put("Value:", "1");
        duckBreast = new SimpleResource("DuckBreast", duckBreastProperties);
        eggProperties.put("Value:", "1");
        egg = new SimpleResource("Egg", eggProperties);
        feathersProperties.put("Value:", "1");
        feathers = new SimpleResource("Feathers", feathersProperties);
        fishProperties.put("Value:", "1");
        fish = new SimpleResource("Fish", fishProperties);
        tunaProperties.put("Value:", "1");
        tuna = new SimpleResource("Tuna", tunaProperties);
        lambProperties.put("Value:", "1");
        lamb = new SimpleResource("Lamb", lambProperties);
        milkProperties.put("Value:", "1");
        milk = new SimpleResource("Milk", milkProperties);
        woolProperties.put("Value:", "1");
        wool = new SimpleResource("Wool", woolProperties);
    }
	
	/**
	* @return instance of simpleResourceHandler
	*/
    public static SimpleResourceHandler getInstance(){
        return INSTANCE;
    }
}
