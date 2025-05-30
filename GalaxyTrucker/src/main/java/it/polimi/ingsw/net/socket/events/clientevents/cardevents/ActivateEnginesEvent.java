package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

import java.util.ArrayList;

public class ActivateEnginesEvent extends ClientEvent {

    private boolean decision;
    private ArrayList<int[]> coordinates, batteryCoordinates;
    private String nickname;

    public ActivateEnginesEvent(boolean decision, ArrayList<int[]> coordinates, ArrayList<int[]> batteryCoordinates,String nickname) {
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
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
