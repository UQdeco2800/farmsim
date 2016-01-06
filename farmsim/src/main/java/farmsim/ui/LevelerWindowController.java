package farmsim.ui;

import farmsim.GameManager;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.crops.Crop;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.util.Passable;
import farmsim.util.Point;
import farmsim.world.WorldManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The GUI for viewing level information
 * @author jack775544 (Team Hartmanis)
 */
public class LevelerWindowController implements Initializable{

	//private static LevelerWindowController INSTANCE;
	private static final double BARWIDTH = 125;
	private boolean maximised  = true;
	private LevelerWindow view;

	@FXML
	private Label xpLabel;
	@FXML
	private Label growthLabel;
	@FXML
	private Label outputLabel;
	@FXML
	private ImageView tileImage;
	@FXML
	private Label nameLabel;
	@FXML
	private Label levelLabel;
	@FXML
	private Pane levelBar;
	@FXML
	private Pane levelPane;
	@FXML
	private Label growthTitle;
	@FXML
	private Label outputTitle;
	@FXML
	private Label pestTitle;
	@FXML
	private Label weatherTitle;
	@FXML
	private Pane pestTile;
	@FXML
	private Pane weatherTile;
	@FXML
	private ImageView backgroundImage;

	public LevelerWindowController(LevelerWindow lw){
		view = lw;
	}

	public void initMouseClick(){
		levelPane.setOnMouseClicked(new LevelerClickedHandler(this));
	}

	/**
	 * Sets the name label to that of a certain tile type
	 * @param t The tile
	 */
	public void setNameLabel(TileEntity t){
		nameLabel.setText(prettyPrint(t.getTileType()));
	}

	/**
	 * Sets the name label to the given string
	 * @param s The String to set the label to
	 */
	public void setNameLabel(String s){
		nameLabel.setText(prettyPrint(s));
	}

	/**
	 * Sets the growth label to the given string
	 * @param s The String to set the label to
	 */
	public void setGrowthLabel(String s){
		growthLabel.setText(s);
	}

	/**
	 * Sets the xp label to the given string
	 * @param i The String to set the label to
	 */
	public void setXpLabel(int i){
		double width = BARWIDTH * (i / 100.0);
		levelBar.setMinWidth(width);
		levelBar.setMaxWidth(width);
		levelBar.setPrefWidth(width);
	}

	/**
	 * Sets the output label to the given string
	 * @param s The String to set the label to
	 */
	public void setOutputLabel(String s){
		outputLabel.setText(s);
	}

	public void setLevelLabel(String s){
		levelLabel.setText(s);
	}

	/**
	 * Sets the tile image to be the image for the given tile number
	 * @param i The number of the tile that the image is needed to be set to
	 */
	public void setTileImage(int i){
		Image img = TileRegister.getInstance().getTileImage(i);
		tileImage.setImage(img);
	}

	/**
	 * Sets the image to the specified image
	 * @param i The image to set the tile image to
	 */
	public void setTileImage(Image i){
		tileImage.setImage(i);
	}

	/**
	 * Updates the leveler windows display based on the current selected square
	 */
	public void updateSelection(){
		List<Point> selection = GameManager.getInstance().getSelection();

		if (selection.size() != 1){
			Image img = new Image("/transparent.png");
			this.setTileImage(img);
			this.setNameLabel("Multiple");
			this.setXpLabel(0);
			this.setGrowthLabel("");
			this.setOutputLabel("");
			this.setLevelLabel("-");
			return;
		}
		WorldManager worldManager = WorldManager.getInstance();
		for (Point p : selection){ //The list is of size one
			Tile tile = worldManager.getWorld().getTile(p);
			TileEntity entity = tile.getTileEntity();
			if (entity == null){
				Image img = TileRegister.getInstance().
						getTileImage(tile.getTileType());
				this.setTileImage(img);
				this.setNameLabel(TileRegister.getInstance().
						getTileName(tile.getTileType()));
				this.setXpLabel(0);
				this.setGrowthLabel("");
				this.setOutputLabel("");
				this.setLevelLabel("-");
			} else {
				String entityName = tile.getTileEntity().getTileType();
				if (tile.getTileEntity() instanceof Crop){
					entityName = ((Crop) tile.getTileEntity()).getName();
				}
				TileRegister tr = TileRegister.getInstance();
				Image img = tr.getTileImage(tile.getTileEntity().getTileType());
				this.setTileImage(img);
				this.setNameLabel(entityName);
				this.setXpLabel(WorldManager.getInstance().getWorld()
						.getLeveler().getExperiencePercentage(entityName));
				this.setGrowthLabel("");
				this.setOutputLabel("");
				//int i = Leveler.getInstance().getLevel(entityName);
				int i = WorldManager.getInstance().getWorld()
						.getLeveler().getLevel(entityName);
				if (i>0){
					this.setLevelLabel(i+"");
				} else {
					this.setLevelLabel("-");
				}
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/**
	 * Pretty prints a string for display. This replaces underscores with
	 * spaces, capitalises words and removes trailing numbers
	 * @param entry The string to be pretty printed
	 * @return The original string after it has been pretty printed
	 */
	private String prettyPrint(String entry) {
		//Capitalise the first letter
		String s = Character.toUpperCase(entry.charAt(0)) + entry.substring(1);
		//Remove underscores eg. ploughed_dirt -> ploughed dirt
		s = s.replace('_',' ');
		//Remove trailing ints grass3 -> grass
		if (isNumeric(s.substring(s.length() - 1))) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	/**
	 * Check to see if a string is a number
	 * @param s the string to check
	 * @return True iff the string is a number
	 */
	public static boolean isNumeric(String s){
		for (char c : s.toCharArray()){
			if (!Character.isDigit(c)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Pretty much does what it says on the tin.
	 */
	@FXML
	public void invertMinification(){
		Region[] e = {
				growthLabel,
				growthTitle,
				outputLabel,
				outputTitle,
				pestTile,
				pestTitle,
				weatherTile,
				weatherTitle
		};
		for (Region r : e) {
			r.setVisible(!r.isVisible());
		}
		//view.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		if (maximised) {
			backgroundImage.setImage(new Image("/leveler/modernTop4.png"));
			backgroundImage.setFitHeight(50);
			backgroundImage.setFitWidth(215);
			levelPane.setMinHeight(50);
			levelPane.setMaxHeight(50);
		} else {
			backgroundImage.setImage(new Image("/leveler/modern4.png"));
			backgroundImage.setFitHeight(139);
			backgroundImage.setFitWidth(215);
			levelPane.setMinHeight(139);
			levelPane.setMaxHeight(139);
		}
		maximised = !maximised;
	}

	private class LevelerClickedHandler implements EventHandler<MouseEvent> {
		private LevelerWindowController controller;
		public LevelerClickedHandler(LevelerWindowController c){
			controller = c;
		}
		public void handle(MouseEvent event){
			controller.invertMinification();
		}
	}
}