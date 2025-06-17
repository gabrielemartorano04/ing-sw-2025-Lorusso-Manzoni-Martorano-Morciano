package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class identifies a card stack composed of three cards from the stack
 */
public class Stack implements Serializable {

    /**
     * it defines the seral version of the class
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * It indicates whether the stack has been selected from another player.
     */
    private boolean selected;

    /**
     * List of cards that
     */
    private final ArrayList<Card> stack;
    private final int size;
    private final int indexOfThis;

    public Stack(int indexOfThis) {
        this.size = 10;
        this.stack = new ArrayList<>();
        this.selected = false;
        this.indexOfThis = indexOfThis;
    }

    public ArrayList<Card> getStack() {
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected() {
        this.selected = true;
    }

    public int getSize() {
        return size;
    }
}
