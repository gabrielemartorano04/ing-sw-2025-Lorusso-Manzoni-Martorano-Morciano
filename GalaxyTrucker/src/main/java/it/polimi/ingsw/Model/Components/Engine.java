package it.polimi.ingsw.Model.Components;

/**
 * Class that manages the single engine
 */
public class Engine extends Component{
    private final boolean isDouble;
    private boolean isActive;

    /**
     * Engine constructor, it initialised the attributes of this class
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
    }

    /**
     * Set if the trucker decides to activate the double engine
     * @param isActive activation of double engine
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Return if the engine is a double engine or single engine
     * @return if is double
     */
    public boolean getIsDouble() {
        return isDouble;
    }

    /**
     * Return if the engine is active or not
     * @return if the double engine is active
     */
    public boolean getIsActive() {
        return isActive;
    }
}
