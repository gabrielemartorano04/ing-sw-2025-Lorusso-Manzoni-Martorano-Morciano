package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Components.GoodsColor;

public class Trucker {


    private GoodsColor goodsColor;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;

    public Trucker() {
        this.goodsColor = null;
        this.spaceShip = new SpaceShip();
        this.cosmicCredit = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addCosmicCredit(int cosmicCredit) {
        this.cosmicCredit += cosmicCredit;
    }

    public GoodsColor getColor() {
        return goodsColor;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public int getPosition() {
        return position;
    }

    public int getCosmicCredit() {
        return cosmicCredit;
    }
}
