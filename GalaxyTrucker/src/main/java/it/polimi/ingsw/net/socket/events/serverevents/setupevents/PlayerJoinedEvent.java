package it.polimi.ingsw.net.socket.events.serverevents.setupevents;


import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

/**
 * Event from server to client to notify the successful login for a player
 */
public class PlayerJoinedEvent extends ServerEvent {
    String username,message;
    public PlayerJoinedEvent(String username) {
        super(ServerEventType.PLAYER_JOINED, true);
        this.username = username;
        message = username+" has joined the game";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
