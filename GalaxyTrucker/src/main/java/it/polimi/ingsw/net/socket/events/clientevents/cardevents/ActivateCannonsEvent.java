package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class ActivateCannonsEvent extends ClientEvent {

    private boolean decision;
    private ArrayList<int[]> coordinates, batteryCoordinates;
    private String nickname;

    public ActivateCannonsEvent(boolean decision, ArrayList<int[]> coordinates, ArrayList<int[]> batteryCoordinates,String nickname) {
        this.decision = decision;
        this.coordinates = coordinates;
        this.batteryCoordinates = batteryCoordinates;
        this.nickname = nickname;
    }

    public boolean getDecision() {
        return decision;
    }

    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }

    public ArrayList<int[]> getBatteryCoordinates() {
        return batteryCoordinates;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
