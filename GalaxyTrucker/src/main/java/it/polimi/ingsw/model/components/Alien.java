package it.polimi.ingsw.model.components;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that manages the single alien
 */
public class Alien implements Serializable {

    /**
     * Alien's color
     */
    private final AlienColor color;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

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
