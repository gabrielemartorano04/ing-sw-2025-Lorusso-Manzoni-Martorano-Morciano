package it.polimi.ingsw.net.socket.events.serverevents.flightevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.Map;

public class FlightStartEvent extends ServerEvent {
    private String message;
    private int position, batteryCount, humanCount,cannonPower, enginePower;
    private Map<String,Integer> track;
    private Component[][] spaceShip;

    public FlightStartEvent(int position, int batteryCount, int humanCount, int cannonPower, int enginePower, Map<String,Integer> track, Component[][] spaceShip) {
        super(ServerEventType.START_FLIGHT, false);
        message = "Flight started, your spaceship were also filled";
        this.position = position;
        this.batteryCount = batteryCount;
        this.humanCount = humanCount;
        this.cannonPower = cannonPower;
        this.enginePower = enginePower;
        this.track = track;
        this.spaceShip = spaceShip;
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

    public int getCannonPower() {
        return cannonPower;
    }
    public int getEnginePower() {
        return enginePower;
    }

    public Component[][] getSpaceShip() {
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
