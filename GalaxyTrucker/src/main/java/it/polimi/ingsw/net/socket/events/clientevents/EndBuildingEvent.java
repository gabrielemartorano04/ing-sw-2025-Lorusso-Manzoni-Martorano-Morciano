package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event client to server
 */
public class EndBuildingEvent extends Event {
    String message;
    public EndBuildingEvent(){
        super(EventType.END_BUILDING);
        message="my spaceship is done";
    }

    public String getMessage(){
        return message;
    }
}
