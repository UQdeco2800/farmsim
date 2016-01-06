package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.Animal;
import farmsim.inventory.SimpleResourceHandler;

/**
 * @author gjavi1 for Team Flyod
 */
public abstract class AbstractAnimalProducts {
    protected Animal animal;
    protected Agent agent;
    protected String productName;
    protected SimpleResourceHandler product;

    public AbstractAnimalProducts(Animal animal, Agent agent) {
        this.animal = animal;
        this.agent = agent;
        this.product = SimpleResourceHandler.getInstance();
    }

    /**
     *  Method to get the name of the product.
     * @return
     *  The name of the product.
     */
    public String getProductName() {
        return productName;
    }

    public abstract void produceProduct();

}
