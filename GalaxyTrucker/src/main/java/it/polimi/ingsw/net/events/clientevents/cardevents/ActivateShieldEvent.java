package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class ActivateShieldEvent extends ClientEvent {

    private boolean decision;
    private int[] coordinates, batteryCoordinates;
    private String nickname;

    public ActivateShieldEvent(boolean decision, int[] coordinates, int[] batteryCoordinates,String nickname) {
        this.decision = decision;
        this.coordinates = coordinates;
        this.batteryCoordinates = batteryCoordinates;
        this.nickname = nickname;
    }

    public boolean getDecision() {
        return decision;
    }

    public int[] getCoordinates() {
        return coordinates;
    }
    public int[] getBatteryCoordinates() {
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
