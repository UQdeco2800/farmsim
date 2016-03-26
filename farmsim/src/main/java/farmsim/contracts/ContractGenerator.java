package farmsim.contracts;

import java.util.ArrayList;
import farmsim.money.*;
import farmsim.inventory.*;
import farmsim.contracts.Contract;
import farmsim.world.*;
import farmsim.resource.SimpleResource;


/**
 * Creates a random contract
 * 
 * @author wondertroy
 *
 */
public class ContractGenerator {

    private static String contractGiver;
    private static ArrayList<String> contractGiverList;
    private static SimpleResource cropType;
    private static ArrayList<SimpleResource> cropList;
    private int amount;
    private static int repeatCount;
    private static int interval;
    private static int reward;
    private static int penalty;
    private static String description;
    private static ArrayList<String> descriptionList;
    private static int expiryDate;
    private static ArrayList<Contract> premadeContracts;
    private static SimpleResourceHandler crops = SimpleResourceHandler.getInstance();

    /**
	* Constructs a generator to make contracts
	*/
	public ContractGenerator() {
        // populate crop list using SimpleResources
        cropList = new ArrayList<SimpleResource>();
        cropList.add(crops.apple);
        cropList.add(crops.banana);
        cropList.add(crops.lettuce);
        cropList.add(crops.mango);
        cropList.add(crops.sugarCane);
        cropList.add(crops.plant);

        // populate contract givers - currently empty
        contractGiverList = new ArrayList<String>();
        contractGiverList.add("Generic Company 1");
        contractGiverList.add("Generic Company 2");
        contractGiverList.add("Generic Company 3");
        contractGiverList.add("Generic Company 4");

        // populate descriptions - currently empty
        descriptionList = new ArrayList<String>();
        descriptionList.add("Generic Description 1");
        descriptionList.add("Generic Description 2");
        descriptionList.add("Generic Description 3");
        descriptionList.add("Generic Description 4");
    }

    /**
     * chooses a random contract from a set list of contract givers with some
     * random attributs
     * 
     * @return Contract
     */
    public Contract generatePreMadeContract() {
        premadeContracts = new ArrayList<Contract>();
        amount = randomAmount();
        cropType = randomCrop();
        repeatCount = randomRepeats();
        // deliver every 7 days.
        interval = (int) (Math.random()*7 + 1);
        // 3 days to accept contract.
        expiryDate = (int) (Math.random()*7 + 1);
        // reward between 500 and 1000
        reward = randomReward();
        // penalty up to .5 of total money held.
        penalty = randomPenalty();

        // add a decoworths contract
        Contract decoWorthsContract = new Contract("DecoWorths", cropType,
                amount, repeatCount, interval, reward, penalty,
                "We have lots of money. We want to make more. "
                        + "Don't care if it is fresh",
                expiryDate);
        premadeContracts.add(decoWorthsContract);
		
		Contract bill = new Contract("Billy Bill ", cropType,
                amount, repeatCount, interval, reward, penalty,
                "I've heard you have bills to pay."
                        + "Let me help you.",
                expiryDate);
        premadeContracts.add(bill);
		
		Contract george = new Contract("George", cropType,
                amount, repeatCount, interval, reward, penalty,
                "Bring me lots of crops, as many as I kill in my books",
                expiryDate);
        premadeContracts.add(george);
		
		Contract mum = new Contract("DecoWorths", cropType,
                amount, repeatCount, interval, reward, penalty,
                "Darling, please bring mummy some food, I'm starving",
                expiryDate);
        premadeContracts.add(mum);

        // add a Todd contract
        Contract todd = new Contract("Todd Ingrim", cropType, amount,
                repeatCount, interval, reward, penalty,
                "Basically, Being Vegan makes you better than "
                        + "most people. Bingo!",
                expiryDate);
        premadeContracts.add(todd);

        // add a Sasha contract
        Contract sasha = new Contract("Potato Girl", cropType, amount,
                repeatCount, interval, reward, penalty,
                "I need potatoes, they need to be fresh.", expiryDate);
        premadeContracts.add(sasha);

        // add a cabbage man contract
        Contract cabbage = new Contract("Cabbage Merchant", cropType, amount,
                repeatCount, interval, reward, penalty,
                "Some bozo destroyed my cabbages again, "
                        + "I'll need some more",
                expiryDate);
        premadeContracts.add(cabbage);

        int rand = (int) (Math.random() * premadeContracts.size());
        return premadeContracts.get(rand);
    }

    /**
     * Creates a random amount of crops required to fulfill contract.
     * 500<amount<1500
     * 
     * @return int amount.
     */
    private int randomAmount() {
    	World world = WorldManager.getInstance().getWorld();
    	int weeks = world.getTimeManager().getWeeks();
        return (int) (Math.random() * 100 * weeks + 10);
    }

    /**
     * will choose a random crop and return it
     * 
     * @return String Crop
     */
    private static SimpleResource randomCrop() {
        int rand = (int) (Math.random() * cropList.size());
        return cropList.get(rand);
    }

    /**
     * will choose a random number of repeats between 1 and 10
     * 
     * @return int repeats
     */
    private static int randomRepeats() {
    	World world = WorldManager.getInstance().getWorld();
    	int weeks = world.getTimeManager().getWeeks();
        return (int) (Math.random() * 7 * weeks + 2);
    }

    /**
     * chooses a random reward between 500 and 1500
     * 
     * @return int reward
     */
    private static int randomReward() {
    	World world = WorldManager.getInstance().getWorld();
    	int balance = (int) world.getMoneyHandler().getAmount();
        return (int) (Math.random() * (balance / 4) + balance / 8);
    }

    /**
     * chooses a random penalty up to .5*current money
     * 
     * @return int penalty
     */
    private static int randomPenalty() {
    	World world = WorldManager.getInstance().getWorld();
    	int balance = (int) world.getMoneyHandler().getAmount();
    	int week = world.getTimeManager().getWeeks();
        return (int) (Math.random() * 0.5 * balance + 200 * week);
    }
}
