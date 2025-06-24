package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;
import java.util.Map;

public class FlightStartEvent extends ServerEvent {
    private final String message;
    private final int position;
    private final int batteryCount;
    private final int humanCount;
    private final int enginePower;
    private final float cannonPower;
    private final Map<String,Integer> track;
    private final SpaceShip spaceShip;
    private final ArrayList<String> players;


    public FlightStartEvent(int position, int batteryCount, int humanCount, float cannonPower, int enginePower, Map<String,Integer> track, SpaceShip spaceShip, ArrayList<String> players) {
        super(false);
        message = "Flight started, your spaceship were also filled";
        this.position = position;
        this.batteryCount = batteryCount;
        this.humanCount = humanCount;
        this.cannonPower = cannonPower;
        this.enginePower = enginePower;
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
    public int getBatteryCount() {
        return batteryCount;
    }
    public int getHumanCount() {
        return humanCount;
    }

    public float getCannonPower() {
        return cannonPower;
    }
    public int getEnginePower() {
        return enginePower;
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
