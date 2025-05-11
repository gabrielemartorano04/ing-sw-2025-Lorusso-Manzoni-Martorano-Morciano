package it.polimi.ingsw.model.components;

import java.io.Serializable;

/**
 * Class that manages the single battery storage
 */
final public class BatteryStorage extends Component implements Serializable  {
    private final int capacity;
    private int quantity;
    private static final long serialVersionUID = 1L;

    /**
     * Battery storage constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param capacity number of batteries that can storage
     */
    public BatteryStorage(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, int capacity) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.capacity = capacity;
        this.quantity = capacity;
    }

    /**
     * Sets the new number of batteries storaged
     * @param quantity number of batteries to storage
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the maximum number of batteries that can be stored
     * @return number of batteries that can be stored
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of batteries stored
     * @return number of batteries stored
     */
    public int getQuantity() {
        return quantity;
    }
}
