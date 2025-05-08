package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event from server to client. A player tried to turn the hourglass but it's not possible at the moment
 */
public class TurnHourglassError extends ServerEvent {
    private String message;
    public TurnHourglassError() {
        super(ServerEventType.TURN_HOURGLASS_ERROR, false);
        message = " You can't turn the hourglass at the moment";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
