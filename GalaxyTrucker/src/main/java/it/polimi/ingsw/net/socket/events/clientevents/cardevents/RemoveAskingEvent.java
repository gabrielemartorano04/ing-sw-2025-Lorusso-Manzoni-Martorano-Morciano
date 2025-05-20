package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class RemoveAskingEvent extends ClientEvent {

    private ArrayList<Integer> indexes;

    public RemoveAskingEvent(ArrayList<Integer> indexes) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.indexes = indexes;
    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
