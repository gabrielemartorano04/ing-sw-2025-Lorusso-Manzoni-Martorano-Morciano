package it.polimi.ingsw.Model.Components;

/**
 * Class that manages the single good
 * Value represents the color of the good:
 * 4 = red
 * 3 = yellow
 * 2 = green
 * 1 = blue
 */
public class Goods {
    private final int value;
    // x e y sono le coordinate della posizione della load che tiene la merce dentro la SpaceShip. Vengono usate solo per le merci
    // dentro la SpaceShip quindi non vengono messo nel costruttore.
    private int x, y;

    /**
     * Goods constructor, initialise the attributes of this class
     * @param value value of the good
     */
    public Goods(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the good
     * @return value of the good
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the coordinate X of the position of the load in the spaceship
     * @param x coordinate X of the position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the coordinate Y of the position of the load in the spaceship
     * @param y coordinate Y of the position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the coordinate X of the position of the load in the spaceship
     * @return coordinate X of the position
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the coordinate Y of the position of the load in the spaceship
     * @return coordinate Y of the position
     */
    public int getY() {
        return y;
    }
}