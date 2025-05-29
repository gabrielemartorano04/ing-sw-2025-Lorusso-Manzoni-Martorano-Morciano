package it.polimi.ingsw.model.components;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that manages the single shield
 */
public final class Shield extends Component implements Serializable {
    private Direction direction;
    @Serial
    private static final long serialVersionUID = 1L;
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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Turns the direction of the shield and the component
     */
    public void turn(){
        super.turn();
        if(direction == Direction.NE){ setDirection(Direction.SE);}
        else if(direction == Direction.SE){setDirection(Direction.SW);}
        else if(direction == Direction.SW){setDirection(Direction.NW);}
        else if(direction == Direction.NW){setDirection(Direction.NE);}
    }
}
