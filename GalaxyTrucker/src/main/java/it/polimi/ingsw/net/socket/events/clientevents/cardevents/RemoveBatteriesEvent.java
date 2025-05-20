package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class RemoveBatteriesEvent extends ClientEvent {

    ArrayList<int[]> coordinatesToRemoveBatteries;

    public RemoveBatteriesEvent(ArrayList<int[]> coordinatesToRemoveBatteries) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.coordinatesToRemoveBatteries = coordinatesToRemoveBatteries;
    }

    public ArrayList<int[]> getCoordinatesToRemoveBatteries() {
        return coordinatesToRemoveBatteries;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
