package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

/**
 * Event from client to server, sent when the player wants to turn the hourglass
 */
public class TurnHourglassEvent extends ClientEvent {

    public TurnHourglassEvent() {
    }

    @Override
    public String getMessage() {
        return "I want to turn the hourglass";
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
