package farmsim.util.console.handler;

import farmsim.particle.ParticleController;
import farmsim.util.console.Console;

/**
 * ParticleHandler.
 */
public class Particle extends BaseHandler implements BaseHandlerInterface {

    /**
     * Particle Command Handler.
     */
    public Particle() {
        super();
        this.addCommands();
        this.setName("Particle");
    }

    /**
     * adds the commands to the handler.
     */
    private void addCommands() {
        addSingleCommand("particleDemo", "starts demo particles");
        addSingleCommand("particleStart", "enables particles (on by default)");
        addSingleCommand("particleStop", "disables particles");
        addSingleCommand("nyanDuck", "for our friend Lachlan Healey");
    }

    /**
     * Handler for commands of the weather system.
     * 
     * @param parameters the incoming command parameters.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "particleDemo":
                    particleDemo();
                    break;
                case "particleStart":
                    particleStart();
                    break;
                case "particleStop":
                    particleStop();
                    break;
                case "nyanDuck":
                    nyanDuck();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Launches the particle demo.
     */
    private void particleDemo() {
        ParticleController.getInstance().mangoes();
    }

    private void particleStart() {
        Console.getInstance().println("Enabling the Particle Engine");
        ParticleController.getInstance().startAnimation();
    }

    private void particleStop() {
        Console.getInstance().println("Disabling the Particle Engine");
        ParticleController.getInstance().stopAnimation();
    }

    private void nyanDuck() {
        ParticleController.getInstance().nyanDuck();
    }
}
