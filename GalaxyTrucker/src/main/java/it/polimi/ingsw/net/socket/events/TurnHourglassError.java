package it.polimi.ingsw.net.socket.events;

/**
 * Event from server to client. A player tried to turn the hourglass but it's not possible at the moment
 */
public class TurnHourglassError extends Event{
    private String username,message;
    public TurnHourglassError(String username) {
        super(EventType.TURN_HOURGLASS_ERROR);
        this.username = username;
        message = username+ " tried to turn the hourglass but it's not possible at the moment";
    }
    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
