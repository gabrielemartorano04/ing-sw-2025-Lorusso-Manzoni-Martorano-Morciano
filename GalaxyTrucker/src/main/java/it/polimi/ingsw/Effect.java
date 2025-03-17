package it.polimi.ingsw;

public class Effect {
     private TypeOfEffect type; //type indica che cosa succede
     private int value; //value indica la quantità di perdite o di colpi

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


