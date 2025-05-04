package it.polimi.ingsw.net.events;

public class DisconnectedUserEvent extends Event{
    public DisconnectedUserEvent(){
        super(EventType.DISCONNECTED_USER);
    }
}
