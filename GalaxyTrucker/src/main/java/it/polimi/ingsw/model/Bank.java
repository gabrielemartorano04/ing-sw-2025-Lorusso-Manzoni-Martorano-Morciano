package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 */

public class Bank implements Serializable {
    protected int[] goods; //red, yellow, green, blue
    @Serial
    private static final long serialVersionUID = 1L;

    public Bank() {
        this.goods = new int[4];
        goods[0] = 12; // red
        goods[1] = 17; // yellow
        goods[2] = 13; // green
        goods[3] = 14; // blue
    }

    // va messo il meno 1 nell'indice perche come indice passiamo il value della merce che va da 1 a 4
    // mentre gli indici dentro l'array vanno da 0 a 3
    public int getGoodsInBank(int valueOfGoods) {
        return goods[valueOfGoods - 1];
    }

    public int[] getGoods(){
        return goods.clone();
    }

    public void addGoodsInBank(int valueOfGoods) {
        goods[valueOfGoods-1]++;
    }
    public void removeGoodsInBank(int valueOfGoods) {
        goods[valueOfGoods-1]--;
    }
}
