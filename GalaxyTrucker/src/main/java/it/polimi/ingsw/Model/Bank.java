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

    public int getGoodsInBank(int valueOfGoods) {
        return goods[valueOfGoods];
    }

    public int[] getGoods(){
        return goods.clone();
    }

    public boolean isEmpty(){
        for( int i = 0; i < goods.length; i++){
            if(goods[i] != 0){
                return false;
            }
        }
        return true;
    }

    public void addGoodsInBank(int valueOfGoods) {
        goods[valueOfGoods]++;
    }
    public void removeGoodsInBank(int valueOfGoods) {
        goods[valueOfGoods]--;
    }
}
