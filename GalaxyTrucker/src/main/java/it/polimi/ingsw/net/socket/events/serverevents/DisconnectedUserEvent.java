package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from server to clients. A player disconnected
 */
public class DisconnectedUserEvent extends Event {
    String username, message;
    public DisconnectedUserEvent(String username){
        super(EventType.DISCONNECTED_USER);
        this.username = username;
        message = username+" disconnected";
    }
    public String getUsername(){
        return username;
    }
    public String getMessage(){
        return message;
    }
}
