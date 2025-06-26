package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an event triggered when a flight starts in the game
 */
public class FlightStartEvent extends ServerEvent {

    /**
     * Position of the trucker
     */
    private final int position;

    /**
     * Map of truckers' positions
     */
    private final Map<String,Integer> track;

    /**
     * Trucker's spaceship
     */
    private final SpaceShip spaceShip;

    /**
     * List of players in the game
     */
    private final ArrayList<String> players;

    /**
     * Event constructor
     * @param position of the trucker
     * @param track mao of truckers' positions
     * @param spaceShip of the trucker
     * @param players list of players in the game
     */
    public FlightStartEvent(int position, Map<String,Integer> track, SpaceShip spaceShip, ArrayList<String> players) {
        super(false);
        this.position = position;
        this.track = track;
        this.spaceShip = spaceShip;
        this.players = players;
    }

    /**
     * @return the map of truckers' positions
     */
    public Map<String,Integer> getTrack() {
        return track;
    }

    /**
     * @return the trucker's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the list of player in the game
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * @return the spaceship of the trucker
     */
    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    /**
     * @return the message associated with the start of flight phase
     */
    @Override
    public String getMessage() {
        return "Flight started, your spaceship were also filled";
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
