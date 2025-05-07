package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event from client to server, sent when the player wants to turn the hourglass
 */
public class TurnHourglassEvent extends ClientEvent {
    private String message;

    public TurnHourglassEvent() {
        super(ClientEventType.TURN_HOURGLASS);
        message = "I want to turn the hourglass";
    }
    public String getMessage() {
        return message;
    }
}
