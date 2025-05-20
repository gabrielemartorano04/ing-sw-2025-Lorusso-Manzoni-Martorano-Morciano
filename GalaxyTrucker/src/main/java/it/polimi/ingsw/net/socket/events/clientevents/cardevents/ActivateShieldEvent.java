package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class ActivateShieldEvent extends ClientEvent {

    private boolean decision;
    private int[] coordinates, batteryCoordinates;

    public ActivateShieldEvent(boolean decision, int[] coordinates, int[] batteryCoordinates) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.decision = decision;
        this.coordinates = coordinates;
        this.batteryCoordinates = batteryCoordinates;
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
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
