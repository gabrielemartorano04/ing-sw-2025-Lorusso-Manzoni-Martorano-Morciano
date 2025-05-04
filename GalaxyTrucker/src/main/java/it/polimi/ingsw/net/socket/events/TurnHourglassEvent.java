package it.polimi.ingsw.net.socket.events;

/**
 * Event from client to server, sent when the player wants to turn the hourglass
 */
public class TurnHourglassEvent extends Event {
    private String username,message;

    public TurnHourglassEvent(String username) {
        super(EventType.TURN_HOURGLASS);
        this.username = username;
        message = username+" wants to turn the hourglass";
    }
    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
}
