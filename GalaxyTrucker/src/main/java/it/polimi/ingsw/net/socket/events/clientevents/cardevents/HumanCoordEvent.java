package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class HumanCoordEvent extends ClientEvent {

    private ArrayList<int[]> humanCoord;

    public HumanCoordEvent(ArrayList<int[]> humanCoord) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.humanCoord = humanCoord;
    }

    public ArrayList<int[]> getHumanCoord() {
        return humanCoord;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
