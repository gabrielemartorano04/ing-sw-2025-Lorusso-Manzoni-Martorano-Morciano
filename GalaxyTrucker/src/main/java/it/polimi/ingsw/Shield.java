package it.polimi.ingsw;

public class Shield extends Component{
    private enum Direction {NE, NW, SE, SW};

    private Direction direction;

    public Shield(Direction direction) {
        this.direction = direction;
        super.setType("Shield");
    }

    public Direction getDirection() {
        return direction;
    }

    public String getType() {
        return type;
    }
}
