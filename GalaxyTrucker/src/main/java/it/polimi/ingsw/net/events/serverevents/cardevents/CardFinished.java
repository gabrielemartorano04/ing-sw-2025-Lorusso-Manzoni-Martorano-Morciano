package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an event indicating that the card phase has been completed
 */
public class CardFinished extends CardEvent {

    /**
     * List of playing truckers
     */
    private final ArrayList<Trucker> truckersList;

    /**
     * List of withdrawn or removed players
     */
    private final ArrayList<Trucker> truckersRemoved;

    /**
     * Positions of the players
     */
    Map<String, Integer> track;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param truckerList list of playing truckers
     * @param truckerRemoved list of withdrawn or removed players
     * @param track positions of the players
     */
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<Trucker> truckerRemoved, Map<String, Integer> track) {
        super(forAll, ParserState.DRAW_CARD, null);
        this.truckersList = truckerList;
        this.truckersRemoved = truckerRemoved;
        this.track = track;
        for(Trucker trucker : truckerList){
            System.out.println("trucker " + trucker.getNickname() + " in pos: " + trucker.getPosition());
        }
    }

    /**
     * @return the list of playing truckers
     */
    public ArrayList<Trucker> getTruckersList() {
        return truckersList;
    }

    /**
     * @return the list of withdrawn or removed players
     */
    public ArrayList<Trucker> getTruckersRemoved(){
        return truckersRemoved;
    }

    /**
     * @return the positions of the players
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
