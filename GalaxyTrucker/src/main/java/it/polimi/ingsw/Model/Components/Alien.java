package it.polimi.ingsw.Model.Components;

/**
 * Class that manage the single alien
 */
public class Alien {
    private final AlienColor color;

    /**
     * Alien constructor, it initialised the attributes of this class
     * @param color color of the alien
     */
    public Alien(AlienColor color){
        this.color = color;
    }

    /**
     * Return the color of the alien
     * @return color of the alien
     */
    public AlienColor getColor() {
        return color;
    }
}
