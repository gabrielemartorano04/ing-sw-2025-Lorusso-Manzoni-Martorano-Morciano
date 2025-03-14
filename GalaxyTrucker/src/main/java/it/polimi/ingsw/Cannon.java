package it.polimi.ingsw;

public class Cannon extends Component{
    private boolean isDouble;
    private boolean isActive;
    public enum Orientation{N, E, S, W};

    private Orientation orientation;

    public Cannon(Orientation orientation) {
        this.isDouble = false;
        this.isActive = false;
        this.orientation = orientation;
        super.setType("Cannon");
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
