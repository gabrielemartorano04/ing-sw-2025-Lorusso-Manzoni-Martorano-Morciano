package it.polimi.ingsw;

public class Trucker {


    public enum Color{RED, GREEN, BLUE, YELLOW};
    private Color color;
    private SpaceShip spaceShip;
    private int position;
    private int cosmicCredit;

    public Trucker(Color color, SpaceShip spaceShip, int position) {
        this.color = color;
        this.spaceShip = spaceShip;
        this.position = position;
        this.cosmicCredit = 0;
    }
    public Trucker() {
        this.color = null;
        this.spaceShip = null;
        this.position = 0;
        this.cosmicCredit = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCosmicCredit(int cosmicCredit) {
        this.cosmicCredit = cosmicCredit;
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
