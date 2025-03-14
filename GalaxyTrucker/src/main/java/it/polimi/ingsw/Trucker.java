package it.polimi.ingsw;

public class Trucker {


    private Color color;
    private final SpaceShip spaceShip;
    private int position;
    private int cosmicCredit;

    public Trucker() {
        this.color = null;
        this.spaceShip = new SpaceShip();
        this.cosmicCredit = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addCosmicCredit(int cosmicCredit) {
        this.cosmicCredit += cosmicCredit;
    }

    public Color getColor() {
        return color;
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
