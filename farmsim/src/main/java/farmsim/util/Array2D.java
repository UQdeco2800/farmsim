package farmsim.util;

import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 2D array implementation.
 * 
 * @author Anonymousthing
 *
 * @param <T>
 */
public class Array2D<T> {
    private int width;
    private int height;
    private List<T> arr;
    World currentWorld;

    public Array2D(int width, int height) {
        this.width = width;
        this.height = height;
        this.arr = new ArrayList<>(width * height);
    }

    /**
     * Initialise must be called before utilising the array for each cell that
     * will be used
     * 
     * @param val initial value of coordinate x, y
     */
    public void initialise(int x, int y, T val) {
        arr.add(y * width + x, val);
    }

    /**
     * Get the object at the given coordinates
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @return the object at that location
     */
    public T get(int x, int y) {
        if (x >= width || y >= height || x < 0 || y < 0) {
            throw new IndexOutOfBoundsException();
        }
        // This cast will always be safe as set() only takes values of type T
        return arr.get(y * width + x);
    }

    /**
     * Set coordinate x, y to val
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param val the object to be stored in this position
     */
    public void set(int x, int y, T val) {
        if (x >= width || y >= height || x < 0 || y < 0) {
            throw new IndexOutOfBoundsException();
        }
        arr.set(y * width + x, val);
    }

    /**
     * Get the width of the array
     * 
     * @return array width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the array
     * 
     * @return array height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Replace the array with a new array of size /x by /y Extends the array
     * down and to the right. Assumes the array will only ever get larger
     * 
     * Discards information outside of the array if the array shrinks
     */
    @SuppressWarnings("unchecked")
    public void setDimensions(int width, int height) {
        List<T> newArr = new ArrayList<>(width * height);
        List<T> subArr;
        int oldWidth = this.width;
        int oldHeight = this.height;
        int x;
        int y;
        this.width = width;
        this.height = height;

        for (y = 0; y < oldHeight; y++) {
            subArr = arr.subList(y * oldWidth, (y + 1) * oldWidth);
            newArr.addAll(y * width, subArr);
            for (x = oldWidth; x < width; x++) {
                // Assumes arr has at least one element
                if (arr.get(0) instanceof Tile) {
                    newArr.add(y * width + x, (T) new Tile(x, y, 0));
                } else if (arr.get(0) instanceof Integer) {
                    newArr.add(y * width + x, (T) new Integer(0));
                }
            }
        }
        
        for (y = oldHeight; y < height; y++) {
            for (x = 0; x < width; x++) {
                // Assumes arr has at least one element
                if (arr.get(0) instanceof Tile) {
                    newArr.add(y * width + x, (T) new Tile(x, y, 0));
                } else if (arr.get(0) instanceof Integer) {
                    newArr.add(y * width + x, (T) new Integer(0));
                }
            }
        }
        
        this.arr = newArr;

    }
}

