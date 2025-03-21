package it.polimi.ingsw.Model.Components;

/**
 * Class that manage the single battery storage
 */
public class BatteryStorage extends Component{
    private final int capacity;
    private int quantity;

    /**
     * Battery storage constructor, it initialised the attributes of this class
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
     * Set the new number of batteries storaged
     * @param quantity number of batteries to storage
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Return the maximum number of batteries that can be storaged
     * @return number of batteries that can be storaged
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Return the number of batteries storaged
     * @return number of batteries storaged
     */
    public int getQuantity() {
        return quantity;
    }
}
