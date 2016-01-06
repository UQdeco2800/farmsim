package farmsim.util.Animation;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class SpriteSheetAnimation extends Animation {

    // The startingIndex of the images in the spritesheet
    private int startingIndex;

    // Sprite sheet used for animation
    private SpriteSheet spriteSheet;


    /**
     * Simple constructor. Sets starting index to 0
     *
     * @param spriteSheet The sprite sheet used for animation
     */
    public SpriteSheetAnimation(SpriteSheet spriteSheet) {
        super(spriteSheet.numberOfImages());
        this.spriteSheet = spriteSheet;
        this.startingIndex = 0;
    }

    /**
     * Constructor that takes startingIndex and length as parameters.
     *
     * @param spriteSheet   The sprite sheet used for animation
     * @param startingIndex Index of the first image to be used in animation
     * @param length        Number of images to be used in the animation
     */
    public SpriteSheetAnimation(SpriteSheet spriteSheet, int startingIndex,
                                int length) {
        super(length);
        this.spriteSheet = spriteSheet;
        this.startingIndex = startingIndex;
    }

    /**
     * Constuctor that takes in looping and speed parameters
     *
     * @param spriteSheet The sprite sheet used for animation
     * @param looping     boolean to indicate whether the animation should loop
     * @param speed       speed of animation
     */
    public SpriteSheetAnimation(SpriteSheet spriteSheet, boolean looping,
                                float speed) {
        super(spriteSheet.numberOfImages(), looping, speed);
        this.spriteSheet = spriteSheet;
        this.startingIndex = 0;
    }


    /**
     * Constructor that takes in looping, speed, starting index and length
     *
     * @param spriteSheet   The sprite sheet used for animation
     * @param looping       boolean to indicate whether the animation should
     *                      loop
     * @param speed         speed of animation
     * @param startingIndex Index of the first image to be used in animation
     * @param length        Number of images to be used in the animation
     */
    public SpriteSheetAnimation(SpriteSheet spriteSheet, boolean looping,
                                float speed, int startingIndex, int length) {
        super(length, looping, speed);
        this.spriteSheet = spriteSheet;
        this.startingIndex = startingIndex;
    }

    /**
     * Calculates the frame of the spritesheet that is currently being displayed
     *
     * @return integer value indicating the index of the current frame in the
     * sprite sheet
     */
    public int getFrameIndex() {
        return frame + startingIndex;
    }

    @Override
    public void renderAnimation(GraphicsContext graphicsContext, double x,
                                double y) {
        SpriteSheet spritesheet = getSpriteSheet();
        java.awt.Point spriteSheetPoint = getCurrentImagePoint();
        graphicsContext
                .drawImage(spritesheet.getSpriteSheet(), spriteSheetPoint.x,
                        spriteSheetPoint.y,
                        spritesheet.getImageWidth(),
                        spritesheet.getImageHeight(), x, y,
                        spritesheet.getImageWidth(),
                        spritesheet.getImageHeight());
    }

    /**
     * Method for updating the animation. Calculates time difference and
     * updates the frame index to the correct position
     */
    @Override
    public void update() {
        // If animation not stopped update frame
        if (!animationStopped) {
            timeDifference +=
                    System.currentTimeMillis() - lastSystemMilliseconds;
            lastSystemMilliseconds = System.currentTimeMillis();
            // while the timeDifference * speed exceeds frame duration  move
            // to next frame
            while (spriteSheet.getDuration() <=
                    timeDifference * speed) {
                timeDifference -= spriteSheet.getDuration() / speed;
                frame++;
                if (frame == getAnimationLength()) {
                    frame = 0;
                    if (!looping) {
                        reset();
                    }
                }
            }
        }
    }

    /**
     * Method that returns the coordinates of the top left corner of the
     * image to be displayed in the sprite sheet
     *
     * @return Point coordinates to top left corner of image
     */
    public Point getCurrentImagePoint() {
        return spriteSheet.getPointForIndex(frame + startingIndex);
    }

    /**
     * Getter method
     *
     * @return The sprite sheet for this animation
     */
    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    /**
     * Setter method.
     *
     * @param spriteSheet The sprite sheet for this animation
     */
    public void setSpriteSheet(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
}
