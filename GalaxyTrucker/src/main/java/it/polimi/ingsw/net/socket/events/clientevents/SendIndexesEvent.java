package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

import java.util.ArrayList;

/**
 * event from client to server
 */
public class SendIndexesEvent extends Event {
    private ArrayList<Integer> indexes;
    public SendIndexesEvent(ArrayList<Integer> indexes) {
        super(EventType.SEND_INFO_EVENT);
        this.indexes = indexes;
    }
    public ArrayList<Integer> getIndexes() {
        return indexes;
    }
}
