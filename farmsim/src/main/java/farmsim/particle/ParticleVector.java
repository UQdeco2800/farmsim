package farmsim.particle;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Particle Vector Class to simplify maths.
 */
public class ParticleVector {

    ArrayList<Double> list;

    /**
     * Empty particle vector.
     */
    public ParticleVector() {
        this(0, 0);
    }

    /**
     * Creates a Particle Vector.
     * 
     * @param vectorX first value
     * @param vectorY second value
     */
    public ParticleVector(double vectorX, double vectorY) {
        list = new ArrayList<>();
        list.add(vectorX);
        list.add(vectorY);
    }

    /**
     * Adds two vectors together.
     * 
     * @param vector the vector to add.
     */
    public void add(ParticleVector vector) {
        Double value = list.get(0) + vector.getX();
        list.set(0, value);
        value = list.get(1) + vector.getY();
        list.set(1, value);
    }

    /**
     * Overwrites current values.
     * 
     * @param vectorX first param to set.
     * @param vectorY second param to set.
     */
    public void set(double vectorX, double vectorY) {
        list.set(0, vectorX);
        list.set(1, vectorY);
    }

    /**
     * Gets the first element.
     * 
     * @return the first element.
     */
    public Double getX() {
        return list.get(0);
    }

    /**
     * Gets the second element.
     * 
     * @return the second element.
     */
    public Double getY() {
        return list.get(1);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ParticleVector) {
            ParticleVector vector = (ParticleVector) object;
            if (Objects.equals(getX(), vector.getX())
                    && Objects.equals(getY(), vector.getY())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (((13 * getX()) + getY()) * 17);
    }
}
