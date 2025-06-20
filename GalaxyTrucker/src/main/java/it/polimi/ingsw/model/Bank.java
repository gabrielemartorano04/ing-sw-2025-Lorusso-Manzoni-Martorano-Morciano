package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 *  Class that manages goods bank.
 */

public class Bank implements Serializable {
    /**
     * A counter for each type of available good.
     * goods[0]: number of red goods
     * goods[1]: number of yellow goods
     * goods[2]: number of green goods
     * goods[3]: number of blue goods
     */
    protected int[] goods;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor, it initializes the number of available goods for each type.
     */
    public Bank() {
        this.goods = new int[4];
        goods[0] = 12; // red
        goods[1] = 17; // yellow
        goods[2] = 13; // green
        goods[3] = 14; // blue
    }

    /**
     * This method is useful to see how many goods are available based of the value
     * @param valueOfGoods value of the goods
     * @return number of goods for that value
     */
    public int getGoodsInBank(int valueOfGoods) {
        return goods[valueOfGoods - 1];
    }

    /**
     * @return the number of available goods for each type
     */
    public int[] getGoods(){
        return goods.clone();
    }

    /**
     * This method adds a good in the bank
     * @param valueOfGoods value of the good
     */
    public void addGoodsInBank(int valueOfGoods) {
        goods[valueOfGoods-1]++;
    }

    /**
     * This method removes a good from the bank
     * @param valueOfGoods value of the good
     */
    public void removeGoodsInBank(int valueOfGoods) {
        goods[valueOfGoods-1]--;
    }
}
