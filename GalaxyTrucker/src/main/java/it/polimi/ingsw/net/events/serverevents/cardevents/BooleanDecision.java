package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

/**
 * Represents a decision-based card event that interacts with a boolean response
 */
public class BooleanDecision extends CardEvent{

    /**
     * Message associated with the bolean decision
     */
    private final String message;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param parserState state of the input parser
     * @param message associated with the boolean decision
     * @param nickname of the player
     */
    public BooleanDecision(boolean forAll, ParserState parserState, String message, String nickname) {
        super(forAll, parserState, nickname);
        this.message = message;
    }

    /**
     * @return the message associated with the boolean decision
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the messages associated with the boolean decision for the other players who are not choosing
     */
    public String getInfoForOthers(){
        switch(getParserState()){
            case ROLL_DICE -> {
                return getNickname() + " is rolling the dice";
            }
            case ACTIVATE_CANNONS -> {
                return getNickname() + " is deciding wich cannons to activate";
            }
            case ACTIVATE_ENGINES -> {
                return getNickname() + " is deciding wich engines to activate";
            }
            case ACTIVATE_SHIELD -> {
                return getNickname() + " is deciding wich shield to activate";
            }
            case LAND -> {
                return getNickname() + " is deciding whether to land or not";
            }
        }
        return "";
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
