package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event server to clients. It signals that a user reconnected to the game.
 */
public class ReconnectedUserEvent extends Event {
    String username,message;
    public ReconnectedUserEvent(String username){
        super(EventType.RECONNECTED_USER);
        this.username = username;
        message = username+" reconnected";
    }
    public String getUsername(){
        return username;
    }
    public String getMessage(){
        return message;
    }
}
