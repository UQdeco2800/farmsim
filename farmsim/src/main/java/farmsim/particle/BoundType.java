package farmsim.particle;

/**
 * The different bounding options for a Emitter.
 * <p>
 * * world: when the camera moves the emitter will translate with the world.
 * * free: sits above the world in the userspace, like the raining effect.
 * </p>
 */
public enum BoundType {
    WORLD, FREE
}
