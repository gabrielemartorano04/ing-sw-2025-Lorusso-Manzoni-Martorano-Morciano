package it.polimi.ingsw.Model.Components;

import java.util.ArrayList;

/**
 * Class that manages the single load
 */
public class Load extends Component{
    private final boolean isSpecial;
    private final int capacity;
    private ArrayList<Goods> goods; //lista delle merci contenute in quel Load ordinate dalla rarità maggiore a quella minore

    /**
     * Load constructor, it initialised the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param capacity number of goods that can be contained
     * @param isSpecial specify if the load is special
     */
    public Load(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, int capacity, boolean isSpecial) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isSpecial = isSpecial;
        this.capacity = capacity;
        goods = new ArrayList<Goods>();
        for(Goods g : goods){
            g=null;
        }
    }

    /**
     * Return if the load is a special one, thus if can contain red goods
     * @return if the load is special
     */
    public boolean getIsSpecial() {
        return isSpecial;
    }

    /**
     * Return the maximum number of goods that can be contained
     * @return number of goods that can be contained
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Return the number of goods contained
     * @return number of goods contained
     */
    public int getQuantity() {
        return goods.size();
    }

    /**
     * Add a single good in the right position in order of decreasing value
     * @param good good to add
     */
    public void addGood(Goods good) {
        int i = 0;
        while (i < goods.size() && goods.get(i).getValue() > good.getValue()) {
            i++;
        }
        goods.add(i, good);
    }

    /**
     * Remove a single good in order of value, from the most valuable
     */
    public void removeGoodFromHead() {
        goods.removeFirst();
    }

    /**
     * Remove a specific good
     * @param good good to remove
     */
    public void removeGood(Goods good){
        goods.remove(good);
    }

    /**
     * Return the list of goods contained in the load
     * @return list of goods
     */
    public ArrayList<Goods> getGoods() {
        return goods;
    }
}
