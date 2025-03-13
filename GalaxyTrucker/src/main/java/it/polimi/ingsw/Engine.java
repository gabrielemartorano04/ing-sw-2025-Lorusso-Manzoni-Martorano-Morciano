package it.polimi.ingsw;

public class Engine extends Component{
    private boolean isDouble;

    public Engine() {
        this.isDouble = false;
        super.setType("engine");
    }

    public void setIsDouble(boolean isDouble) {
        isDouble = true;
    }

    public void setPower() {
        ;
    }

    public void getIsDouble() {
        ;
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
