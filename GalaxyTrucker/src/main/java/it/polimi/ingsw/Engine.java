package it.polimi.ingsw;

public class Engine extends Component{
    private boolean isDouble;
    private boolean isActive;

    public Engine(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, boolean isDouble) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isDouble = isDouble;
        this.isActive = false;
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
