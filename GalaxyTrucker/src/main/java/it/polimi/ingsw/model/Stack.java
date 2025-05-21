package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;

import java.util.ArrayList;

public class Stack {
    private boolean selected;
    private ArrayList<Card> stack;
    private int size;

    public Stack() {
        this.size = 10;
        this.stack = new ArrayList<>();
        this.selected = false;
    }

    public ArrayList<Card> getStack() {
        this.selected=true;
        return stack;
    }

    public void dropStack(){
        this.selected = false;
    }

    public void addCardInStack(Card card) {
        this.stack.add(card);
    }
}
