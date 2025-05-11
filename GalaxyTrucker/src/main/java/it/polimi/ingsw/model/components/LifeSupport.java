package it.polimi.ingsw.model.components;

import java.io.Serializable;

/**
 * Class that manages the single life support
 */
final public class LifeSupport extends Component implements Serializable{
    private final AlienColor color;
    private static final long serialVersionUID = 1L;

    /**
     * Life support constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param color color of the component
     */
    public LifeSupport(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, AlienColor color) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.color = color;
    }

    /**
     * Returns the color of the life support
     * @return return the color
     */
    public AlienColor getColor() {
        return color;
    }
}
