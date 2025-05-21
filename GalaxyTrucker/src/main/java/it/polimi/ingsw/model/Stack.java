package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;

import java.util.ArrayList;

public class Stack {
    private boolean selected;
    private ArrayList<Card> stack;
    private int size;
    private int indexOfThis;

    public Stack(int indexOfThis) {
        this.size = 10;
        this.stack = new ArrayList<>();
        this.selected = false;
        this.indexOfThis = indexOfThis;
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

    public int getIndexOfThis(){
        return indexOfThis;
    }
}
