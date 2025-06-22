package it.polimi.ingsw.model.components;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that manages the single good
 * Value represents the color of the good:
 * 4 = red
 * 3 = yellow
 * 2 = green
 * 1 = blue
 */
public class Goods implements Serializable {
    /**
     * Good's value
     */
    private final int value;

    /**
     * Load position if the good is placed in a spaceship
     */
    private int x, y;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * This method returns good's color based on value
     * @return good's color
     */
    public String getColor() {
        switch (value){
            case 1 -> {
                return "blue";
            }
            case 2 -> {
                return "green";
            }
            case 3 -> {
                return "yellow";
            }
            case 4 -> {
                return "red";
            }
        }
        return "";
    }
}