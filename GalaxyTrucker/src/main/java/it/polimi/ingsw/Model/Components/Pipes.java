package it.polimi.ingsw.Model.Components;

/**
 * Class that manage the single pipes
 */
public class Pipes extends Component{

    /**
     * Pipes constructor, it initialised the attributes of this class
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
