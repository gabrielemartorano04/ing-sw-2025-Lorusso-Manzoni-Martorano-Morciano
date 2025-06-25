package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent when the hourglass is turned.
 * This event notifies the client of the hourglass turn and specifies a duration in seconds.
 */
public class HourglassTurnEvent extends ServerEvent {

    /**
     * Duration of the hourglass
     */
    private final int seconds;

    /**
     * Event constructor
     * @param seconds of duration of the hourglass
     */
    public HourglassTurnEvent(int seconds) {
        super(true);
        this.seconds= seconds;
    }

    /**
     * @return the duration of the hourglass
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * @return the message associated with the turn of the hourglass
     */
    @Override
    public String toString() {
        return "Hourglass turned";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
