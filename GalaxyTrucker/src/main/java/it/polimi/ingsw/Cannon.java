package it.polimi.ingsw;

public class Cannon extends Component{
    private boolean isDouble;
    private boolean isActive;
    private Orientation orientation;

    public Cannon(int linkU, int linkR, int linkD, int linkL, String type, boolean isDouble) {
        super(linkU, linkR, linkD, linkL, type);
        this.orientation = Orientation.N;
        this.isDouble = isDouble;
        this.isActive = false;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setIsDouble(boolean isDouble) {
        this.isDouble = isDouble;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsDouble() {
        return isDouble;
    }

    public double getPower() {
        if (isDouble) {
            if (isActive) {
                if (orientation == Orientation.N  || orientation == Orientation.S) {
                    return 2;
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        }else{
            if (orientation == Orientation.N && orientation == Orientation.S){
                return 1;
            }else{
                return 0.5;
            }
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public String getType() {
        return type;
    }
}
