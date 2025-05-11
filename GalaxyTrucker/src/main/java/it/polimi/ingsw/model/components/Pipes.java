package it.polimi.ingsw.model.components;

import java.io.Serializable;

/**
 * Class that manages the single pipes
 */
final public class Pipes extends Component implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Pipes constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     */
    public Pipes(int linkU, int linkR, int linkD, int linkL, String type, String imagePath){
        super(linkU, linkR, linkD, linkL, type, imagePath);
    }
}
