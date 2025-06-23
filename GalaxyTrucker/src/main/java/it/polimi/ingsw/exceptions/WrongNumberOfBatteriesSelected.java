package it.polimi.ingsw.exceptions;

/**
 * The player selected a wrong number of batteries
 */
public class WrongNumberOfBatteriesSelected extends Exception {
    public WrongNumberOfBatteriesSelected(String message) {
        super(message);
    }
}
