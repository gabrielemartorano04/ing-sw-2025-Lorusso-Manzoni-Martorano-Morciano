package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event server to clients. It signals that a user reconnected to the game.
 */
public class ReconnectedUserEvent extends ServerEvent {
    String username,message;
    public ReconnectedUserEvent(String username){
        super(ServerEventType.RECONNECTED_USER);
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
