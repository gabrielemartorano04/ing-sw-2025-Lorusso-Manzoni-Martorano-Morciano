package it.polimi.ingsw;

public class Engine extends Component{
    private boolean isDouble;
    private boolean isActive;
    public Engine() {
        this.isDouble = false;
        super.setType("Engine");
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

    public boolean getIsActive() {
        return isActive;
    }

    public int getPower() {
        if (isDouble) {
            return 2;
        }else{
            return 1;
        }
    }

    public String getType() {
        return type;
    }
}
