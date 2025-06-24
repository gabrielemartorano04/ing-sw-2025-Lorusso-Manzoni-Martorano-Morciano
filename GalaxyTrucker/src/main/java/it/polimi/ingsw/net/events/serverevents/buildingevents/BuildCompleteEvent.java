package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Event sent to inform that building is completed
 */
public class BuildCompleteEvent extends ServerEvent {

    /**
     * List of all the truckers playing
     */
    private final ArrayList<Trucker> truckers;

    /**
     * Event constructor
     * @param truckers list mof all the trucker
     */
    public BuildCompleteEvent(ArrayList<Trucker> truckers) {
        super(true);
        this.truckers = truckers;
    }

    /**
     * @return the list of all the truckers playing
     */
    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }

    /**
     * @return the message associated with the completion of the building phase
     */
    @Override
    public String toString() {
        return "All the ships are checked and the building phase is complete";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
