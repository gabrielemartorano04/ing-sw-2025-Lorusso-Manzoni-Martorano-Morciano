package it.polimi.ingsw.exceptions;

/**
 * The player didn't choose enough batteries
 */
public class NotEnoughGoodsChosen extends Exception {
    public NotEnoughGoodsChosen(String message) {
        super(message);
    }
}
