package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class SendHumanCoordEvent extends ClientEvent {

    private ArrayList<int[]> humanCoord;

    public SendHumanCoordEvent(ArrayList<int[]> humanCoord) {
        super(ClientEventType.SEND_INFO_EVENT, true);
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
