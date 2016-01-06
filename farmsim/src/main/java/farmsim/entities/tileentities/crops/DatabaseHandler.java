package farmsim.entities.tileentities.crops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A handler which implements the Singleton Pattern for reading the plant data
 * from the database and storing it so it can be easily accessed when executing
 * a plant order.
 * 
 * @author Blake
 *
 */
public class DatabaseHandler {

    // the single instance of DatabaseHandler
    private static final DatabaseHandler INSTANCE = new DatabaseHandler();
    // the URL used to access the database
    private static String DATABASE_URL =
            "jdbc:derby:decoFarmDB;user=decofarm;password=decofarm";
    // a structure used to map each plant type to its properties
    private HashMap<String, HashMap<String, Object>> crops;
    // the attributes of a given plant type
    private HashMap<String, Object> attributes;
    
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(DatabaseHandler.class);

    private DatabaseHandler() {
        crops = new HashMap<String, HashMap<String, Object>>();
    }

    public static DatabaseHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Reads the data stored in the database and stores it in an internal
     * structure so it can be more easily accessed.
     */
    public void importData() {
        try (Connection connection =
                DriverManager.getConnection(DATABASE_URL)) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM plants", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No data to read");
                    } else {
                        do {
                            attributes = new HashMap<String, Object>();
                            attributes.put("Clevel", rs.getInt("Clevel"));
                            attributes.put("Gtime1", rs.getInt("Gtime1"));
                            attributes.put("Gtime2", rs.getInt("Gtime2"));
                            attributes.put("Gtime3", rs.getInt("Gtime3"));
                            attributes.put("Gtime4", rs.getInt("Gtime4"));
                            attributes.put("Gtime5", rs.getInt("Gtime5"));
                            attributes.put("Hquantity", rs.getInt("Hquantity"));
                            attributes.put("StNames1", rs.getString("StNames1"));
                            attributes.put("StNames2", rs.getString("StNames2"));
                            attributes.put("StNames3", rs.getString("StNames3"));
                            attributes.put("StNames4", rs.getString("StNames4"));
                            attributes.put("DeadStage1", rs.getString("DeadStage1"));
                            attributes.put("DeadStage2", rs.getString("DeadStage2"));
                            attributes.put("DeadStage3", rs.getString("DeadStage3"));
                            attributes.put("Penvironment", rs.getString("Penvironment"));
                            attributes.put("Nenvironment", rs.getString("Nenvironment"));
                            crops.put(rs.getString("Pname"), attributes);
                        } while (rs.next());
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }

    }

    /**
     * Provides a copy of the data read from the database for the given plant.
     * 
     * @param plant
     *      The plant that the returned data must relate to.
     * @return 
     *      A copy of the imported data.
     */
    public Map<String, Object> getPlantData(String plant) {
        HashMap<String, Object> copy = new HashMap<String, Object>(crops.get(plant));
        return copy;
    }

    /**
     * Returns all of the possible planting options.
     * 
     * @return 
     *      A set containing all possible plants.
     */
    public Set<String> getPlants() {
        return crops.keySet();
    }
}
