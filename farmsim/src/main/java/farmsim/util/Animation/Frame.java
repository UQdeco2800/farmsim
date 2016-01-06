package farmsim.util.Animation;


import javafx.scene.image.Image;

public class Frame {

    // The image for the frame
    public Image image;

    // Duration of frame in  milliseconds
    public int duration;

    /**
     * Getter
     * @return The image associated with the frame
     */
    public Image getImage() {
        return image;
    }

    public Frame(Image image, int duration) {
        this.image = image;
        this.duration = duration;
    }

    /**
     * Setter
     * @param image Image associated with frame
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Getter
     * @return Duration for the frame
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Setter
     * @param duration Duration of the frame
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
