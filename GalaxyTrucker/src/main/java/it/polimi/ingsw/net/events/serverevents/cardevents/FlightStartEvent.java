package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;
import java.util.Map;

public class FlightStartEvent extends ServerEvent {
    private final String message;
    private final int position;
    private final Map<String,Integer> track;
    private final SpaceShip spaceShip;
    private final ArrayList<String> players;


    public FlightStartEvent(int position, Map<String,Integer> track, SpaceShip spaceShip, ArrayList<String> players) {
        super(false);
        message = "Flight started, your spaceship were also filled";
        this.position = position;
        this.track = track;
        this.spaceShip = spaceShip;
        this.players = players;
    }

    public Map<String,Integer> getTrack() {
        return track;
    }
    public int getPosition() {
        return position;
    }
    public ArrayList<String> getPlayers() {
        return players;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
