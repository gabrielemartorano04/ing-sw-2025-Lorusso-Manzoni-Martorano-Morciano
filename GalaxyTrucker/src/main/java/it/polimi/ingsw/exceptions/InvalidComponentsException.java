package it.polimi.ingsw.exceptions;

/**
 * The player chose an invalid component
 */
public class InvalidComponentsException extends Exception {
  public InvalidComponentsException(String message) {
    super(message);
  }
}
