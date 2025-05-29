package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.PlayerColors;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that manages the single cabin
 */
final public class Cabin extends Component implements Serializable {
    private final boolean isMain;
    private int quantity;
    private final int capacity;
    private Alien alien;
    private final PlayerColors color;
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Cabin constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param color color of the cabin in case it's main, null otherwise
     * @param isMain specify if the cabin is the main one
     */
    public Cabin(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, PlayerColors color, boolean isMain) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isMain = isMain;
        this.quantity = 0;
        this.capacity = 2;
        this.alien = null;
        this.color = color;
    }

    /**
     * Returns, if contained, the alien in the cabin, null otherwise
     * @return alien in the cabin
     */
    public Alien getAlien(){
        return alien;
    }

    /**
     * Sets the alien if the cabin contain one
     * @param alien type of alien
     */
    public void setAlien(Alien alien){
        this.alien = alien;
    }

    /**
     * Sets the maximum number of crew members (2 humans or 1 alien) in the cabin
     * @param quantity number of crew members
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns if the cabin is a main one
     * @return if the cabin is a main one
     */
    public boolean getIsMain() {
        return isMain;
    }

    /**
     * Returns the number of crew members contained in the cabin
     * @return number crew members contained
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the maximum number of crew members that can be contained in the cabin
     * @return number of crew members that can be contained
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the color of the main cabin, null otherwise
     * @return color of the cabin
     */
    public PlayerColors getColor() {
        return color;
    }

    /**
     * Removes crew members from the cabin
     * @param num number of crew members to remove
     */
    public void removeHumans(int num) {
        quantity -= num;
    }
}
