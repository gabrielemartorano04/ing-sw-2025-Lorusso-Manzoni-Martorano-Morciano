package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents a specific type of card-based event where a player decides on
 * the rearrangement of goods during the game
 */
public class RearrangeEvent extends CardEvent{

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param nickname of the player
     * @param truckers list of players
     */
    public RearrangeEvent(boolean forAll, String nickname, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.REARRANGE, nickname, truckers);
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" is deciding goods rearrangement.\n";
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
