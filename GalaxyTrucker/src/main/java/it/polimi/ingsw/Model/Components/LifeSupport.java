package it.polimi.ingsw.Model.Components;

public class LifeSupport extends Component{

    private AlienColor color;

    public LifeSupport(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, AlienColor color) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.color = color;
    }

    public AlienColor getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
