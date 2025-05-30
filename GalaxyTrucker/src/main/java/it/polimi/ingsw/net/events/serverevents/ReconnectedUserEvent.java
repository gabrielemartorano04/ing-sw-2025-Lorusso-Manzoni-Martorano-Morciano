package it.polimi.ingsw.net.events.serverevents;


import it.polimi.ingsw.net.ServerEventHandlerVisitor;

/**
 * Event server to clients. It signals that a user reconnected to the game.
 */
public class ReconnectedUserEvent extends ServerEvent {
    String username,message;
    public ReconnectedUserEvent(String username){
        super(ServerEventType.RECONNECTED_USER, true);
        this.username = username;
        message = username+" reconnected";
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
