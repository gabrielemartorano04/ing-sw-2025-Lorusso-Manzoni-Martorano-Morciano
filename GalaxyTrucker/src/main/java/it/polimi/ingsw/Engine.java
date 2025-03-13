package it.polimi.ingsw;

public class Engine extends Component{
    private boolean isDouble;
    private String type;

    public Engine() {
        isDouble = false;
        type = "engine";
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

    public void getPower() {
        ;
    }

    public String getType() {
        return type;
    }
}
