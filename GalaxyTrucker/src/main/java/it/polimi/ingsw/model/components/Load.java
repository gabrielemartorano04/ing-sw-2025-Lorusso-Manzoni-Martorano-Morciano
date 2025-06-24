package it.polimi.ingsw.model.components;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that manages the single load
 */
final public class Load extends Component implements Serializable {
    /**
     * It tells if the load is for special goods
     */
    private final boolean isSpecial;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Load's capacity
     */
    private final int capacity;

    /**
     * Array for goods in the load
     */
    private final Goods[] goods;

    /**
     * Load constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param capacity number of goods that can be contained. Goods list dimension
     * @param isSpecial specify if the load is special
     */
    public Load(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, int capacity, boolean isSpecial) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isSpecial = isSpecial;
        this.capacity = capacity;
        this.goods = new Goods[capacity];
        Arrays.fill(this.goods, new Goods(-1));
    }

    /**
     * This method tells if the load is for special goods
     * @return if the load is special
     */
    public boolean getIsSpecial() {
        return isSpecial;
    }

    /**
     * This method is the load capacity getter
     * @return the load capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * This method returns number of goods in the load
     * @return number of goods in the load
     */
    public int getQuantity() {
        int count = 0;
        for (Goods g : goods) {
            if (g.getValue() != -1) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method is used to tell if the load is empty
     * @return if the load is empty
     */
    public boolean isEmpty() {
        for (Goods g : goods) {
            if (g.getValue() != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method replaces the good in the given position with a new good
     * @param index good's position in the array
     * @param good good to add in that position
     */
    public void addGood(int index, Goods good) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException("Invalid index in addGood");
        }
        goods[index] = good;
    }

    /**
     * This method removes a good. An empty position has a good with value '-1'
     * @param index position to empty
     */
    public void removeGood(int index) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException("Invalid index in removeGood");
        }
        goods[index] = new Goods(-1);
    }

    /**
     * This method gives the good in the load in the given position
     * @param index good position
     * @return good in the given position
     */
    public Goods getGood(int index) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException("Invalid index in getGood");
        }
        return goods[index];
    }

    /**
     * This method returns goods in the load array as a list
     * @return goods in the load as a list
     */
    public ArrayList<Goods> getGoods() {
        return new ArrayList<>(Arrays.asList(goods));
    }

}


