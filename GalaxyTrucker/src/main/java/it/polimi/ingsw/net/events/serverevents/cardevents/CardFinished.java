package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an event triggered when a card is finished in the game
 */
public class CardFinished extends CardEvent {

    /**
     * List of removed truckers
     */
    private final ArrayList<Trucker> truckersRemoved;

    /**
     * Map of the truckers' position
     */
    Map<String, Integer> track;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param truckerList list of all truckers
     * @param truckerRemoved list of removed truckers
     * @param track map of the truckers' positions
     */
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<Trucker> truckerRemoved, Map<String, Integer> track) {
        super(forAll, ParserState.DRAW_CARD, null, truckerList);
        this.truckersRemoved = truckerRemoved;
        this.track = track;
    }

    /**
     * @return the list of removed truckers
     */
    public ArrayList<Trucker> getTruckersRemoved(){
        return truckersRemoved;
    }

    /**
     * @return the map of the truckers' position
     */
    public Map<String, Integer> getTrack() {
        return track;
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
