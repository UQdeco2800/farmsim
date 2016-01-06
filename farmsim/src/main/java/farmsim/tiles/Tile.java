package farmsim.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.Snow;
import farmsim.render.Drawable;
import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static farmsim.util.ImageHelper.*;

/**
 * Represents a square unit of the world.
 * 
 * @author Leggy, Anonymousthing
 *
 */
public class Tile extends Observable implements Drawable, Tickable {

    /**
     * The type of this tile.
     */
    private int tileType;

    /**
     * The current water level associated with the tile.
     */
    private float waterLevel;

    /**
     * The time at which the water level display was last updated
     */
    private long lastWaterUpdateTime;

    /**
     * How often the water level decreases
     */
    private static int WATER_TIME = 6000;

    /**
     * How much the water level decreases
     */
    private static float WATER_DECREASE = (float) 0.005;
    
    /**
     * Whether this tile is in a Scarecrow's safe-zone radius.
     */
    private boolean safeZone = false;

    /**
     * A key/value dictionary for custom properties. Values are stored as
     * Objects to allow for flexible types and amounts of information to be
     * stored on a Tile.
     */
    private Map<TileProperty, Object> properties;

    /**
     * The {@link TileEntity} occupying this tile, such as a crop, or rock.
     */
    private TileEntity tileEntity;

    private double pollution = 0;

    private int x;
    private int y;

    // The season active on the tile (snowing may also change this)
    String activeSeason = "";

    /**
     * Initialises this Tile with a specified tile type.
     * 
     * @param tileType The tile type to create this Tile with
     */
    public Tile(int x, int y, int tileType) {
        this.tileType = tileType;
        properties = new HashMap<>();
        waterLevel = (float) Math.random();

        this.x = x;
        this.y = y;
    }

    @Override
    public double getWorldX() {
        return x;
    }

    @Override
    public double getWorldY() {
        return y;
    }

    public double getWidth() {
        return TileRegister.TILE_SIZE;
    }

    public double getHeight() {
        return TileRegister.TILE_SIZE;
    }

    public void draw(Viewport v, GraphicsContext g) {
        World world = WorldManager.getInstance().getWorld();

        TileRegister tileRegister = TileRegister.getInstance();
        Image image = tileRegister.getTileImage(
                this.getTileType(),
                (int) this.getProperty(TileProperty.ELEVATION)
        );

        boolean n = this.y > 0;
        if (n) {
            n = ((int) world.getTile(x, y - 1).getProperty(TileProperty.ELEVATION) <
                    (int) this.getProperty(TileProperty.ELEVATION));
        }

        boolean e = this.x < world.getWidth() - 1;
        if (e) {
            try {
            e = ((int) world.getTile(x + 1, y).getProperty(TileProperty.ELEVATION) <
                    (int) this.getProperty(TileProperty.ELEVATION));
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        boolean s = this.y < world.getHeight() - 1;
        if (s) {
            s = ((int) world.getTile(x, y + 1).getProperty(TileProperty.ELEVATION) <
                    (int) this.getProperty(TileProperty.ELEVATION));
        }

        boolean w = this.x > 0;
        if (w) {
            w = ((int) world.getTile(x - 1, y).getProperty(TileProperty.ELEVATION) <
                    (int) this.getProperty(TileProperty.ELEVATION));
        }

        if (n || e || s || w) {
            image = tileRegister.getTileImage(this.getTileType(),
                    (int) this.getProperty(TileProperty.ELEVATION), n, e, s, w);
        }

        g.drawImage(
                image,
                (getWorldX() - v.getX()) * TileRegister.TILE_SIZE,
                (getWorldY() - v.getY()) * TileRegister.TILE_SIZE
        );

        if (tileEntity != null) {
            tileEntity.draw(v, g);
        }
    }

    /**
     * Sets the type of this Tile to the specified value.
     * 
     * @param tileType The type to set this Tile to
     */
    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    /**
     * Clears all properties on this tile.
     */
    public void clearProperties() {
        properties.clear();
    }

    /**
     * Sets the specified property on this tile to the specified value.
     * 
     * @param prop The property to set
     * @param value The value to set the property to
     */
    public void setProperty(TileProperty prop, Object value) {
        properties.put(prop, value);
    }

    /**
     * @param prop The property to check
     * @return Returns true if this Tile has the specified property; false if
     *         otherwise.
     */
    public Boolean hasProperty(TileProperty prop) {
        return properties.containsKey(prop);
    }

    /**
     * Retrieves the value of a property on this Tile.
     * 
     * @param prop The property to retrieve
     * @return Returns the value of the property if it exists, otherwise returns
     *         null. Note that a return value of null does not necessarily mean
     *         that the property does not exist -- it is possible that the
     *         property value was explicitly set to null.
     *
     *         Also note that as all property values are stored as Objects, the
     *         return value will require casting in order to get it to the type
     *         you want.
     */
    public Object getProperty(TileProperty prop) {
        return properties.get(prop);
    }

    /**
     * Removes a single property from this Tile.
     * 
     * @param prop The property to remove
     */
    public void removeProperty(TileProperty prop) {
        properties.remove(prop);
    }

    /**
     * Gets the type of this Tile.
     * 
     * @return The type of the Tile
     */
    public int getTileType() {
        return tileType;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public void setTileEntity(TileEntity tileEntity) {
    	this.tileEntity = tileEntity;
    }

    @Override
    public void tick() {
        if (tileType == TileRegister.getInstance().getTileType("dirt")) {
            double random = new Random().nextDouble();
            if (random < 0.000016) {
                tileType = TileRegister.getInstance().getTileType("grass");
            } else if (random < 0.000032) {
                tileType = TileRegister.getInstance().getTileType("grass2");
            } else if (random < 0.00005) {
                tileType = TileRegister.getInstance().getTileType("grass3");
            }
        }
        if (tileEntity != null) {
            tileEntity.tick();
        }

        /* if this tile is in the selection call its observer to update its
          water status bar
         */
        if ((System.currentTimeMillis() - lastWaterUpdateTime) > 2000) {
            decreaseWaterLevel();
            List<Point> selection = GameManager.getInstance().getSelection();
            if (selection != null) {
                updateSelected(selection);
            }
            lastWaterUpdateTime = System.currentTimeMillis();
        }
    }

    private void updateSelected(List<Point> selection) {
        for (Point point : selection) {
            if (WorldManager.getInstance().getWorld().getTile(point)
                    .equals(this)) {
                handleObservers();
            }
        }
    }

    /**
     * Gives the water level of the tile
     * 
     * @return the current tile water level
     */
    public float getWaterLevel() {
        return waterLevel;
    }

    /**
     * Reset the water level of the tile to 1.0 (100%)
     */
    public void increaseWaterLevel() {
        waterLevel = (float) 1.0;
    }


    /**
     * Set the water level of the tile
     */
    public void setWaterLevel(float newLevel) {
        if (newLevel < 0) {
            waterLevel = (float) 0.0;
        } else if (newLevel > 1) {
            waterLevel = (float) 1.0;
        } else {
            waterLevel = newLevel;
        }
    }


    /**
     * Decreases the water level of the tile or mains it at 0 if 0 has already
     * been reached, if the tile is/is adjacent to a water tile it will not
     * decrease.
     */
    public void decreaseWaterLevel() {
        if(!adjacentToWater()){
            if (waterLevel > WATER_DECREASE) {
        		waterLevel -= WATER_DECREASE;
            } else {
                waterLevel = (float) 0.0;
            }
        }
    }

    /**
     * Notifies the tile observer that the water level has changed and must be
     * updated
     */
    public void handleObservers() {
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the amount of pollution on this tile
     * 
     * @return The amount of pollution on this tile
     */
    public double getPollution() {
        return pollution;
    }

    /**
     * Sets the amount of pollution this tile has
     * 
     * @param pollution The new amount of pollution for this tile
     */
    public void setPollution(double pollution) {
        if (pollution > 1) {
            this.pollution = 1;
        } else if (pollution < 0) {
            this.pollution = 0;
        } else {
            this.pollution = pollution;
        }
    }
    
    /**
     * @return true if this tile is in a scarecrow's safe zone.
     */
    public boolean getSafeZone() {
        return safeZone;
    }
    
    /**
     * sets a tile to be safe (indicative of whether it is in a scarecrow's safe 
     * zone). 
     * 
     * @param safe boolean indicating whether tile is in a safe zone.
     */
    public void setSafeZone(boolean safe) {
        safeZone = safe;
    }

    /**
     * Sets the activeSeason of the tile
     */
    public void setActiveSeason(String season) {
        activeSeason = season;
    }

    public String getActiveSeason() {
        return activeSeason;
    }
    
    /**
     * Returns a Boolean based on the
     * surrounding tiles.
     * 
     * @return
     *     true if there is water in an adjacent tile.
     *     false if there are no water tiles.
     */
    public boolean adjacentToWater() {
        int xThis = (int)this.getWorldX();
        int yThis = (int)this.getWorldY();
        int xPlus = xThis;
        int yPlus = yThis;
        int xMinus = xThis;
        int yMinus = yThis;
        World world = WorldManager.getInstance().getWorld();
        if(world == null) {
            return false;
        }
        if(x > 0){
        	xMinus = x - 1;
        }
        if(xThis > 0){
        	xMinus = xThis - 1;
        }
        if (xThis < world.getWidth()-1){
        	xPlus = xThis + 1;
        }
        if (yThis > 0){
        	yMinus = yThis - 1;
        }
        if (yThis < world.getHeight()-1){
        	yPlus = yThis + 1;
        }
        
        for(;xMinus <= xPlus; xMinus++){
        	for(int yM = yMinus;yM <= yPlus; yM++){
        		Tile tile = world.getTile(xMinus,yM);
        		if(tile.getTileEntity()!= null && "water".equals(tile.getTileEntity().getTileType().toString())){
            		return true;
            	}
        	}
        }
        return false;
    }
}
