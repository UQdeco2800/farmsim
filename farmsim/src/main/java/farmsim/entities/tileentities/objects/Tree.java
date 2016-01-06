package farmsim.entities.tileentities.objects;

import farmsim.tiles.Tile;

import java.util.Random;

public class Tree extends BaseObject {
    private int treePrice;
    private int treeStyle;

    /**
     * Create a new Tree
     * 
     * @param treeType the type of tree
     * @param treePrice cost to remove the tree
     */
    public Tree(String treeType, Tile parent, int treePrice) {
        super(treeType, parent, true);
        this.treePrice = treePrice;
        this.treeStyle = new Random().nextInt(2);
    }

    public int getTreePrice() {
        return treePrice;
    }

    @Override
    public String toString() {
        return getTileType()
                + " has a price "
                + Integer.toString(getTreePrice());
    }

    /**
     * Get the Water tile type (water or ice)
     *
     * @return the type currently active.
     */
    @Override
    public String getTileType() {
        if("AUTUMN".equals(getParent().getActiveSeason())) {
            if(treeStyle == 0) {
                return "treeAutumn";
            } else {
                return "treeAutumn2";
            }
        } else if ("WINTER".equals(getParent().getActiveSeason())) {
            if(treeStyle == 0) {
                return "treeWinter";
            } else {
                return "treeWinter2";
            }
        } else {
            return super.getTileType();
        }
    }
}
