package it.polimi.ingsw.net.events;

public class TimeExpiredEvent extends Event {
    public TimeExpiredEvent() {
        super(EventType.TIME_EXPIRED);
    }
}
