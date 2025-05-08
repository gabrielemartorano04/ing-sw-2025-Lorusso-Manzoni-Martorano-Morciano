package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

/**
 * Event from server to clients. A player disconnected
 */
public class DisconnectedUserEvent extends ServerEvent {
    String username, message;
    public DisconnectedUserEvent(String username){
        super(ServerEventType.DISCONNECTED_USER, true);
        this.username = username;
        message = username+" disconnected";
    }
    public String getUsername(){
        return username;
    }
    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
