package it.polimi.ingsw;

public class Cannon extends Component{
    private boolean isDouble;
    //aggiungere la enum per orientation

    public Cannon() {
        this.isDouble = false;
    }

    public void setIsDouble(boolean isDouble) {
        isDouble = true;
    }

    public void setPower() {
        ;
    }

    public boolean getIsDouble() {
        return isDouble;
    }

    public void getPower() {
        ;
    }
}
