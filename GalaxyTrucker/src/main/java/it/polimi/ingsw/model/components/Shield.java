package it.polimi.ingsw.model.components;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that manages the single shield
 */
public final class Shield extends Component implements Serializable {
    /**
     * Protection direction of the shield
     */
    private Direction direction;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * It tells if the shield is active
     */
    private boolean isActive;

    /**
     * Shield constructor, initialise the attributes of this class
     * @param linkU number of link on the top
     * @param linkR number of link on the right side
     * @param linkD number of link on the bottom
     * @param linkL number of link on the left side
     * @param type type of component
     * @param imagePath front image of the component
     */
    public Shield(int linkU, int linkR, int linkD, int linkL, String type, String imagePath) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.direction = Direction.NE;
        this.isActive = false;
    }

    /**
     * Sets the direction if the shield is rotated
     * @param direction direction of the shield
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Returns the direction of the shield
     * @return direction of the shield
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * This method sets isActive shield's attribute
     * @param isActive new value for isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * This method tells if the shield is active
     * @return if the shield is active
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Turns the protection direction of the shield and links
     */
    public void turn(){
        super.turn();
        if(direction == Direction.NE){ setDirection(Direction.SE);}
        else if(direction == Direction.SE){setDirection(Direction.SW);}
        else if(direction == Direction.SW){setDirection(Direction.NW);}
        else if(direction == Direction.NW){setDirection(Direction.NE);}
    }
}
