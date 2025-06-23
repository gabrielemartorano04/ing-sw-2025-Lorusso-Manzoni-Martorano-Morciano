package it.polimi.ingsw.exceptions;

/**
 * The choosen nickname is already used in the game
 */
public class NicknameUsedException extends Exception {
    public NicknameUsedException(String message) {
        super(message);
    }
}
