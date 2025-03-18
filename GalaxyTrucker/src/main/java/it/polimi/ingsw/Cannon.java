package it.polimi.ingsw;

public class Cannon extends Component{
    private boolean isDouble;
    private boolean isActive;
    private Orientation orientation;

    public Cannon(int linkR, int linkD, int linkL, String type, String imagePath, boolean isDouble) {
        super(0, linkR, linkD, linkL, type, imagePath);
        this.orientation = Orientation.N;
        this.isDouble = isDouble;
        this.isActive = false;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsDouble() {
        return isDouble;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public String getType() {
        return type;
    }
}
