package farmsim.entities;

import farmsim.Viewport;
import farmsim.render.Drawable;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.util.Tickable;
import javafx.scene.canvas.GraphicsContext;

/**
 * WorldEntity forms the basis for all entities which interact with the World.
 * 
 * @author Leggy
 *
 */
public abstract class WorldEntity implements Drawable, Tickable {
    private Point location;
    private String type;
    private boolean isSelected;

    public WorldEntity(String type, double x, double y) {
        this.location = new Point(x, y);
        this.type = type;
        this.isSelected = false;
    }

    public Point getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    @Override
    public double getWorldX() {
        return location.getX();
    }
    
    @Override
    public double getWorldY() {
        return location.getY();
    }

    public void draw(Viewport v, GraphicsContext g) {
        TileRegister tileRegister = TileRegister.getInstance();
        g.drawImage(tileRegister.getTileImage(type),
                (getLocation().getX() - v.getX()) * TileRegister.TILE_SIZE,
                (getLocation().getY() - v.getY()) * TileRegister.TILE_SIZE);
        drawSelection(v, g);
    }

    public void drawSelection(Viewport v, GraphicsContext g) {
        if (isSelected){
            g.drawImage(TileRegister.getInstance().getTileImage("selectionS"),
                    (getLocation().getX() - v.getX()) * TileRegister.TILE_SIZE,
                    (getLocation().getY() - v.getY()) * TileRegister.TILE_SIZE);
        }
    }

    /**
     *
     * @return true iff this entity is selected
     */
    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }
}
