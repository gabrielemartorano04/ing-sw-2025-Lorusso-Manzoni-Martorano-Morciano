package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

import java.util.ArrayList;


/**
 * Event client to server. The player insert a list of coordinates
 */
public class SendCoordListEvent extends ClientEvent {
    private ArrayList<int[]> coordinates;
    public SendCoordListEvent(ArrayList<int[]> coordinates) {
        super(ClientEventType.SEND_INFO_EVENT, true);
        this.coordinates = coordinates;
    }
    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }
    @Override
    public String getMessage() {
        return "Sent coordinate list: " + coordinates;
    }
}
