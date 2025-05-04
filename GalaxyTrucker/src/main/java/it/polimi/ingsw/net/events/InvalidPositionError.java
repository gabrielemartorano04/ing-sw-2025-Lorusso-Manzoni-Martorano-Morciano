package it.polimi.ingsw.net.events;

public class InvalidPositionError extends Event{
    public InvalidPositionError(){
        super(EventType.INVALID_POSITION_ERROR);
    }
}
