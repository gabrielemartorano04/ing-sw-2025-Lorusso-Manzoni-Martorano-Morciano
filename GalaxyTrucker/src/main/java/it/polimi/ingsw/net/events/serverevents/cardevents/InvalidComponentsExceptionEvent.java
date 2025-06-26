package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event that occurs when an invalid component is selected during the game
 */
public class InvalidComponentsExceptionEvent extends CardEvent {

    /**
     * Message associated with the error
     */
    private final String message;

    /**
     * Event construcotr
     * @param message associated with the error
     * @param forAll specify if the message is for a player or everyone
     * @param parserState state of the parser
     * @param nickname of the player
     */
    public InvalidComponentsExceptionEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        super(forAll, parserState, nickname,new ArrayList<>());
        this.message = message;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" chose an invalid component.\n";
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
