package it.polimi.ingsw.Model;

public class Bank {
    protected int[] goods; //red, yellow, green, blue

    public Bank() {
        this.goods = new int[4];
        goods[0] = 12;
        goods[1] = 17;
        goods[2] = 13;
        goods[3] = 14;
    }

    public int[] getBank() {
        return goods;
    }

}
