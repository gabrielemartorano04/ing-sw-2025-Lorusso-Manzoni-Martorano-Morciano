package it.polimi.ingsw.model.cards;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that manages the effects
 */
public class Effect implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final TypeOfEffect type;
    private final int value;

    /**
     * Effect constructor, initialise the attributes of this class
     * @param type type of effect
     * @param value number of element to lose
     */
    public Effect(TypeOfEffect type, int value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the type of effect of the card
     * @return type of effect
     */
    public TypeOfEffect getType() {
        return type;
    }

    /**
     * Returns the number of element to lose
     * @return element to lose
     */
    public int getValue() {
        return value;
    }
}


