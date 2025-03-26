package it.polimi.ingsw.Model.Components;

/**
 * Class that manages the single engine
 */
public class Engine extends Component{
    private final boolean isDouble;
    private boolean isActive;
    private Orientation orientation;

    /**
     * Engine constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     * @param isDouble specify if the engine is double
     */
    public Engine(int linkU, int linkR, int linkL, String type, String imagePath, boolean isDouble) {
        super(linkU, linkR, 0, linkL, type, imagePath);
        this.isDouble = isDouble;
        this.isActive = false;
        this.orientation = Orientation.S;
    }

    /**
     * Sets if the trucker decides to activate the double engine
     * @param isActive activation of double engine
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns if the engine is a double engine or single engine
     * @return if is double
     */
    public boolean getIsDouble() {
        return isDouble;
    }

    /**
     * Returns if the engine is active or not
     * @return if the double engine is active
     */
    public boolean getIsActive() {
        return isActive;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void turn(){
        super.turn();
        if(orientation == Orientation.N){ setOrientation(Orientation.E);}
        else if(orientation == Orientation.E){setOrientation(Orientation.S);}
        else if(orientation == Orientation.S){setOrientation(Orientation.W);}
        else if(orientation == Orientation.W){setOrientation(Orientation.N);}
    }
}
