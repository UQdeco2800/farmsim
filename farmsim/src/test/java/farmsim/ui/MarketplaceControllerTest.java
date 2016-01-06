package farmsim.ui;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import common.client.FarmClient;
import common.client.GenericClient;
import common.resource.SimpleOrder;
import common.resource.SimpleResource;
import farmsim.inventory.Storage;
import farmsim.inventory.StorageManager;
import farmsim.world.World;
import farmsim.world.WorldManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WorldManager.class)
public class MarketplaceControllerTest {
    
    private GenericClient client;
    private MarketplaceController controller;

    @Before
    public void setUp() throws Exception {
        client = Mockito.mock(FarmClient.class);
        PowerMockito.mockStatic(WorldManager.class);
        WorldManager manager = Mockito.mock(WorldManager.class);
        Mockito.when(WorldManager.getInstance()).thenReturn(manager);
        World world = Mockito.mock(World.class);
        Mockito.when(manager.getWorld()).thenReturn(world);
        
        /* Setup StorageManager behaviour */
        StorageManager storageManager = Mockito.mock(StorageManager.class);
        Mockito.when(world.getStorageManager()).thenReturn(storageManager);
        Mockito.when(storageManager.getCrops()).thenReturn(new Storage());
        Mockito.when(storageManager.getSeeds()).thenReturn(new Storage());
        Mockito.when(storageManager.getTools()).thenReturn(new Storage());
        Mockito.when(storageManager.getGeneral()).thenReturn(new Storage());
        
        /* Setup client behaviour */
        Mockito.when(world.getFarmClient()).thenReturn((FarmClient) client);
        Mockito.when(client.allTypes()).thenReturn(new ArrayList<String>());
        Mockito.when(client.getBuyOrders(Mockito.anyString())).thenReturn(new ArrayList<SimpleOrder>());
        Mockito.when(client.getSellOrders(Mockito.anyString())).thenReturn(new ArrayList<SimpleOrder>());
    }
    
    @Ignore
    @Test
    public void testInitialize() {
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                URL location = getClass().getResource("/Login.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                try {
                    Parent root = fxmlLoader.load();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testComparator() {
        ArrayList<SimpleOrder> orders = new ArrayList<>();
        ArrayList<SimpleOrder> sortedOrders = new ArrayList<>();
        controller = Mockito.mock(MarketplaceController.class);
        /* Create elements for testing. Note that same id -> same type/attributes */
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    orders.add(new SimpleOrder(i, new SimpleResource(i == 1 ? "Peon2" : "Peon", new HashMap<String, String>()), 'B', j, (long) k));
                    sortedOrders.add(orders.get(i * 2 * 2 + j * 2 + k));
                }
            }
        }
        /* Type sorting */
        sortedOrders.sort(controller.new TypeComparator());
        assertTrue(sortedOrders.get(0).equals(orders.get(0)));
        assertTrue(sortedOrders.get(1).equals(orders.get(2)));
        assertTrue(sortedOrders.get(2).equals(orders.get(1)));
        assertTrue(sortedOrders.get(3).equals(orders.get(3)));
        assertTrue(sortedOrders.get(4).equals(orders.get(8)));
        assertTrue(sortedOrders.get(5).equals(orders.get(10)));
        assertTrue(sortedOrders.get(6).equals(orders.get(9)));
        assertTrue(sortedOrders.get(7).equals(orders.get(11)));
        assertTrue(sortedOrders.get(8).equals(orders.get(4)));
        assertTrue(sortedOrders.get(9).equals(orders.get(6)));
        assertTrue(sortedOrders.get(10).equals(orders.get(5)));
        assertTrue(sortedOrders.get(11).equals(orders.get(7)));
        
        /* Price sorting */
        sortedOrders.sort(controller.new PriceComparator());
        assertTrue(sortedOrders.get(0).equals(orders.get(0)));
        assertTrue(sortedOrders.get(1).equals(orders.get(2)));
        assertTrue(sortedOrders.get(2).equals(orders.get(4)));
        assertTrue(sortedOrders.get(3).equals(orders.get(6)));
        assertTrue(sortedOrders.get(4).equals(orders.get(8)));
        assertTrue(sortedOrders.get(5).equals(orders.get(10)));
        assertTrue(sortedOrders.get(6).equals(orders.get(1)));
        assertTrue(sortedOrders.get(7).equals(orders.get(3)));
        assertTrue(sortedOrders.get(8).equals(orders.get(5)));
        assertTrue(sortedOrders.get(9).equals(orders.get(7)));
        assertTrue(sortedOrders.get(10).equals(orders.get(9)));
        assertTrue(sortedOrders.get(11).equals(orders.get(11)));
        
        /* ID sorting */
        sortedOrders.sort(controller.new IdComparator());
        assertTrue(sortedOrders.get(0).equals(orders.get(0)));
        assertTrue(sortedOrders.get(1).equals(orders.get(2)));
        assertTrue(sortedOrders.get(2).equals(orders.get(1)));
        assertTrue(sortedOrders.get(3).equals(orders.get(3)));
        assertTrue(sortedOrders.get(4).equals(orders.get(4)));
        assertTrue(sortedOrders.get(5).equals(orders.get(6)));
        assertTrue(sortedOrders.get(6).equals(orders.get(5)));
        assertTrue(sortedOrders.get(7).equals(orders.get(7)));
        assertTrue(sortedOrders.get(8).equals(orders.get(8)));
        assertTrue(sortedOrders.get(9).equals(orders.get(10)));
        assertTrue(sortedOrders.get(10).equals(orders.get(9)));
        assertTrue(sortedOrders.get(11).equals(orders.get(11)));
    }
}
