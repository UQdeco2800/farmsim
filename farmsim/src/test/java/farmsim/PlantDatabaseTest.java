package farmsim;

import java.io.IOException;
import java.net.URL;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlantDatabaseTest {

    // database tester (used for connecting to the database)
    private static IDatabaseTester dbTester;
    // store being tested
    // private DatabaseHandler handler;

    /**
     * Connect to the database
     * 
     * @throws ClassNotFoundException
     */

    public static void setUpClass() throws ClassNotFoundException {
        assertTrue(true);
        /*
         * dbTester = new
         * JdbcDatabaseTester("org.apache.derby.jdbc.EmbeddedDriver",
         * "jdbc:derby:decoFarmDB;user=decofarm;password=decofarm;create=true");
         */}

    /**
     * Load in a dataset from an XML file
     * 
     * @return data set to be used for testing
     * @throws DataSetException
     * @throws IOException
     */
    private IDataSet getDataSet() throws DataSetException, IOException {
        assertTrue(true);
        /*
         * URL url = DatabaseHandler.class.getClassLoader().getResource(
         * "PlantDatabaseTestFile"); FlatXmlDataSetBuilder builder = new
         * FlatXmlDataSetBuilder(); builder.setColumnSensing(true); return
         * builder.build(url);
         */
        return null;
    }
}
