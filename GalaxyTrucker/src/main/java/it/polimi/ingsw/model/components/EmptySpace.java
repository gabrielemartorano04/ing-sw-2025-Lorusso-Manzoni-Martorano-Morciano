package it.polimi.ingsw.model.components;

import java.io.Serializable;

/**
 * Class that manages the space outside the spaceship's construction space
 */
final public class EmptySpace extends Component implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Empty space constructor, initialise the attributes of this class
     */
    public EmptySpace() {
        super(0, 0, 0, 0, "Empty Space", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web157.jpg");
    }
}
