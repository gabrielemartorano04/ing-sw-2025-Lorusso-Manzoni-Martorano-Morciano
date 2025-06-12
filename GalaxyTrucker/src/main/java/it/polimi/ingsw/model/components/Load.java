package it.polimi.ingsw.model.components;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that manages the single load
 */
final public class Load extends Component implements Serializable {
    private final boolean isSpecial;
    @Serial
    private static final long serialVersionUID = 1L;

    private final int capacity;
    private final Goods[] goods; // vettore a dimensione fissa

    /**
     * Load constructor, initialise the attributes of this class
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
        this.goods = new Goods[capacity];
        Arrays.fill(this.goods, new Goods(-1)); // -1 = slot vuoto
    }

    public boolean getIsSpecial() {
        return isSpecial;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Ritorna il numero di Goods validi (cioè con value != -1)
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
     * Sostituisce la good nella posizione data con la nuova good
     * @param index posizione nel vettore
     * @param good merce da inserire
     */
    public void addGood(int index, Goods good) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException("Invalid index in addGood");
        }
        goods[index] = good;
    }

    /**
     * "Rimuove" una merce impostando il valore a -1
     * @param index posizione da svuotare
     */
    // probabile non serve mai tanto la addGood sostituisce già

    public void removeGood(int index) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException("Invalid index in removeGood");
        }
        goods[index] = new Goods(-1);
    }


    public ArrayList<Goods> getGoods() {
        return new ArrayList<>(Arrays.asList(goods));
    }

    /**
     * Adds a single good in the right position in order of decreasing value
     * @param good good to add
     */
    @Deprecated
    public void addGood(Goods good) {
//        int i = 0;
//        while (i < goods.size() && goods.get(i).getValue() > good.getValue()) {
//            i++;
//        }
//        goods.add(i, good);
    }

    @Deprecated
    public void removeGood(Goods good) {
        // never used!
    }

}


