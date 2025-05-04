package it.polimi.ingsw.net.socket.events;

public class InvalidPositionError extends Event{
    public InvalidPositionError(){
        super(EventType.INVALID_POSITION_ERROR);
    }
}
