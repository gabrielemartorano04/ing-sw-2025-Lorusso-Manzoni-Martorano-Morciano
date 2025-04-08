package it.polimi.ingsw.Model;

public class Bank {
    protected int[] goods; //red, yellow, green, blue

    public Bank() {
        this.goods = new int[4];
        goods[0] = 12; // red
        goods[1] = 17; // yellow
        goods[2] = 13; // green
        goods[3] = 14; // blue
    }

    public int[] getBank() {
        return goods;
    }

}
