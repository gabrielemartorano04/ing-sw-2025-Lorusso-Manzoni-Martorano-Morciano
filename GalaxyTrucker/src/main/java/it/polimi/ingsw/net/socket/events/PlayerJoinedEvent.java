package it.polimi.ingsw.net.socket.events;

/**
 * Event from server to client to notify the successful login for a player
 */
public class PlayerJoinedEvent extends Event {
    String username,message;
    public PlayerJoinedEvent(String username) {
        super(EventType.PLAYER_JOINED);
        this.username = username;
        message = username+" has joined the game";
    }
    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
}
