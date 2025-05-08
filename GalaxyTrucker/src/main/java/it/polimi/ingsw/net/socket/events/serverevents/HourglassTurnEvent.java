package it.polimi.ingsw.net.socket.events.serverevents;

public class HourglassTurnEvent extends ServerEvent{
    private String message;

    public HourglassTurnEvent() {
        super(ServerEventType.TURN_HOURGLASS, true);
        message = "Hourglass turned";
    }

    @Override
    public String toString() {
        return message;
    }
}
