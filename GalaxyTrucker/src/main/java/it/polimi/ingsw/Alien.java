package it.polimi.ingsw;

public class Alien {
    private enum Color {PURPLE, BROWN};

    private Color color;

    public Alien(Color color){
        this.color = color;
    }

    public String getColor() {
        if (this.color == Color.PURPLE) {
            return "PURPLE";
        }else{
            return "BROWN";
        }
    }
}
