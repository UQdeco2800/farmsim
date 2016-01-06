package farmsim.entities.predators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * A class used for loading in the config file for predators
 * @author r-portas
 */
public class PredatorConfigLoader {
    private HashMap<String, HashMap<String, String>> predators;

    /**
     * Intialises the PredatorConfigLoader class
     */
    public PredatorConfigLoader() {
        predators = new HashMap<String, HashMap<String, String>>();
    }

    /**
     * Loads a config file into the object
     * @param filename A string representing the filename of the config file
     */
    public void loadFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line = new String();
            line = reader.readLine();
            String predatorName = new String();
            HashMap<String, String> tempPred = new HashMap<String, String>();

            while (line != null) {
                if (line.length() > 0) {
                    if (line.charAt(0) == '[' && line.charAt(line.length() - 1) == ']') {
                        if (!predatorName.isEmpty()) {
                            predators.put(predatorName, tempPred);
                        }
                        predatorName = line.replace("[", "");
                        predatorName = predatorName.replace("]", "");
                        tempPred = new HashMap<String, String>();

                    } else if (line.contains("=")) {
                        String[] sep = line.split("=");
                        if (sep.length == 2) {
                            tempPred.put(sep[0], sep[1]);
                        }
                    }

                }
                line = reader.readLine();
            }
            reader.close();

            if (!predatorName.isEmpty()) {
                predators.put(predatorName, tempPred);
            }

        } catch (Exception e){
            //System.out.println(e.toString());  @Todo -> user logger!!!!
            // For now, do nothing
            // If this occurs the predator manager will
            // revert to it's backup values
        }
    }

    /**
     * Returns the string representation of the stored data
     * @return String representation of PredatorConfigLoader
     */
    public String toString() {
        return predators.toString();
    }
    
    /**
     * Returns the number of predators loaded in by the config loader
     * @return The count of animals loaded
     */
    public int getLoadedCount() {
    	return predators.size();
    }

    /**
     * Gets all entries relating to the given predator
     * @param predator A string name representing the predator
     * @return Returns a hashmap of data if the predator is found, else returns null
     */
    public HashMap<String, String> getPredator(String predator) {
        return predators.get(predator);
    }
    
    /**
     * Gets the given predator attribute
     * @param predator The name of the predator
     * @param attr The name of the attribute
     * @return A string containing the requested attribute
     */
    public String getPredatorAttribute(String predator, String attr) {
    	return predators.get(predator).get(attr);
    }
    
    /**
     * Gets everything stored inside the config loader 
     * @return A hashmap containing the predators with corresponding key value pairs
     */
    public HashMap<String, HashMap<String, String>> getConfig() {
    	return predators;
    }
}
