package it.polimi.ingsw;

public class Engine extends Component{
    private boolean isDouble;
    private boolean isActive;

    public Engine(int linkU, int linkR, int linkD, int linkL, String type) {
        this.isDouble = false;
        this.isActive = false;
        super.link = new int[] {linkU, linkR, linkD, linkL};
        super.setType(type);
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
