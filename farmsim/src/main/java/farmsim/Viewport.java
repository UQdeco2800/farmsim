package farmsim;

import farmsim.render.Drawable;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * Represents the section of the world that is currently visible.
 *
 * @author lightandlight
 */
public class Viewport {
    private int x;
    private int y;

    private int currentWidth;
    private int currentHeight;

    private int maxWidth;
    private int maxHeight;

    private World world;

    /**
     * Creates a new viewport object.
     *
     * <p>The maximum viewport width and height are initially set to width and
     * height.</p>
     *
     * @param width The width of the viewport in pixels
     * @param height The height of the viewport in pixels
     * @param world The world that the viewport is observing
     */
    public Viewport(double width, double height, World world) {
        this.world = world;

        x = 0;
        y = 0;

        setMaxWidthPixels(width);
        setMaxHeightPixels(height);

        setWidthPixels(width);
        setHeightPixels(height);
    }

    /**
     * Returns true if item is located within the viewport's bounds.
     */
    public boolean isOnScreen(Drawable item) {
        return item.getWorldX() >= getX()
                && item.getWorldX() < getX() + getWidthTiles()
                && item.getWorldY() >= getY()
                && item.getWorldY() < getY() + getHeightTiles();
    }

    /**
     * Returns the left tile of the viewport.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns top tile of the viewport.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the top-left point of the viewport.
     */
    public Point getPosition() {
        return new Point(x, y);
    }

    /**
     * Sets the position of the viewport.
     * 
     * @param newX The new left tile of the viewport
     * @param newY The new top tile of the viewport
     */
    public void setPosition(int newX, int newY) {
        int maxX = world.getWidth() - getWidthTiles();
        if (newX >= 0 && newX <= maxX) {
            x = newX;
        } else if (newX < 0) {
            x = 0;
        } else if (newX > maxX) {
            x = maxX;
        }

        int maxY = world.getHeight() - getHeightTiles();
        if (newY >= 0 && newY <= maxY) {
            y = newY;
        } else if (newY < 0) {
            y = 0;
        } else if (newY > maxY) {
            y = maxY;
        }
    }

    /**
     * Moves the viewport offsetX tiles to the right and offsetY tiles down.
     */
    public void move(int offsetX, int offsetY) {
        setPosition(getX() + offsetX, getY() + offsetY);
    }

    /**
     * Returns the width of the viewport in tiles.
     */
    public int getWidthTiles() {
        return currentWidth;
    }

    /**
     * Returns the maximum width of the viewport in tiles.
     */
    public int getMaxWidthTiles() {
        return maxWidth;
    }

    /**
     * Returns the width of the viewport in pixels.
     */
    public int getWidthPixels() {
        return getWidthTiles() * TileRegister.TILE_SIZE;
    }

    /**
     * Returns the height of the viewport in tiles.
     */
    public int getHeightTiles() {
        return currentHeight;
    }

    /**
     * Returns the maximum height of the viewport in tiles.
     */
    public int getMaxHeightTiles() {
        return maxHeight;
    }

    /**
     * Returns the height of the viewport in pixels.
     */
    public int getHeightPixels() {
        return getHeightTiles() * TileRegister.TILE_SIZE;
    }

    /**
     * Sets the width of the viewport in tiles.
     *
     * <p>If newWidth is less than the max viewport width, the viewport
     * width is set to newWidth. Otherwise, it is set to the max viewport
     * width.</p>
     *
     * @param newWidth The new viewport width
     */
    public void setWidthTiles(int newWidth) {
        currentWidth = newWidth < maxWidth ? newWidth : maxWidth;
    }

    /**
     * Sets the maximum width of the viewport in tiles.
     *
     * <p>If newMaxWidth is less than the world's width, the maximum viewport
     * width is set to the world's width. Otherwise, it is set to
     * newMaxWidth</p>
     *
     * @param newMaxWidth The new max viewport width
     */
    public void setMaxWidthTiles(int newMaxWidth) {
        int worldWidth = world.getWidth();
        maxWidth = newMaxWidth < worldWidth ? newMaxWidth : worldWidth;
    }

    /**
     * Sets the height of the viewport in tiles.
     *
     * <p>If newHeight is less than the max viewport height, the viewport
     * height is set to newHeight. Otherwise, it is set to the max viewport
     * height.</p>
     *
     * @param newHeight The new viewport height
     */
    public void setHeightTiles(int newHeight) {
        currentHeight = newHeight < maxHeight ? newHeight : maxHeight;
    }

    /**
     * Sets the maximum height of the viewport in tiles.
     *
     * <p>If newMaxHeight is less than the world's height, the maximum viewport
     * height is set to the world's height. Otherwise, it is set to
     * newMaxHeight.</p>
     *
     * @param newMaxHeight The new max viewport height
     */
    public void setMaxHeightTiles(int newMaxHeight) {
        int worldHeight = world.getHeight();
        maxHeight = newMaxHeight < worldHeight ? newMaxHeight : worldHeight;
    }

    /**
     * Sets the width and height of the viewport in tiles.
     *
     * {@see setWidthTiles}
     * {@see setHeightTiles}
     */
    public void setSizeTiles(int width, int height) {
        setWidthTiles(width);
        setHeightTiles(height);
    }

    /**
     * Sets the width of the viewport to newWidth / TILE_SIZE tiles.
     * @param newWidth The new viewport width
     */
    public void setWidthPixels(double newWidth) {
        setWidthTiles((int) Math.ceil(newWidth / TileRegister.TILE_SIZE));
    }

    /**
     * Sets the height of the viewport to newHeight / TILE_SIZE tiles.
     * @param newHeight The new viewport height
     */
    public void setHeightPixels(double newHeight) {
        setHeightTiles((int) Math.ceil(newHeight / TileRegister.TILE_SIZE));
    }

    /**
     * Sets the max width of the viewport to newMaxWidth / TILE_SIZE tiles.
     * @param newMaxWidth The new viewport width
     */
    public void setMaxWidthPixels(double newMaxWidth) {
        setMaxWidthTiles((int) Math.ceil(newMaxWidth / TileRegister.TILE_SIZE));
    }

    /**
     * Sets the max height of the viewport to newMaxHeight / TILE_SIZE tiles.
     * @param newMaxHeight The new viewport height
     */
    public void setMaxHeightPixels(double newMaxHeight) {
        setMaxHeightTiles(
                (int) Math.ceil(newMaxHeight / TileRegister.TILE_SIZE)
        );
    }
}
