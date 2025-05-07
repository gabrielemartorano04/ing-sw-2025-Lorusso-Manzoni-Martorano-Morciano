package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event from server to client. The time is expired
 */
public class TimeExpiredEvent extends ServerEvent {
    String message;
    public TimeExpiredEvent() {
        super(ServerEventType.TIME_EXPIRED, true);
        message = "Time is expired";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
