package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

import java.util.ArrayList;


/**
 * Event client to server. The player insert a list of coordinates
 */
public class SendCoordListEvent extends ClientEvent {
    private ArrayList<int[]> coordinates;
    public SendCoordListEvent(ArrayList<int[]> coordinates) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.coordinates = coordinates;
    }
    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }
}
