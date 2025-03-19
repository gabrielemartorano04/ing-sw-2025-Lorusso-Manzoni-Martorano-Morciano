package it.polimi.ingsw.Model.Cards;

public class Effect {
    private final TypeOfEffect type;
    private final int value;

    public Effect(TypeOfEffect type, int value) {
        this.type = type;
        this.value = value;
    }

    public TypeOfEffect getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}


