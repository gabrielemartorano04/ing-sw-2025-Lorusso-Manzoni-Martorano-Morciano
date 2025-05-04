package it.polimi.ingsw.net.socket.events;

public class TimeExpiredEvent extends Event {
    public TimeExpiredEvent() {
        super(EventType.TIME_EXPIRED);
    }
}
