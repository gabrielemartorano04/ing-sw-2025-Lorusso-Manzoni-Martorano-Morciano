package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class SendActivateCannonsEvent extends ClientEvent {

    private boolean decision;
    private ArrayList<int[]> coordinates, batteryCoordinates;

    public SendActivateCannonsEvent(boolean decision, ArrayList<int[]> coordinates, ArrayList<int[]> batteryCoordinates) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.decision = decision;
        this.coordinates = coordinates;
        this.batteryCoordinates = batteryCoordinates;
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
}
