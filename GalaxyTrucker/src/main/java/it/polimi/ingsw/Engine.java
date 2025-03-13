package it.polimi.ingsw;

public class Engine extends Component{
    private boolean isDouble;
    private boolean isActive;

    public Engine() {
        this.isDouble = false;
        this.isActive = false;
        super.setType("Engine");
    }

    public void setIsDouble(boolean isDouble) {
        this.isDouble = isDouble;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getPower() {
        if (isDouble) {
            if (isActive) {
                return 2;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public String getType() {
        return type;
    }
}
