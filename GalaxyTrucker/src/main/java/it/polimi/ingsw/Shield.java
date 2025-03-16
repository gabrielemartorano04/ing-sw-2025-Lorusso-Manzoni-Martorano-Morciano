package it.polimi.ingsw;

public class Shield extends Component{

    private Direction direction;

    public Shield(int linkU, int linkR, int linkD, int linkL, String type, String imagePath) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.direction = Direction.NE;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getType() {
        return type;
    }
}
