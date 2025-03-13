package it.polimi.ingsw;

public class LifeSupport extends Component{
    private enum Color {PURPLE, BROWN};

    private Color color;

    public LifeSupport(Color color) {
        this.color = color;
        super.setType("Life Support");
    }

    public String getColor() {
        if (color == Color.PURPLE) {
            return "PURPLE";
        } else {
            return "BROWN";
        }
    }

    public String getType() {
        return type;
    }
}
