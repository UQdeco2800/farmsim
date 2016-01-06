package farmsim.util.Animation;

import farmsim.tiles.TileRegister;
import javafx.scene.image.Image;

import java.awt.*;

public class SpriteSheet {

    // number of images in each row of sprite sheet
    private int width;

    // number of images in each column of sprite sheet
    private int height;

    // The sprite sheet image
    private Image spriteSheet;

    // The width of the individual images in the sprite sheet
    private int imageWidth;

    // The height of the individual images in the sprite sheet
    private int imageHeight;

    // The duration for the images in the sprite sheet
    private int duration;

    /**
     * Constuctor that defaults image width and image height to the TILE_SIZE
     *
     * @param spriteSheet The sprite sheet image
     * @param width       number of images in each row of sprite sheet
     * @param height      number of images in each column of sprite sheet
     * @param duration    duration of each image in animation
     */
    public SpriteSheet(Image spriteSheet, int width, int height, int duration) {
        this.spriteSheet = spriteSheet;
        imageWidth = TileRegister.TILE_SIZE;
        imageHeight = TileRegister.TILE_SIZE;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }


    /**
     * @param width       number of images in each row of sprite sheet
     * @param height      number of images in each column of sprite sheet
     * @param spriteSheet The sprite sheet image
     * @param imageWidth  The width of individual images
     * @param imageHeight The height of individual images
     * @param duration    duration of each image in animation
     */
    public SpriteSheet(int width, int height, Image spriteSheet, int imageWidth,
                       int imageHeight, int duration) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.duration = duration;
    }


    /**
     * Static constructor that returns Spritesheet with the image retrieved from TileRegister.
     *
     * @param tileName The name of tile in TileRegister
     * @param width       number of images in each row of sprite sheet
     * @param height      number of images in each column of sprite sheet
     * @param duration    duration of each image in animation
     * @return SpriteSheet object created
     */
    public static SpriteSheet CreateSpriteSheetFromTileRegister(String tileName, int width, int height, int duration) {
        TileRegister register = TileRegister.getInstance();
        return new SpriteSheet(register.getTileImage(tileName),
                width, height, duration);
    }

    /**
     * Get the coordinates for the top left corner of the current image
     * relevant to the sprite sheet image
     *
     * @param imageIndex The index of the image
     * @return Point coordinates for image
     */
    public Point getPointForIndex(int imageIndex) {
        int row = imageIndex / width;
        int column = imageIndex % width;
        return new Point(column * imageWidth, row * imageHeight);
    }

    /**
     * @param imageHeight The height of the individual images
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @param imageWidth The width of the individual images
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return number of images in each row of sprite sheet
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return number of images in each column of sprite sheet
     */
    public int getHeight() {
        return height;
    }

    /**
     * Calculates total number of images in sprite sheet. Assumes spritesheet
     * is full.
     *
     * @return number of images in sprite sheet.
     */
    public int numberOfImages() {
        return height * width;
    }

    /**
     * @return The sprites sheet image
     */
    public Image getSpriteSheet() {
        return spriteSheet;
    }

    /**
     * @return Duration specified for each frame
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return The height of the individual images
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @return The width of the individual images
     */
    public int getImageWidth() {
        return imageWidth;
    }
}
