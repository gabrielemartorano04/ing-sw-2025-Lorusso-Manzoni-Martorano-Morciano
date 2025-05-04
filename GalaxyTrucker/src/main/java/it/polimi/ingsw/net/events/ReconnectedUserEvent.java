package it.polimi.ingsw.net.events;

public class ReconnectedUserEvent extends Event{
    public ReconnectedUserEvent(){
        super(EventType.RECONNECTED_USER);
    }
}
