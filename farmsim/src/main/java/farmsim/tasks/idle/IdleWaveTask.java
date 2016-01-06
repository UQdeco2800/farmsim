package farmsim.tasks.idle;

import farmsim.entities.agents.Agent;
import farmsim.tasks.AnimatedTask;
import farmsim.util.Animation.Animation;
import farmsim.util.Animation.SpriteSheet;
import farmsim.util.Animation.SpriteSheetAnimation;
import farmsim.world.World;

/**
 * Task that makes the agent wave
 */
public class IdleWaveTask extends IdleTask implements AnimatedTask {

    private SpriteSheetAnimation animation;

    /**
     * Tells the Agent to idle at a specific location. THIS TASK IS ONLY TO BE
     * INSTANSIATED BY THE AGENT MANAGER.
     *
     * @param x     The {@link Tile} x coordinate of the task.
     * @param y     The {@link Tile} y coordinate of the task.
     * @param world The {@link World} for which the task need to be completed.
     */
    public IdleWaveTask(int x, int y, World world, Agent.RoleType roleType,
                        String gender) {
        super(x, y, 4000, world);
        SpriteSheet spriteSheet = SpriteSheet.CreateSpriteSheetFromTileRegister(
                roleType.spriteSheetName() + gender + "Wave", 3, 1, 200);
        animation = new SpriteSheetAnimation(spriteSheet, true, 1);
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }
}
