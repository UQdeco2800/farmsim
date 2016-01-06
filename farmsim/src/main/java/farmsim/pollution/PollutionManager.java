package farmsim.pollution;

import java.util.List;

import org.slf4j.Logger;

import farmsim.entities.animals.FarmAnimal;
import farmsim.entities.animals.FarmAnimalManager;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * Handles the setting and management of pollution, as well as controls the
 * spreading of pollution
 * 
 * @author bobri
 */
public class PollutionManager implements Tickable {
	private static final PollutionManager INSTANCE = new PollutionManager();

	private PollutionUpdater pollutionUpdater = PollutionUpdater.getInstance();

	private Logger LOGGER = PollutionUpdater.getLogger();

	private World world = WorldManager.getInstance().getWorld();
	
	private int animalPollute = 0;

	/**
	 * Gets the PollutionManager
	 * 
	 * @return The instance of PollutionManager
	 */
	public static PollutionManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Places a new pollution source
	 * 
	 * @param x
	 *            The X location of the source
	 * @param y
	 *            The Y location of the source
	 */
	public void placePollutionSource(int x, int y, double rate) {
		Tile t = world.getTile(x, y);
		placePollutionSource(t, rate);
	}

	/**
	 * Places a new pollution source
	 * 
	 * @param p
	 *            The point at which to place a new pollution source
	 */
	public void placePollutionSource(Point p, double rate) {
		Tile t = world.getTile(p);
		placePollutionSource(t, rate);
	}

	/**
	 * Places a pollution source on the specified tile
	 * 
	 * @param t
	 *            The tile to create a pollution source on
	 * @param rate
	 *            The rate of pollution production from this source
	 */
	public void placePollutionSource(Tile tile, double rate) {
		if (rate > 1) {
			rate = 1;
		}
		if (rate < 0) {
			rate = 0;
		}
		tile.setProperty(TileProperty.IS_POLLUTION_SOURCE, rate);
		LOGGER.info("Pollution source placed at " + tile.getWorldX() + ", "
				+ tile.getWorldY() + ". Pollution production rate " + rate);
		tile.setPollution(1);

		if (tile.hasProperty(TileProperty.IS_POLLUTION_DRAIN)) {
			tile.removeProperty(TileProperty.IS_POLLUTION_DRAIN);
		}
	}

	/**
	 * Removes the pollution source property from a tile
	 * 
	 * @param x
	 *            The X location of the tile
	 * @param y
	 *            The Y location of the tile
	 */
	public void removePollutionSource(int x, int y) {
		Tile t = world.getTile(x, y);
		removePollutionSource(t);
	}

	/**
	 * Removes the pollution source property from a tile
	 * 
	 * @param t
	 *            The tile to remove the pollution source property from
	 */
	public void removePollutionSource(Tile t) {
		if (t.hasProperty(TileProperty.IS_POLLUTION_SOURCE)) {
			t.removeProperty(TileProperty.IS_POLLUTION_SOURCE);
		}
	}

	/**
	 * Places a new pollution drain
	 * 
	 * @param x
	 *            The X location of the drain
	 * @param y
	 *            The Y location of the drain
	 */
	public void placePollutionDrain(int x, int y) {
		Tile t = world.getTile(x, y);
		placePollutionDrain(t);
	}

	/**
	 * Places a new pollution drain
	 * 
	 * @param p
	 *            The point at which to place a new pollution drain
	 */
	public void placePollutionDrain(Point p) {
		Tile t = world.getTile(p);
		placePollutionDrain(t);
	}

	/**
	 * Places a pollution drain on the specified tile
	 * 
	 * @param t
	 *            The tile to create a pollution drain on
	 * @param rate
	 *            The rate of pollution production from this drain
	 */
	public void placePollutionDrain(Tile tile) {
		tile.setProperty(TileProperty.IS_POLLUTION_DRAIN, null);
		LOGGER.info("Pollution drain placed at " + tile.getWorldX() + ", "
				+ tile.getWorldY());
		tile.setPollution(0);

		if (tile.hasProperty(TileProperty.IS_POLLUTION_SOURCE)) {
			tile.removeProperty(TileProperty.IS_POLLUTION_SOURCE);
		}
	}

	/**
	 * Removes the pollution drain property from a tile
	 * 
	 * @param x
	 *            The X location of the tile
	 * @param y
	 *            The Y location of the tile
	 */
	public void removePollutionDrain(int x, int y) {
		Tile t = world.getTile(x, y);
		removePollutionDrain(t);
	}

	/**
	 * Removes the pollution drain property from a tile
	 * 
	 * @param t
	 *            The tile to remove the pollution drain property from
	 */
	public void removePollutionDrain(Tile t) {
		if (t.hasProperty(TileProperty.IS_POLLUTION_DRAIN)) {
			t.removeProperty(TileProperty.IS_POLLUTION_DRAIN);
		}
	}

	/**
	 * Clears pollution from the specified tile
	 * 
	 * @param x
	 *            The X location of the tile
	 * @param y
	 *            The Y location of the tile
	 */
	public void removePollution(int x, int y) {
		Tile t = world.getTile(x, y);
		removePollution(t);
	}

	/**
	 * Clears pollution from the specified tile
	 * 
	 * @param t
	 *            The tile to remove pollution from
	 */
	public void removePollution(Tile t) {
		t.setPollution(0);
	}

	/**
	 * Changes the pollution production rate of a source tile
	 * 
	 * @param x
	 *            the x location of this tile
	 * @param y
	 *            the y location of this tile
	 * @param rate
	 *            the new pollution production rate for this source tile
	 */
	public void changeSourceRate(int x, int y, double rate) {
		Tile t = world.getTile(x, y);
		changeSourceRate(t, rate);
	}

	/**
	 * Changes the pollution production rate of a source tile
	 * 
	 * @param t
	 *            the source tile to change the pollution production rate of
	 * @param rate
	 *            the new pollution production rate for this source tile
	 */
	public void changeSourceRate(Tile t, double rate) {
		if (t.hasProperty(TileProperty.IS_POLLUTION_SOURCE)) {
			t.setProperty(TileProperty.IS_POLLUTION_SOURCE, rate);
		}
	}

	/**
	 * Updates the pollution of every tile on the map
	 */
	@Override
	public void tick() {
		pollutionUpdater.updateTiles();
		polluteAnimals();
	}

	/**
	 * Makes animals emit pollution to the ground below them
	 */
	private void polluteAnimals() {
		List<FarmAnimal> animalList = FarmAnimalManager.getInstance().getFarmAnimals();
        try {
            Tile t = world.getTile(animalList.get(animalPollute).getLocation());
            t.setPollution(world.getTile(animalList.get(animalPollute)
                    .getLocation()).getPollution() + 0.05);

            if ((t.getPollution() > 0.6)  && (!t.hasProperty(TileProperty.IS_POLLUTION_SOURCE))) {
                placePollutionSource(t, t.getPollution());
            }
        } catch (Exception e) {}

		if (animalList.size() > 0) {
            animalPollute = (animalPollute + 1) % animalList.size();
        }
	}
}
