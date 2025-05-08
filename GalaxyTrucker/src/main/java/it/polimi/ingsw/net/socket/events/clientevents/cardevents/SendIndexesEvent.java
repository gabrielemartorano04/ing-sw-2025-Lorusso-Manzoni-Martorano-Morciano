package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

import java.util.ArrayList;

/**
 * event from client to server
 */
public class SendIndexesEvent extends ClientEvent {
    private ArrayList<Integer> indexes;
    public SendIndexesEvent(ArrayList<Integer> indexes) {
        super(ClientEventType.SEND_INFO_EVENT, true);
        this.indexes = indexes;
    }
    public ArrayList<Integer> getIndexes() {
        return indexes;
    }
    @Override
    public String getMessage() {
        return "Sent indexes: " + indexes.toString();
    }
}
