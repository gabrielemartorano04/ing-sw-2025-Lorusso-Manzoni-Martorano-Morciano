package it.polimi.ingsw.model.components;

import java.io.Serializable;

/**
 * Class that manages the single alien
 */
public class Alien {
    private final AlienColor color;

    /**
     * Alien constructor, initialise the attributes of this class
     * @param color color of the alien
     */
    public Alien(AlienColor color){
        this.color = color;
    }

    /**
     * Returns the color of the alien
     * @return color of the alien
     */
    public AlienColor getColor() {
        return color;
    }
}
