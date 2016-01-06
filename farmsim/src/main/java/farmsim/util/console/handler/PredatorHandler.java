package farmsim.util.console.handler;

import java.util.Arrays;

import farmsim.entities.predators.PredatorManager;
import farmsim.entities.predators.WolfPredator;
import farmsim.GameManager;
import farmsim.entities.predators.BearPredator;
import farmsim.entities.predators.Predator;
import farmsim.util.console.Console;
import farmsim.world.WorldManager;

/**
 * Handles predators within the console
 * @author r-portas
 *
 */
public class PredatorHandler extends BaseHandler implements BaseHandlerInterface {
	PredatorManager predatorManager;
	
	public PredatorHandler(){
		super();
		this.addCommands();
		this.setName("Predators");
		try {
			predatorManager = WorldManager.getInstance().getWorld().getPredatorManager();
		} catch (NullPointerException e) {
			// Caused by UI tests
		}
	}
	
	private void addCommands(){
		addSingleCommand("predators",
				"list - Lists all predators in the currently in game\n"
				+ "count - Gets a total count of predators in the game\n"
				+ "kill all - Kills all the predators\n"
				+ "disable - Disables predators\n"
				+ "enable - Enables predators\n"
				+ "spawn bear - Spawns a bear\n"
				+ "spawn rabbit - Spawns a rabbit\n"
				+ "spawn wolf - Spawns a wolf\n");
	}
	
	@Override
	public void handle(String[] parameters){
		try {
			predatorManager = WorldManager.getInstance().getWorld().getPredatorManager();
		} catch (NullPointerException e) {
			// Caused by UI tests
		}
		//Console.getInstance().println(Arrays.asList(parameters).toString());
		if (parameters != null){
			if (parameters[0].equals("predators")){
				
				if (parameters.length == 2 && parameters[1].equals("disable")){
					Console.getInstance().println("Disabling predators");
					predatorManager.disablePredators();
				} else if (parameters.length == 2 && parameters[1].equals("enable")){
					Console.getInstance().println("Enabling predators");
					predatorManager.enablePredators();
				}
				
				if (parameters.length == 2 && parameters[1].equals("list")){
					printPredators();
				} else if (parameters.length == 3 && parameters[1].equals("kill") 
						&& parameters[2].equals("all")){
					killPredators();
				} else if (parameters.length == 2 && parameters[1].equals("count")){
					Console.getInstance().println("Total predators in game: " + 
							predatorManager.getPredatorCount());
				} else if (parameters.length == 3 && parameters[1].equals("spawn")){
					if (parameters[2].equals("bear")){
						Console.getInstance().println("Spawning bear");
						predatorManager.spawnPredator("bear");
					} else if (parameters[2].equals("wolf")){
						Console.getInstance().println("Spawning wolf");
						predatorManager.spawnPredator("wolf");
					} else if (parameters[2].equals("rabbit")){
						Console.getInstance().println("Spawning rabbit");
						predatorManager.spawnPredator("rabbit");
					} else if (parameters[2].equals("alligator")) {
						Console.getInstance().println("Spawning alligator");
						predatorManager.spawnPredator("alligator");
					} else if (parameters[2].equals("mole")) {
						Console.getInstance().println("Spawning mole");
						predatorManager.spawnPredator("mole");
					}
				}
			}
		}
		
	}
	
	private void printPredators(){
		Console.getInstance().println("Current predators in game:");
		for (Predator p : predatorManager.getPredators()){
			Console.getInstance().println("-> " + p.toString());
		}
	}
	
	private void killPredators(){
		Console.getInstance().println("Killing predators:");
		for (Predator p : predatorManager.getPredators()){
			Console.getInstance().println("-> Killing " + p.toString());
			p.killPredator();
		}
		Console.getInstance().println("Finished killing predators");
	}
}
