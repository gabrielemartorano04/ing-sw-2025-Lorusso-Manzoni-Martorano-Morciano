package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event from server to client. A player tried to turn the hourglass but it's not possible at the moment
 */
public class TurnHourglassError extends ServerEvent {
    private String username,message;
    public TurnHourglassError(String username) {
        super(ServerEventType.TURN_HOURGLASS_ERROR, false);
        this.username = username;
        message = username+ " tried to turn the hourglass but it's not possible at the moment";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
