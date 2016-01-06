package farmsim.util.Animation;

import javafx.scene.canvas.GraphicsContext;

/**
 * Base class that defines varibles and methods applicable to all types of
 * animation.
 * @author hbsteel
 */
public abstract class Animation {

    /* Basic image animation class
    *  Contains array of images and methods to start stop, reset and loop
    *  animation */

    // Index of frame
    protected int frame;

    // Speed of animation
    protected float speed;

    // Milliseconds since frame started (animation not stopped)
    protected long timeDifference;

    // Last value of system time
    protected long lastSystemMilliseconds;

    // Number of frames/images in animation
    protected int animationLength;

    // boolean value indicating if animation stopped
    protected boolean animationStopped;

    // boolean value indicating if animation looping
    protected boolean looping;

    /**
     * Constructor for Animation. Defaults speed to 1 and looping to true.
     *
     * @param animationLength The number of seperate frames/images in the
     *                        animation
     */
    public Animation(int animationLength) {
        this.animationLength = animationLength;
        animationStopped = true;
        looping = true;
        timeDifference = 0;
        speed = 1;
        frame = 0;
    }


    /**
     * Detailed constructor for Animation.
     *
     * @param animationLength An array of Frame's to be displayed in the
     *                        animation
     * @param looping         boolean value. true indicated the animation
     *                        should loop.
     * @param speed           The speed of the animation. High speeds may
     *                        cause frames to skip.
     */
    public Animation(int animationLength, boolean looping, float speed) {
        this.animationLength = animationLength;
        this.looping = looping;
        animationStopped = true;
        timeDifference = 0;
        this.speed = speed;
    }

    /**
     * Method to start the animation. Animation will start playing from the
     * current frame. If animation is being resumed it will update the
     * lastSystemMilliseconds variable
     */
    public void start() {
        if (animationStopped) {
            animationStopped = false;
            lastSystemMilliseconds = System.currentTimeMillis();
        }
    }


    /**
     * Method to stop the animation from running. This method does not reset the
     * animation and calling start() will resume the animation from the frame
     * it was stopped on. Adds the time since last update to the timeDifference.
     */
    public void stop() {
        // reset time difference and stop animation
        if (!animationStopped) {
            timeDifference +=
                    System.currentTimeMillis() - lastSystemMilliseconds;
        }
        animationStopped = true;
    }

    /**
     * Method to reset animation to its original state.Animation is stopped,
     * located at first frame and clear the timeDifference.
     */
    public void reset() {
        animationStopped = true;
        frame = 0;
        timeDifference = 0;
    }

    /**
     * Method to update the animation. If the animation isnt stopped the time
     * since the animation was last updated(whilst animation running) is
     * computed and the correct frame is saved.
     */
    public abstract void update();

    public int getFrameIndex() {
        return frame;
    }


    public int getAnimationLength() {
        return animationLength;
    }

    /**
     * Getter method for speed
     *
     * @return speed of the animation
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Setter method for speed.
     *
     * @param speed of the animation
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Getting for animationStopped.
     *
     * @return boolean value indicaiting if animation stopped.
     */
    public boolean isAnimationStopped() {
        return animationStopped;
    }

    /**
     * Getter for looping variable
     *
     * @return boolean value indicating if animation is looping.
     */
    public boolean isLooping() {
        return looping;
    }

    /**
     * Setter method for looping variable
     *
     * @param looping boolean value indicating if animation is looping.
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    /**
     * Method to render the animation.
     * @param graphicsContext The graphics object used for rendering
     * @param x The x coordinate to render the animation at.
     * @param y The y coordinate to render the animation at.
     */
    public abstract void renderAnimation(GraphicsContext graphicsContext,
                                         double x, double y);
}
