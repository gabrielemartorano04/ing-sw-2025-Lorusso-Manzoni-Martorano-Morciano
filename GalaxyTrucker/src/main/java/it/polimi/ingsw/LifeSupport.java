package it.polimi.ingsw;

public class LifeSupport extends Component{

    private LifeSupportColor color;

    public LifeSupport(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, LifeSupportColor color) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.color = color;
    }

    public LifeSupportColor getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
