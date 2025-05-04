package it.polimi.ingsw.net.socket.events;

public class InvalidCoordinatesError extends Event{

    public InvalidCoordinatesError(){
        super(EventType.INVALID_COORDINATES_ERROR);
    }
}
