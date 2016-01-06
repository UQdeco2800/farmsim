package farmsim.world.generators;

import farmsim.world.World;

/**
 * This interface outlines the requirements for a WorldGenerator subclass.
 * WorldGenerators are builder patterns, with configureWorld() being called to
 * initialise a World creation and build() returning the World itself.
 * Implementations of this interface can add more methods in between for
 * specific methods that they want.
 * 
 * @author Anonymousthing
 *
 */
public interface WorldGenerator {
    // TODO: Narrow these exceptions down (i.e. specific exceptions), and
    // document the cases in which they're thrown.

    void configureWorld(World world) throws Exception;

    void configureWorld(String name, int width, int height, long seed)
            throws Exception;

    World build() throws Exception;
}
