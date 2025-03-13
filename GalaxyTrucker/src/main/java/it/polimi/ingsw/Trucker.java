package it.polimi.ingsw;

public class Trucker {
    private enum Color{red, green, blue, yellow};
    private Color color;
    private SpaceShip spaceShip;
    private int position;
    private int cosmicCredit;

    public Trucker() {

    }

    public int getPosition() {
        return position;
    }
}
