package farmsim.pollution;

import java.util.ArrayDeque;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.entities.tileentities.TileEntity;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * Spreads pollution across the map from pollution sources
 * 
 * @author bobri
 */
public class PollutionUpdater {
	private static final PollutionUpdater INSTANCE = new PollutionUpdater();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PollutionUpdater.class);

	private World world = WorldManager.getInstance().getWorld();
	private Random rand = new Random();
	private Tile t; // The current working tile
	double pollutionValue = 0; // The pollution value of the working tile
	private int width = world.getWidth();
	private int height = world.getHeight();
	// A queue to store tiles to update in
	private ArrayDeque<Tile> updateQueue = new ArrayDeque<Tile>();

	// The Max level of pollution a tile can have
	private static final double MAX_POLLUTION = 1;

	// The rate at which pollution spreads from a source
	private static final double INFECT_RATE = 0.3;

	// The rate at which tiles take pollution from those around them
	private static final double TAKE_RATE = 0.15;

	// How much faster pollution spreads through water to land
	private static final double WATER_INCREASE = 2;

	// How much pollution water should add
	private static final double WATER_CREATE = 0.015;

	// Chance of a tile updating on tick
	private static final double UPDATE_CHANCE = 0.6;

	// Value of pollution decay
	private static final double DECAY_VALUE = 0.01;

	// The number of tiles to update per tick
	private static final int UPDATE_SPEED = 50;

	/**
	 * Gets the PollutionUpdater
	 * 
	 * @return the instance of the PollutionUpdater
	 */
	public static PollutionUpdater getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets the pollution logger
	 * 
	 * @return A Logger to be used for logging pollution events
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Updates UPDATE_SPEED number of tiles each time this method is called.
	 */
	public void updateTiles() {
		if (updateQueue.isEmpty()) {
			LOGGER.warn("No tiles in updateQueue. Refetching tiles");
			fetchTiles();
			return;
		}
		if (!checkWorldUpdate()) {
			LOGGER.info("World has changed size, refetching tiles");
			emptyTileQueue();
			fetchTiles();
			return;
		}
		for (int i = 0; i < UPDATE_SPEED; i++) {
			t = updateQueue.removeFirst();
			pollutionValue = t.getPollution();
			if (t.hasProperty(TileProperty.IS_POLLUTION_SOURCE)) {
				spreadPollution(t);
			} else if (t.hasProperty(TileProperty.IS_POLLUTION_DRAIN)) {
				drainPollution(t);
			} else {
				takePollution(t);
				decayTile(t);
			}
			if (t.getPollution() > MAX_POLLUTION) {
				t.setPollution(MAX_POLLUTION);
			}
			if (t.getTileEntity() != null) {
				//Kill pollution
				t.setProperty(TileProperty.IS_POLLUTION_DRAIN, "grunty");
				drainPollution(t);
			} else {
				if (t.hasProperty(TileProperty.IS_POLLUTION_DRAIN)) {
					t.removeProperty(TileProperty.IS_POLLUTION_DRAIN);
				}
			}
			updateQueue.add(t);
		}
	}

	/**
	 * Updates the pollution of a tile by spreading pollution to tiles around it
	 * 
	 * @param t
	 *            the Tile to update the pollution of
	 */
	private void spreadPollution(Tile t) {
		int x = (int) t.getWorldX();
		int y = (int) t.getWorldY();
		int[] xNeighbours = { x + 1, x - 1, x, x };
		int[] yNeighbours = { y, y, y + 1, y - 1 };
		double newValue = 0;
		Tile nT;

		for (int i = 0; i < 4; i++) {
			if (outsideWorld(xNeighbours[i], yNeighbours[i])) {
				continue;
			}

			nT = world.getTile(xNeighbours[i], yNeighbours[i]);
			if (isWater(xNeighbours[i], yNeighbours[i])) {
				nT.setPollution(nT.getPollution()
						+ (pollutionValue * WATER_INCREASE * INFECT_RATE * getRate(world
								.getTile(x, y))));
			} else {
				newValue = nT.getPollution() + (pollutionValue * INFECT_RATE);
				if (newValue > 1) {
					spreadPollution(nT, newValue - 1);
				}
				nT.setPollution(newValue);
			}
		}
		LOGGER.info("Pollution Spread from: " + x + ", " + y);
	}

	/**
	 * Updates the pollution of a tile by spreading pollution to tiles around it
	 * 
	 * @param t
	 *            the Tile to update the pollution of
	 */
	private void spreadPollution(Tile t, double extra) {
		int x = (int) t.getWorldX();
		int y = (int) t.getWorldY();
		int[] xNeighbours = { x + 1, x - 1, x, x };
		int[] yNeighbours = { y, y, y + 1, y - 1 };
		double newValue = 0;
		Tile nT;

		for (int i = 0; i < 4; i++) {
			if (outsideWorld(xNeighbours[i], yNeighbours[i])) {
				continue;
			}

			nT = world.getTile(xNeighbours[i], yNeighbours[i]);
			if (isWater(xNeighbours[i], yNeighbours[i])) {
				nT.setPollution(nT.getPollution()
						+ (pollutionValue * WATER_INCREASE * INFECT_RATE * getRate(world
								.getTile(x, y))));
			} else {
				newValue = nT.getPollution() + (newValue / 4);
				nT.setPollution(newValue);
			}
		}
		LOGGER.info("Pollution Spread from: " + x + ", " + y);
	}

	/**
	 * Updates the pollution of a tile by taking pollution from the tiles around
	 * it.
	 * 
	 * @param t
	 *            The tile to update the pollution of
	 */
	private void takePollution(Tile t) {
		int x = (int) t.getWorldX();
		int y = (int) t.getWorldY();
		int[] xNeighbours = { x + 1, x - 1, x, x };
		int[] yNeighbours = { y, y, y + 1, y - 1 };
		Tile nT;
		double pollutionDelta;

		for (int i = 0; i < 4; i++) {
			if (outsideWorld(xNeighbours[i], yNeighbours[i])) {
				continue;
			}

			nT = world.getTile(xNeighbours[i], yNeighbours[i]);
			if ((nT.hasProperty(TileProperty.IS_POLLUTION_SOURCE) || (nT
					.getPollution() < 0.005))) {
				continue;
			}

			if ((isWater(xNeighbours[i], yNeighbours[i])) || isWater(x, y)) {
				pollutionDelta = nT.getPollution() * TAKE_RATE * WATER_INCREASE;
				t.setPollution(t.getPollution() + pollutionDelta + WATER_CREATE);
			} else {
				pollutionDelta = nT.getPollution() * TAKE_RATE;
				t.setPollution(t.getPollution() + pollutionDelta);
			}
			nT.setPollution(nT.getPollution() - pollutionDelta);
		}
	}

	/**
	 * Sets pollution of a tile based on the tiles around it.
	 * @param t The tile to adjust the pollution of
	 */
	private void drainPollution(Tile t) {
		int x = (int) t.getWorldX();
		int y = (int) t.getWorldY();
		int[] xNeighbours = { x + 1, x - 1, x, x };
		int[] yNeighbours = { y, y, y + 1, y - 1 };
		Tile nT;

		for (int i = 0; i < 4; i++) {
			if (outsideWorld(xNeighbours[i], yNeighbours[i])) {
				continue;
			}

			nT = world.getTile(xNeighbours[i], yNeighbours[i]);
			if (!nT.hasProperty(TileProperty.IS_POLLUTION_SOURCE)) {
				nT.setPollution(nT.getPollution() * 0.15);
			}
		}
	}

	/**
	 * Decays the pollution on this tile
	 * 
	 * @param x
	 *            the X location of this tile
	 * @param y
	 *            the Y location of this tile
	 */
	private void decayTile(Tile t) {
		if (rand.nextDouble() > UPDATE_CHANCE) {
			return;
		}
		t.setPollution(t.getPollution() - DECAY_VALUE);
	}

	/**
	 * Checks if a tile is outside the world
	 * 
	 * @param x
	 *            The X location of the tile
	 * @param y
	 *            The y location of the tile
	 * @return True if the tile is outside the world, false otherwise
	 */
	private boolean outsideWorld(int x, int y) {
		if ((x < 0) || (x >= world.getWidth()) || (y < 0)
				|| (y >= world.getHeight())) {
			// This tile is outside the world
			return true;
		}
		return false;
	}

	/**
	 * Checks if a tile is water
	 * 
	 * @param x
	 *            the X location of the tile
	 * @param y
	 *            the Y location of the tile
	 * @return True if the tile is water, false otherwise
	 */
	public boolean isWater(int x, int y) {
		TileEntity tE = world.getTile(x, y).getTileEntity();
		if (tE != null) {
			if (tE.getTileType() == "water") {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the rate of pollution creation from a tile
	 * 
	 * @param t
	 *            The Tile to get the rate of
	 * @return double of the rate of pollution creation
	 */
	private double getRate(Tile t) {
		if (!(t.hasProperty(TileProperty.IS_POLLUTION_SOURCE))) {
			return -1;
		}

		return (double) t.getProperty(TileProperty.IS_POLLUTION_SOURCE);
	}

	/**
	 * Checks if the world size is the same as the last update
	 * 
	 * @return True if the world has not changed size, false otherwise.
	 */
	private boolean checkWorldUpdate() {
		/*
		 * TODO: OBSERVOR PATTERN WITH WORLDSIZE
		 */
		if (world.getHeight() != height || world.getWidth() != width) {
			width = world.getWidth();
			height = world.getHeight();
			return false;
		}
		return true;
	}

	/**
	 * Fetches all tiles from the world and adds them to the updateQueue
	 */
	private void fetchTiles() {
		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				updateQueue.add(world.getTile(x, y));
			}
		}
	}

	/**
	 * Removes all tiles from the updateQueue
	 */
	private void emptyTileQueue() {
		updateQueue.clear();
	}
}
