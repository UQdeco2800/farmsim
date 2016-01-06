package farmsim.events;

import java.util.Random;

import farmsim.entities.tileentities.TileEntity;
import farmsim.events.statuses.StatusName;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * A crop circle event that creates a crop circle. Crop circles must be placed
 * on ploughed dirt. All crops in the crop circle will be removed and replaced
 * with crop_circle_dirt. Random SecretGold will also be placed in the crop
 * circle.
 * 
 * @author bobri
 */
public class CropCircleEvent extends AbstractEvent {
	private TileRegister tileRegister = TileRegister.getInstance();
	private World world = WorldManager.getInstance().getWorld();
	private Random rand = new Random();

	private int x, y;
	private int[][] cropCircle;
	private Tile t;
	private TileEntity tileEntity;

	/**
	 * Creates a 9x9 crop circle around the specified location
	 * 
	 * @param x
	 *            The X location of the crop circle center
	 * @param y
	 *            The Y location of the crop circle center
	 * @param circle
	 *            The circle style to be used
	 */
	public CropCircleEvent(int x, int y, CropCircleStyle circle) {
		cropCircle = CropCircleDefinitions.getCircle(circle);
		this.x = x - 4;
		this.y = y - 4;
		LOGGER.info("Created a crop circle event for location " + x + 
				", " + y + " and style " + circle);
	}

	/**
	 * Begins this crop circle event
	 */
	@Override
	public void begin() {
		if (checkValidLocation(x, y)) {

			// Create a crop circle
			for (int i = 0; i < 9; x++, i++) {
				for (int j = 0; j < 9; y++, j++) {
					if (cropCircle[i][j] == 1) {
						t = world.getTile(x, y);
						t.setTileEntity(null);
						t.setTileType(tileRegister.getTileType("crop_circle_dirt"));
						if (rand.nextInt(4) == 0) {
							t.setProperty(TileProperty.HAS_SECRET_GOLD, true);
						}
					}
				}
				y = y - 9;
			}
			LOGGER.info("Placed crop crop circle centered on " + x + ", " + y);
		}
	}

	@Override
	public boolean update() {
		return false;
	}

	@Override
	public boolean needsTick() {
		return false;
	}

	@Override
	public StatusName getName() {
		return StatusName.VOIDSTATUS;
	}

	/**
	 * Checks if the entire crop circle will fit around the given location
	 * 
	 * @param x
	 *            The X location of the crop cirles upper left
	 * @param y
	 *            The y location of the crop cires upper left
	 * @return True if crop circle will fit on world, otherwise false
	 */
	private Boolean checkValidLocation(int x, int y) {
		if ((x < 0) || (x + 9 > world.getWidth()) || (y < 0) || (y + 9 > world.getHeight())) {
			LOGGER.info("Tried to place crop circle outside the world");
			return false;
		}
		// Check that the crop circle is on ploughed dirt
		for (int i = 0; i < 9; x++) {
			for (int j = 0; j < 9; y++) {
				if (cropCircle[i][j] == 1) {
					tileEntity = world.getTile(x, y).getTileEntity();
					if (tileEntity != null){
						if (tileEntity.getTileType() == "water" || tileEntity.getTileType() 
								== "building" || tileEntity.getTileType() == "ice") {
							LOGGER.info("Tried to place crop circle on water or building");
							return false;
						}
					}
				}
				j++;
			}
			i++;
			y = y - 9;
		}
		return true;
	}
}
