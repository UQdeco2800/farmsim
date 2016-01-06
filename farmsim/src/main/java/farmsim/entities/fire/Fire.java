package farmsim.entities.fire;

import javafx.scene.canvas.GraphicsContext;
import farmsim.Viewport;
import farmsim.entities.tileentities.TileEntity;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;

/**
 * Fire class. Fire burns crops in the game world.
 * 
 * @author 
 *      yojimmbo
 *
 */
public class Fire extends TileEntity {
    /**Used as a timer*/
    private int count;
    /**Is the fire burnt out*/
    private boolean out;
    /**How many ticks the fire should last*/
    private int timeToBurn;
    /**Can the fire spread to other tiles*/
    private boolean isSpreadable;
    /**Image name of current animation frame*/
    private String imageName;

    /**
     * Creates a fire at a tile.
     * 
     * @param parent 
     *      The tile to burn.
     */
    public Fire(Tile parent, int timeToBurn) {
        super("fire", parent);
        this.count = 0;
        if (timeToBurn >= 0) {
            this.out = false;
            this.isSpreadable = true;
        } else {
            this.out = true;
            this.isSpreadable = false;
        }
        this.timeToBurn = timeToBurn;
        this.imageName = "fire";
    }

    /**
     * Updates the fire.
     * 
     */
    @Override
    public void tick() {
        if (timeToBurn <= 0) {
            this.out = true;
        } else if (count >= timeToBurn) {
            getParent().setTileType(
                    TileRegister.getInstance().getTileType("dirt"));
            getParent().setTileEntity(null);
            out = true;            
        } else {
            count++;
        }
    }
    
    /**
     * Determines if the fire has burnt out.
     * 
     * @return
     *      True if the fire is out, false otherwise.
     */
    public boolean out() {
        return out;
    }
    
    /**
     * Determines if the fire can spread
     * 
     * @return
     *      True if the fire can be spread, false otherwise.
     */
    public boolean canSpread() {
        return isSpreadable;
    }
    
    /**
     * Sets the fire to not be able to spread.
     */
    public void stopSpreading() {
        isSpreadable = false;
    }
    
    /**
     * Get the maximum ticks this fire can burn for.
     * 
     * @return
     *      Ticks the fire should burn for.
     */
    public int getBurnTime() {
        return timeToBurn;
    }
    
    /**
     * Get the image name of the next frame to render.
     * 
     * @return imageName
     *      the next image name of the next image to be rendered.
     */
    public String getImageName() {
        if (count % 20 >= 0 && count % 20 < 7) {
            imageName = "fire";
        } else if (count % 20 >= 7 && count % 20 < 14) {
            imageName = "fire2";
        } else if (count % 20 >= 14) {
            imageName = "fire3";
        }
        return imageName;
    }
    
    @Override
    public void draw(Viewport v, GraphicsContext g) {
        TileRegister tileRegister = TileRegister.getInstance();
        g.drawImage(tileRegister.getTileImage(getImageName()),
                (this.getWorldX() - v.getX()) * TileRegister.TILE_SIZE,
                (this.getWorldY() - v.getY() - 0.5) * TileRegister.TILE_SIZE);
    }
    
}
