package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the current condition of warzone card in the game
 */
public class CurrentCondition extends CardEvent{

    /**
     * Current card condition
     */
    private final Condition condition;

    /**
     * Map of truckers' positions
     */
    private final Map<String, Integer> track;

    /**
     * List of removed players
     */
    private final ArrayList<Trucker> truckersRemoved;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param parserState state of the parser
     * @param nickname of the player
     * @param condition of the warzone card
     * @param track map of truckers' positions
     * @param deadTruckersName nickname of the death truckers
     * @param truckers list of players
     */
    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition, Map<String, Integer> track, ArrayList<Trucker> deadTruckersName, ArrayList<Trucker> truckers) {
        super(forAll, parserState, nickname, truckers);
        this.condition = condition;
        this.track=track;
        this.truckersRemoved=deadTruckersName;
    }

    /**
     * @return the current condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * @return the map of truckers' positions
     */
    public Map<String, Integer> getTrack() {
        return track;
    }

    /**
     * @return the list of removed truckers
     */
    public ArrayList<Trucker> getTruckersRemoved(){
        return this.truckersRemoved;
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
