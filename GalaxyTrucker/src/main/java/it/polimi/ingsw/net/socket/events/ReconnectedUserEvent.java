package it.polimi.ingsw.net.socket.events;

public class ReconnectedUserEvent extends Event{
    public ReconnectedUserEvent(){
        super(EventType.RECONNECTED_USER);
    }
}
