package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents a specific type of card event in the game where a decision must be made
 * based on a boolean condition
 */
public class BooleanDecision extends CardEvent{

    /**
     * Event constructor
     * @param forAll Determines if the decision affects all players or just an individual player
     * @param parserState The current state of the parser, used to define the type of action or decision required
     * @param nickname The nickname of the player associated with this decision event
     * @param truckers A list of Trucker objects representing all players in the game
     */
    public BooleanDecision(boolean forAll, ParserState parserState, String nickname, ArrayList<Trucker> truckers) {
        super(forAll, parserState, nickname, truckers);
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        switch(getParserState()){
            case ROLL_DICE -> {
                return getNickname() + " is rolling the dice";
            }
            case ACTIVATE_CANNONS -> {
                return getNickname() + " is deciding which cannons to activate";
            }
            case ACTIVATE_ENGINES -> {
                return getNickname() + " is deciding which engines to activate";
            }
            case ACTIVATE_SHIELD -> {
                return getNickname() + " is deciding which shield to activate";
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
