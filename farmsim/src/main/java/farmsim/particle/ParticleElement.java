package farmsim.particle;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;

/**
 * A particle Element
 * <p>
 * A particle element that can display on the screen. If a particle element has
 * no velocity then it stays in the position including when the user pans the
 * screen but we still die when off the screen. The dead particles could also be
 * translated to the top off the screen.
 * </p>
 */
public class ParticleElement extends Region {
    // where are we, where are we going
    private ParticleVector location;
    private ParticleVector velocity;

    // our size
    double width;
    double height;

    // what to display
    private Image texture;

    // Age
    private int age = 0;

    /**
     * A particle element is a instance of a particle set.
     * 
     * @param location x,y coordinates
     * @param velocity of the particle
     * @param width of the image
     * @param height of the image
     */
    public ParticleElement(ParticleVector location, ParticleVector velocity,
            double width, double height, Image image) {
        this.location = location;
        this.velocity = velocity;
        this.width = width;
        this.height = height;
        this.texture = image;
    }

    /**
     * Move's the particle
     * <p>
     *     Gets the current position and moves according to the velocity.
     * </p>
     */
    public void move() {
        location.add(velocity);
    }

    /**
     * Displays the image to the screen at its location.
     */
    public void display() {
        relocate(location.getX(), location.getY());
    }

    /**
     * Increments the age of the particle by one.
     */
    public void age() {
        age++;
    }

    /**
     * Gets the particles location on the screen (in the parent canvas).
     * @return particleVector representing coordinates in parent.
     */
    public ParticleVector getLocation() {
        return location;
    }

    /**
     * Gets the texture which is to be displayed in the location of the
     * particle.
     * @return Image of the particle.
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * Tests if the particle is dead (out of bounds)
     * @param pointX x coordinate from which it cant extend past.
     * @param pointY y coordinate from which it cant extend past.
     * @return true if out of bounds, false otherwise.
     */
    public Boolean isDead(double pointX, double pointY) {
        // test if above the top left point
        // test if below the bottom right point
        return location.getX() < 0
                || location.getY() < 0
                || location.getX() > pointX
                || location.getY() > pointY;
    }

    /**
     * returns how long its been alive in fps ticks.
     * @param deathTime time to compare
     * @return true if lived time >= deathTime
     */
    public boolean isDead(int deathTime) {
        return age >= deathTime;
    }

    /**
     * Is the particle able to be resized.
     * @return true if resizable, false otherwise.
     */
    @Override
    public boolean isResizable() {
        return false;
    }
}
