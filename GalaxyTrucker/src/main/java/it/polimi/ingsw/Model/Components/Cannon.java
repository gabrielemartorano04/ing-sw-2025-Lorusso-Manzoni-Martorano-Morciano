package it.polimi.ingsw.Model.Components;

/**
 * Class that manage the cannon
 */
public class Cannon extends Component{
    private final boolean isDouble;
    private boolean isActive;
    private Orientation orientation;

    /**
     * Cannon constructor, it initialised the attributes of this class
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param isDouble specify if the cannon is double
     */
    public Cannon(int linkR, int linkD, int linkL, String type, String imagePath, boolean isDouble) {
        super(0, linkR, linkD, linkL, type, imagePath);
        this.orientation = Orientation.N;
        this.isDouble = isDouble;
        this.isActive = false;
    }

    /**
     * Set the orientation if the component is rotated
     * @param orientation orientation of the cannon
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Set if the trucker decides to activate the double cannon
     * @param isActive activation of double cannon
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Return if the cannon is a double cannon or single cannon
     * @return if is a double cannon
     */
    public boolean getIsDouble() {
        return isDouble;
    }

    /**
     * Return if the double cannon is active or not
     * @return if the double cannon is active
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Return the orientation of the cannon
     * @return orientation of the cannon
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Turn the orientation of the cannon and the component
     */
    public void turn(){
        super.turn();
        if(orientation == Orientation.N){ setOrientation(Orientation.E);}
        else if(orientation == Orientation.E){setOrientation(Orientation.S);}
        else if(orientation == Orientation.S){setOrientation(Orientation.W);}
        else if(orientation == Orientation.W){setOrientation(Orientation.N);}
    }
}
