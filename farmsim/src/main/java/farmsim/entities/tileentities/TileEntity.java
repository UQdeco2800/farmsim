package farmsim.entities.tileentities;

import farmsim.Viewport;
import farmsim.render.Drawable;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Tickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Observable;

public abstract class TileEntity extends Observable
        implements Drawable, Tickable {

    protected String type;
    private Tile parent;

    public TileEntity(String type, Tile parent) {
        this.type = type;
        this.parent = parent;
    }

    public double getWorldX() {
        return parent.getWorldX();
    }

    public double getWorldY() {
        return parent.getWorldY();
    }

    public double getWidth() {
        return TileRegister.getInstance()
                .getTileImage(getTileType())
                .getWidth();
    }

    public double getHeight() {
        return TileRegister.getInstance()
                .getTileImage(getTileType())
                .getHeight();
    }

    public void draw(Viewport v, GraphicsContext g) {
        Image entityImage = TileRegister.getInstance()
                .getTileImage(getTileType());

        g.drawImage(
                entityImage,
                (getWorldX() - v.getX()) * TileRegister.TILE_SIZE,
                (getWorldY() - v.getY()) * TileRegister.TILE_SIZE
                        - entityImage.getHeight() + TileRegister.TILE_SIZE
        );
    }

    public String getTileType() {
        return type;
    }

    public Tile getParent() {
        return parent;
    }
}
