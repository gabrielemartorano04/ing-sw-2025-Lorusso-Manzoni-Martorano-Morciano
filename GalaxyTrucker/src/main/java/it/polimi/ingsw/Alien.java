package it.polimi.ingsw;

public class Alien {
    private enum AlienColor {PURPLE, BROWN};

    private AlienColor color;

    public Alien(AlienColor color){
        this.color = color;
    }

    public AlienColor getColor() {
        return color;
    }
}
