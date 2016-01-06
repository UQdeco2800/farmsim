package farmsim.util.Animation;

import javafx.scene.canvas.GraphicsContext;

public class FrameAnimation extends Animation {

    // Array of frames for animation
    private Frame[] animationFrames;


    /**
     * Simple constructor
     * @param animationFrames Array of frames for animation
     */
    public FrameAnimation(Frame[] animationFrames) {
        super(animationFrames.length);
        this.animationFrames = animationFrames;
    }

    /**
     * Constuctor that also specifies looping and speed.
     * @param animationFrames Array of frames for animation
     * @param looping boolean value indicating if animation should loop
     * @param speed speed of the animation
     */
    public FrameAnimation(Frame[] animationFrames, boolean looping,
                          float speed) {
        super(animationFrames.length, looping, speed);
        this.animationFrames = animationFrames;
    }

    @Override
    public void update() {
        // If animation not stopped update frame
        if (!animationStopped) {
            timeDifference +=
                    System.currentTimeMillis() - lastSystemMilliseconds;
            lastSystemMilliseconds = System.currentTimeMillis();
            // while the timeDifference * speed exceeds frame duration move
            // to next frame
            while (animationFrames[frame].getDuration() <=
                    timeDifference * speed) {
                timeDifference -= animationFrames[frame].getDuration() / speed;
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

    @Override
    public void renderAnimation(GraphicsContext graphicsContext, double x,
                                double y) {
        graphicsContext.drawImage(getCurrentFrame().image, x, y);
    }

    /**
     * Method to get the current frame to display.
     *
     * @return Frame the current frame.
     */
    public Frame getCurrentFrame() {
        return animationFrames[frame];
    }
}
