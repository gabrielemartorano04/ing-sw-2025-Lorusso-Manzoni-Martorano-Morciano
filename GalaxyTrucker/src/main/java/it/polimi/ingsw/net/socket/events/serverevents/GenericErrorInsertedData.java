package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Error event from server to client. It signals a generic error when the player inserted data
 */
public class GenericErrorInsertedData extends Event {
    private String message;
    public GenericErrorInsertedData(){
        super(EventType.GENERIC_ERROR_INSERTED_DATA);
        message = "Error inserted data";
    }
    public String getMessage(){
        return message;
    }
}
