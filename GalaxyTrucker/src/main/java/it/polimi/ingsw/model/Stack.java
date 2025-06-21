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
     * Serial version of the class
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * It indicates whether the stack has been selected from another player.
     */
    private boolean selected;

    /**
     * List of cards in the stack
     */
    private final ArrayList<Card> stack;

    /**
     * Stack size
     */
    private final int size;

    /**
     * Index of the stack
     */
    private final int indexOfThis;

    /**
     * Class constructor
     * @param indexOfThis index of the stack
     */
    public Stack(int indexOfThis) {
        this.size = 10;
        this.stack = new ArrayList<>();
        this.selected = false;
        this.indexOfThis = indexOfThis;
    }

    /**
     * @return cards in the stack
     */
    public ArrayList<Card> getStack() {
        return stack;
    }

    /**
     * This method sets the stack as unselected
     */
    public void dropStack(){
        this.selected = false;
    }

    /**
     * This method adds a card in the stack
     * @param card card to add
     */
    public void addCardInStack(Card card) {
        this.stack.add(card);
    }

    /**
     * Stack index getter
     * @return the index of the stack
     */
    public int getIndexOfThis(){
        return indexOfThis;
    }

    /**
     * This method tells if the stack is selected
     * @return if the stack is selected or not
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * This method sets the stack as selected
     */
    public void setSelected() {
        this.selected = true;
    }

}
