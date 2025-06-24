package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class HourglassTurnEvent extends ServerEvent {
    private String message;
    private int seconds;

    public HourglassTurnEvent(int seconds) {
        super(true);
        message = "Hourglass turned";
        this.seconds= seconds;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public int getSeconds() {
        return seconds;
    }
}
