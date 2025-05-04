package it.polimi.ingsw.net.socket.events;

public class NoAvailableSpaceError extends Event{
    public NoAvailableSpaceError(){
        super(EventType.NO_AVAILABLE_SPACE);
    }
}
