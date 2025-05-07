package it.polimi.ingsw.net.socket.events.clientevents;

/**
 * Event from client to server, sent when the player wants to turn the hourglass
 */
public class TurnHourglassEvent extends ClientEvent {

    public TurnHourglassEvent() {
        super(ClientEventType.TURN_HOURGLASS);
    }
    @Override
    public String getMessage() {
        return "I want to turn the hourglass";
    }
}
