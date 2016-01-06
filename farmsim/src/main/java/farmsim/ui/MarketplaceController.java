package farmsim.ui;

import common.client.FarmClient;
import common.exception.UnauthenticatedUserException;
import common.resource.SimpleOrder;
import common.resource.SimpleResource;
import common.resource.Tradeable;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.ui.Dialog.DialogMode;
import farmsim.world.WorldManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 * Controller for the Marketplace GUI. Handles connections to the marketplace,
 * buy and sell orders
 * 
 * @author BlueDragon23
 *
 */
public class MarketplaceController implements Initializable {

    private static final Logger LOGGER = LoggerFactory
                    .getLogger(MarketplaceController.class);
    public static final String HOST = "http://deco2800-marketplace.uqcloud.net";
    public static final int PORT = 80;

    @FXML
    private AnchorPane main;
    
    @FXML
    private Tab placeTab;

    @FXML
    private ListView<String> typeList;

    @FXML
    private ListView<Tradeable> outgoingList;

    @FXML
    private ListView<SimpleOrder> buyList;

    @FXML
    private ListView<SimpleOrder> sellList;

    /* Ordering screen */
    @FXML
    private ComboBox<String> typeCombo;
    
    @FXML
    private ComboBox<String> keyCombo;
    
    @FXML
    private TextField valueField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private ToggleGroup orderType;

    @FXML
    private RadioButton buyRadio;

    @FXML
    private RadioButton sellRadio;

    @FXML
    private ToggleGroup buySort;

    @FXML
    private ToggleGroup sellSort;

    private ObservableList<String> typeModel;

    private ObservableList<SimpleOrder> buyOrders;
    private ObservableList<SimpleOrder> sellOrders;

    private HashMap<String, String> attributes;

    private FarmClient client;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        client = WorldManager.getInstance().getWorld().getFarmClient();
        attributes = new HashMap<>();
        
        placeTab.setOnSelectionChanged(new EventHandler<Event>() {
            
            @Override
            public void handle(Event arg0) {
                if (!placeTab.isSelected()) {
                    attributes.clear();
                }
                
            }
        });

        List<String> allTypes = client.allTypes();
        typeModel = FXCollections.observableArrayList(allTypes);
        typeList.setItems(typeModel);

        getOrders();

        /* Add existing resources to the comboBox */
        List<Integer> tradeables = new ArrayList<>();
        SimpleResource resource;
        for (SimpleOrder order : buyOrders) {
            resource = order.getResource();
            if (!(tradeables.contains(resource.getId()))) {
                tradeables.add(resource.getId());
            }
        }
        /* Add resources the player has */
        for (SimpleResource crop : WorldManager.getInstance().getWorld()
                        .getStorageManager().getCrops().getList()) {
            if (!(tradeables.contains(crop.getId()))) {
                tradeables.add(crop.getId());
            }
        }
        for (SimpleResource seeds : WorldManager.getInstance().getWorld()
                        .getStorageManager().getSeeds().getList()) {
            if (!(tradeables.contains(seeds.getId()))) {
                tradeables.add(seeds.getId());
            }
        }
        for (SimpleResource tools : WorldManager.getInstance().getWorld()
                        .getStorageManager().getTools().getList()) {
            if (!(tradeables.contains(tools.getId()))) {
                tradeables.add(tools.getId());
            }
        }
        typeCombo.setItems(typeModel);
    }

    /**
     * Populate the respective lists with all of the buy and sell orders on the
     * marketplace
     */
    private void getOrders() {
        String type;
        List<SimpleOrder> buyOrderList = new ArrayList<>();
        List<SimpleOrder> sellOrderList = new ArrayList<>();
        for (int i = 0; i < typeModel.size(); i++) {
            type = (String) typeModel.get(i);
            buyOrderList.addAll(client.getBuyOrders(type));
            sellOrderList.addAll(client.getSellOrders(type));
        }

        buyOrders = FXCollections.observableArrayList(buyOrderList);

        sellOrders = FXCollections.observableArrayList(sellOrderList);

        buyList.setItems(buyOrders);
        buyList.setCellFactory(new Callback<ListView<SimpleOrder>, ListCell<SimpleOrder>>() {

            @Override
            public ListCell<SimpleOrder> call(ListView<SimpleOrder> arg0) {
                return new OrderCell();
            }
        });
        sellList.setItems(sellOrders);
        sellList.setCellFactory(new Callback<ListView<SimpleOrder>, ListCell<SimpleOrder>>() {

            @Override
            public ListCell<SimpleOrder> call(ListView<SimpleOrder> arg0) {
                return new OrderCell();
            }
        });
    }

    /**
     * Display a generic error. Used where errors really shouldn't happen as
     * confirmation that nothing breaks
     */
    private void genericError() {
        PopUpWindowManager
                        .getInstance()
                        .addPopUpWindow(new Dialog(
                                "Error",
                                "Something happened, view the logs for more information",
                                DialogMode.ERROR));
    }

    @FXML
    /**
     * Accept an existing buy order
     * @param event
     */
    public void acceptBuyOrder(ActionEvent event) {
        /* Get the selected order */
        SimpleOrder selected = buyList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        /* Check if you have the resource */
        SimpleResource resource = selected.getResource();
        if (WorldManager.getInstance().getWorld().getStorageManager()
                        .containsItem(resource)) {
            /* We have it, let's sell it */
            WorldManager.getInstance().getWorld().getStorageManager()
                            .removeItem(resource);

            try {
                client.putSellOrder(resource, selected.getPrice(),
                        selected.getQuantity());
            } catch (UnauthenticatedUserException e) {
                genericError();
                LOGGER.error("Unauthenticated", e);
            }
            getOrders();
        } else {
            PopUpWindowManager.getInstance().addPopUpWindow(
                            new Dialog("Invalid order",
                                            "You don't have the resources to complete order: "
                                                            + selected,
                                            DialogMode.ERROR));
        }
    }

    @FXML
    /**
     * Accept an existing sell order
     * @param event
     */
    public void acceptSellOrder(ActionEvent event) {
        /* Get the selected order */
        SimpleOrder selected = sellList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        /* Check if you have enough money */
        try {
            if (client.getWallet() < selected.getPrice()) {
                PopUpWindowManager
                                .getInstance()
                                .addPopUpWindow(new Dialog(
                                                "Not Enough Money",
                                                "You don't have enough money in the bank to purchase "
                                                                + selected.getType(),
                                                DialogMode.ERROR));
                return;
            }
            /* Place matching buy order */
            client.putBuyOrder(selected.getResource(), selected.getPrice(),
                    selected.getQuantity());
            /* Update orders */
            getOrders();
        } catch (UnauthenticatedUserException e) {
            genericError();
            LOGGER.error("Invalid login", e);
            return;
        }
    }

    public void checkOutgoing(ActionEvent actionEvent) {
        List<Tradeable> outgoingResources = new ArrayList<>();

        try {
            Tradeable tradeable = client.getOutgoing();
            while (tradeable != null) {
                outgoingResources.add(tradeable);

                 if (tradeable != null) {
                      if ("peon".equals(tradeable.getType())) {
                          AgentManager.getInstance().addAgent(
                                  new Agent(tradeable.getAttributes().get("name"),
                                          10, 10, 5));
                      } else {
                          WorldManager.getInstance().getWorld().getStorageManager()
                                  .getGeneral()
                                  .addItem((SimpleResource) tradeable);
                    }
                }

                tradeable = client.getOutgoing();
            }
        } catch (UnauthenticatedUserException e) {
            genericError();
            LOGGER.error("Invalid login", e);
            return;
        }

        outgoingList.setItems(FXCollections.observableArrayList(outgoingResources));
        outgoingList.setCellFactory(new Callback<ListView<Tradeable>, ListCell<Tradeable>>() {

            @Override
            public ListCell<Tradeable> call(ListView<Tradeable> arg0) {
                return new TradeableCell();
            }
        });
    }

    @FXML
    /**
     * Update the way the buy list is sorted
     * @param event
     */
    public void updateBuySort(ActionEvent event) {
        ToggleButton selected = (ToggleButton) buySort.getSelectedToggle();
        if (selected != null) {
            switch (selected.getText()) {
            case "ID":
                buyOrders.sort(new IdComparator());
                break;
            case "Type":
                buyOrders.sort(new TypeComparator());
                break;
            case "Price":
                buyOrders.sort(new PriceComparator());
                break;
            default:
                buyOrders.sort(new IdComparator());
            }
        }
    }

    @FXML
    /**
     * Update the way the sell list is sorted
     * @param event
     */
    public void updateSellSort(ActionEvent event) {
        ToggleButton selected = (ToggleButton) sellSort.getSelectedToggle();
        if (selected != null) {
            switch (selected.getText()) {
            case "ID":
                sellOrders.sort(new IdComparator());
                break;
            case "Type":
                sellOrders.sort(new TypeComparator());
                break;
            case "Price":
                sellOrders.sort(new PriceComparator());
                break;
            default:
                sellOrders.sort(new IdComparator());
            }
        }
    }
    
    @FXML
    /**
     * Add the values in the textfield to the attributes map
     */
    public void addAttribute(ActionEvent event) {
        attributes.put(keyCombo.getSelectionModel().getSelectedItem(), valueField.getText());
    }

    @FXML
    /**
     * Place a new order based on the entries in the input fields
     * @param event
     */
    public void placeOrder(ActionEvent event) {
        Tradeable item = new SimpleResource(typeCombo.getSelectionModel().getSelectedItem(), attributes);
        /* Get the price */
        long price = 0;
        try {
            price = Long.parseLong(priceField.getText());
        } catch (NumberFormatException e) {
            PopUpWindowManager.getInstance().addPopUpWindow(
                            new Dialog("Invalid Price", "A price of "
                                            + priceField.getText()
                                            + " is invalid", DialogMode.ERROR));
            return;
        }

        /* Get the quantity */
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            PopUpWindowManager.getInstance().addPopUpWindow(
                            new Dialog("Invalid Quantity", "A quantity of "
                                            + quantityField.getText()
                                            + " is invalid", DialogMode.ERROR));
            return;
        }
        /* Order type */
        boolean isBuy = buyRadio.isSelected();

        /* Place order */
        try {
            if (isBuy) {
                client.putBuyOrder(item, price, quantity);
            } else {
                client.putSellOrder(item, price, quantity);
            }
        } catch (UnauthenticatedUserException e) {
            genericError();
            LOGGER.error("Invalid authentification", e);
        }
        typeCombo.getSelectionModel().clearSelection();
        keyCombo.getSelectionModel().clearSelection();
        valueField.setText("");
        priceField.setText("");
        quantityField.setText("1");
        attributes.clear();

        /* Update orders and notify user */
        getOrders();
        PopUpWindowManager.getInstance().addPopUpWindow(
                        new Dialog("Success!", "Order for " + quantity + " "
                                        + item + " at price " + price
                                        + " each placed successfully",
                                        DialogMode.MESSAGE));
    }

    /**
     * Renderer for orders, to make them a little more user friendly.
     * UNIMPLEMENTED May also be extended to do something with sorting
     * 
     * @author BlueDragon23
     *
     */
    class OrderCell extends ListCell<SimpleOrder> {

        @Override
        protected void updateItem(SimpleOrder value, boolean empty) {
            super.updateItem(value, empty);
            if (!empty) {
                setText("Type: "
                                + String.format("%12s", value.getResource()
                                                .getType())
                                + " | Price: "
                                + String.format("%8s", value.getPrice())
                                + " | Quantity: "
                                + String.format("%8s", value.getQuantity())
                                + String.format(" | Attributes: %s", value
                                                .getResource().getAttributes()));
            }
        }
    }

    /**
     * Renderer for tradeables, to make them a little more user friendly.
     */
    class TradeableCell extends ListCell<Tradeable> {

        @Override
        protected void updateItem(Tradeable tradeable, boolean empty) {
            super.updateItem(tradeable, empty);
            if (!empty) {
                setText("Type: "
                        + String.format("%12s", tradeable.getType())
                        + " | Quantity: "
                        + String.format("%8s", tradeable.getQuantity())
                        + String.format(" | Attributes: %s",
                        tradeable.getAttributes()));
            }
        }
    }

    /**
     * Renderer for id
     * 
     * @author BlueDragon23
     *
     */
    class ResourceIdCell extends ListCell<Integer> {

        @Override
        protected void updateItem(Integer id, boolean empty) {
            super.updateItem(id, empty);
            if (!empty && id != null) {
                Tradeable item = client.getResource(id);
                setText(item.getType() + " " + item.getAttributes());
            }
        }
    }

    /*
     * Comparators for SimpleOrders: The default ordering is ID, Type, Price,
     * Quantity. Each comparator shifts one element to the top, the others stay
     * in order
     */

    /**
     * Sorts ID, Type, Price, Quantity
     * 
     * @author Aidan
     *
     */
    class IdComparator implements Comparator<SimpleOrder> {

        @Override
        public int compare(SimpleOrder o1, SimpleOrder o2) {
            if (o1.getId() != o2.getId()) {
                return o1.getId() - o2.getId();
            } else if (!o1.getResource().getType()
                            .equals(o2.getResource().getType())) {
                return o1.getResource().getType()
                                .compareTo(o2.getResource().getType());
            } else if (o1.getPrice() != o2.getPrice()) {
                return (int) (o1.getPrice() - o2.getPrice());
            } else {
                return o1.getQuantity() - o2.getQuantity();
            }
        }
    }

    /**
     * Sorts Type, ID, Price, Quantity
     * 
     * @author Aidan
     *
     */
    class TypeComparator implements Comparator<SimpleOrder> {

        @Override
        public int compare(SimpleOrder o1, SimpleOrder o2) {
            if (!o1.getResource().getType().equals(o2.getResource().getType())) {
                return o1.getResource().getType()
                                .compareTo(o2.getResource().getType());
            } else if (o1.getId() != o2.getId()) {
                return o1.getId() - o2.getId();
            } else if (o1.getPrice() != o2.getPrice()) {
                return (int) (o1.getPrice() - o2.getPrice());
            } else {
                return o1.getQuantity() - o2.getQuantity();
            }
        }
    }

    /**
     * Sorts Price, ID, Type, Quantity
     * 
     * @author Aidan
     *
     */
    class PriceComparator implements Comparator<SimpleOrder> {

        @Override
        public int compare(SimpleOrder o1, SimpleOrder o2) {
            if (o1.getPrice() != o2.getPrice()) {
                return (int) (o1.getPrice() - o2.getPrice());
            } else if (o1.getId() != o2.getId()) {
                return o1.getId() - o2.getId();
            } else if (!o1.getResource().getType()
                            .equals(o2.getResource().getType())) {
                return o1.getResource().getType()
                                .compareTo(o2.getResource().getType());
            } else {
                return o1.getQuantity() - o2.getQuantity();
            }
        }
    }
}
