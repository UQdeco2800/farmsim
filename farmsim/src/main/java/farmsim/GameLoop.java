package farmsim;

import java.util.concurrent.atomic.AtomicBoolean;

import farmsim.util.TicksPerSecond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.predators.PredatorManager;
import farmsim.entities.tileentities.crops.Crop;
import farmsim.pollution.*;
import farmsim.events.EventManager;
import farmsim.events.RandomEventCreator;
import farmsim.world.DayNight;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * Responsible for updating the World and all entities in the game.
 * 
 * @author Leggy
 *
 */
public class GameLoop implements Runnable {

    private World world;
    private int tick;
    private AtomicBoolean quit;
    private AgentManager agentManager;
    private PredatorManager predatorManager;
    private DayNight dayNight;
    private RandomEventCreator rEC;
    private EventManager eventManager;
    private PollutionManager pollutionManager;
    private TicksPerSecond tps;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(GameLoop.class);

    private Viewport viewport;
    private MiniMap minimap;


    public GameLoop(AtomicBoolean quit, Viewport viewport, MiniMap minimap, int tick) {
        this.world = WorldManager.getInstance().getWorld();
        this.tick = tick;
        this.quit = quit;
        this.agentManager = AgentManager.getInstance();
        this.predatorManager = WorldManager.getInstance().getWorld().getPredatorManager();
        this.dayNight = WorldManager.getInstance().getWorld().getTimeManager();
        this.rEC = RandomEventCreator.getInstance();
        this.eventManager = EventManager.getInstance();
        this.pollutionManager = PollutionManager.getInstance();
        this.viewport = viewport;
        this.tps = TicksPerSecond.getInstance();
        this.minimap = minimap;
    }

    private void moveViewport() {
        GameManager gameManager = GameManager.getInstance();
        if (gameManager.moveViewportLeft()) {
            viewport.move(-1, 0);
        }

        if (gameManager.moveViewportRight()) {
            viewport.move(1, 0);
        }

        if (gameManager.moveViewportUp()) {
            viewport.move(0, -1);
        }

        if (gameManager.moveViewportDown()) {
            viewport.move(0, 1);
        }
    }

    @Override
    public void run() {
        while (!quit.get()) {
            tps.tickStart(); //start tick timer

            world.tick();
            agentManager.tick();
            try {
                predatorManager.tick();
            } catch (ViewportNotSetException e) {
                LOGGER.error("main ticker", e);
                predatorManager.setViewport(viewport);
            }
            dayNight.tick();
            rEC.tick();
            eventManager.tick();
            pollutionManager.tick();
            minimap.tick();
            moveViewport();
            // Reset treatment rounds after ticks
            Agent.setTreatmentRound(0);
            Crop.setTreatmentRound(0);
            try {
                Thread.sleep(tick);
            } catch (InterruptedException e) {
                LOGGER.error("main ticker", e);
            }

            tps.tickEnd(); //end tick timer
            tps.updateTPSCounter();
        }
    }

}
