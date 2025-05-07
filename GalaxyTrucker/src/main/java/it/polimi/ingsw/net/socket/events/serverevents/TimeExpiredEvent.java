package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event from server to client. The time is expired
 */
public class TimeExpiredEvent extends ServerEvent {
    String message;
    public TimeExpiredEvent() {
        super(ServerEventType.TIME_EXPIRED);
        message = "Time is expired";
    }
    public String getMessage() {
        return message;
    }
}
