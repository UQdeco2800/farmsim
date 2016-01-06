package farmsim.ui;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;

import org.apache.derby.impl.tools.sysinfo.Main;

import common.client.FarmClient;
import common.marketGUI.*;
import common.resource.SimpleResource;
import farmsim.inventory.Resources;
import farmsim.inventory.Storage;
import farmsim.inventory.StorageManager;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controls storage UI
 * @author gelbana
 *
 */
public class StorageController implements Initializable{

    @FXML
    private Pane mainPane;

    @FXML
    private GridPane cropTab = new GridPane();

    @FXML
    private GridPane toolTab = new GridPane();

    @FXML
    private Button removeButton;

    @FXML
    private Button sellButton;

    @FXML
    private Button equipButton;
    
    @FXML
    private TextField count;
    
    @FXML
    private Slider countSlider;
    
    @FXML
    private Label dialogMessage;
    
    @FXML
    private ImageView dialogImage;
    
    private Pane selected;
    private SimpleResource selectedItem;
    private int dragging;
    private Resources resources = new Resources();
    Storage crops;
    Storage tools;
	public void initialize(URL arg0, ResourceBundle arg1) {
		crops = WorldManager.getInstance().getWorld().getStorageManager().getCrops();
		tools = WorldManager.getInstance().getWorld().getStorageManager().getTools();
		
    	displayCrops();
    	dialogImage.opacityProperty().set(0);
    	dialogMessage.opacityProperty().set(0);
    	dialogMessage.setAlignment(Pos.CENTER);
	}
    
	/**
	 * Loads in crops
	 */
    private void displayCrops(){
    	cropTab.getChildren().clear();
    	toolTab.getChildren().clear();
    	for (int i=0;i<crops.getSize();i++){
    		Pane app=createItem(i,7, 1);	
    	}
    	for (int i=0;i<tools.getSize();i++){ 
    		Pane app1=createItem(i,7, 2);	
    	}
    }
    
    /**
     * Adds individual crops to corresponding parts position on GridPane.
     * Sets up mouse events, drag and tooltip events.
     * Allows interaction between buttons and crops in storage instance.
     * @param itemIndex
     * @param maxRow
     * @return Pane to add to a pop up window containing UI
     */
    private Pane createItem(int itemIndex, int maxRow, int type) {
    	SimpleResource crop;
		Pane item = new StackPane(); //Stack pane containing item 
		item.setStyle("-fx-background-color: transparent; "
				+ "-fx-border-color:grey;"); //Set style
		item.setId("item" + Integer.toString(type) + "-" + Integer.toString(itemIndex));
		int x = itemIndex % maxRow; //Get item row
		int y = itemIndex / maxRow; //Get item column
		
		if (type == 1) { //Add to gridpane at coordinates
			cropTab.add(item, x, y);
			crop = crops.getList().get(itemIndex);
		} else {
			toolTab.add(item, x, y);
			crop = tools.getList().get(itemIndex);
		}
		 
		item.setPrefSize(20, 20);
		
		Image path;
    	ImageView image;
		
		//Try find picture
		if (resources.getCrops().containsKey(crop.getType())) { //Check for attribute
			path = new Image(Main.class.getResourceAsStream((resources.getCrops().get(crop.getType()))));
			image = new ImageView(path);
		} else if (resources.getTools().containsKey(crop.getType())) {
			path = new Image(Main.class.getResourceAsStream((resources.getTools().get(crop.getType()))));
			image = new ImageView(path);
		} else { //Else load "No image" image
			image = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("unknown.png")));
		}
		image.setFitWidth(56); //Set heigh and width of image
		image.setFitHeight(56);
		image.setPreserveRatio(true);
		//Put a quantity number on top of image
		Text quantity= new Text(Integer.toString( crop.getQuantity())); 
		quantity.setFill(Color.RED);
		StackPane.setAlignment(quantity, Pos.BOTTOM_RIGHT); //Align to bottom left 
		item.getChildren().add(image); //Add both to stackpane
		item.getChildren().add(quantity);
		Tooltip tp = new Tooltip(crop.getType());
		Tooltip.install(item, tp); //Add tooltip name on hover
		
		//Mouse click handler
		item.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) { //On left click
					if (item.equals(selected)) { //Make clicked selected with visual change
						item.setStyle("-fx-background-color: transparent; "
								+ "-fx-border-color:grey;");
						selected=null;
						selectedItem=null;
					} else { 
						item.setStyle("-fx-background-color: #ffcfff;"
								+ " -fx-border-color:black;");
						selected=item;
						selectedItem=crop;
						updateSelected(type);
					}
					//If right click
				} else if (event.getButton() == MouseButton.SECONDARY) { 
					crops.trackAdd(crop); //Track crop
					//updateTracking();
				}
			}
		});
		
		//set drag detection
		item.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Move item to dragboard if dragged
				Dragboard drag = item.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent cc = new ClipboardContent();
				cc.putString(String.valueOf(itemIndex));
				drag.setContent(cc);
				event.consume();
				dragging=itemIndex; //Get itemIndex for item swap
			}
		});
		//set drag over check
		item.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard drag = event.getDragboard();
				boolean check = false;
				if (drag.hasString()) { //If something relevant is dragged
					String hover = drag.getString();
					try {
						int draggedAppNumber = Integer.parseInt(hover);
						//If not the same item and a pane (pain)
						if (draggedAppNumber != itemIndex && 
								event.getGestureSource() instanceof Pane) {
							check = true;
						}
					} catch (NumberFormatException exc) {
						check = false;
					}
				}
				if (check) { //Transfer
					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
		});
		//When dropped and satisfies train 
		item.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				//Swap EVERYTHING
				Pane draggedApp = (Pane) event.getGestureSource();
				int dragX = GridPane.getColumnIndex(draggedApp);
				int dragY = GridPane.getRowIndex(draggedApp);
				int dropX = GridPane.getColumnIndex(item);
				int dropY = GridPane.getRowIndex(item);
				GridPane.setColumnIndex(draggedApp, dropX);
				GridPane.setRowIndex(draggedApp, dropY);
				GridPane.setColumnIndex(item, dragX);
				GridPane.setRowIndex(item, dragY);
				crops.swapItem(dragging, itemIndex);
			}
		});
		return item;
    }
    
    /**
     * Update which item is selected
     */
    private void updateSelected(int type) {
    	if (type == 1) {
    		for (Node item:cropTab.getChildren()){
        		if (!item.equals(selected))
        			item.setStyle("-fx-background-color: transparent;");
        	}
    	} else {
    		for (Node item:toolTab.getChildren()){
        		if (!item.equals(selected))
        			item.setStyle("-fx-background-color: transparent;"); 
        	}
    	}
    	
    }
	
    /**
     * Remove quantity of selected resource on button press
     */
    @FXML
    public void removeSelected() {
    	int error = 0;
    	if (selectedItem == null) {
    		dialogBox("Select an item");
    		return;
    	}
    	if (resources.getCrops().containsKey(selectedItem.getType())) {
    		if (validate(count.getText()) && selected != null){
        		int quantity = Integer.parseInt(count.getText());
        		error = crops.removeItem(selectedItem, quantity);
        		displayCrops();
    		}
    	} else {
    		if (validate(count.getText()) && selected != null){
        		int quantity = Integer.parseInt(count.getText());
        		error = tools.removeItem(selectedItem, quantity);
        		displayCrops();
    		}
    	}
    	if (error == 1) {
			dialogBox("Not enough items");
		} else if (error == 2) {
			dialogBox("Cannot remove 0 items");
		} else if (error == 3) {
			dialogBox("Something broke"); 
		}
    	
    }
    
    /**
     * Checks if text is numeric
     * @return true id numeric, false otherwise
     */
    private boolean validate(String text) {
    	try  {  
    		int d = Integer.parseInt(text);  
        } catch (NumberFormatException nfe) {  
        	dialogBox("Enter a valid amount");
        	return false;  
        }  
        return true;  
    }
    
    
    
    public void sell() {
    	FarmClient client; 
    }
    
    
    private void dialogBox(String message) {
    	dialogMessage.setText(message);
    	FadeTransition ft = new FadeTransition(Duration.millis(2000), dialogImage);
    	ft.setFromValue(0.0);
    	ft.setToValue(1.0);
    	ft.setFromValue(1.0);
    	ft.setToValue(0.0);
    	ft.play();
    	FadeTransition ft1 = new FadeTransition(Duration.millis(2000), dialogMessage);
    	ft1.setFromValue(0.0);
    	ft1.setToValue(1.0);
    	ft1.setFromValue(1.0);
    	ft1.setToValue(0.0);
    	ft1.play();
    	
    }
    
    
   
}





