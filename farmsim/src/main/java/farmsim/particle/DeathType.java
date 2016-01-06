package farmsim.particle;

/**
 * Death types available for particles.
 * <p>
 * * bound: once it leaves the screen it dies/gets reused
 * * time: lasts an amount of ticks of the animator.
 * </p>
 */
public enum DeathType {
    BOUND, TIME
}
