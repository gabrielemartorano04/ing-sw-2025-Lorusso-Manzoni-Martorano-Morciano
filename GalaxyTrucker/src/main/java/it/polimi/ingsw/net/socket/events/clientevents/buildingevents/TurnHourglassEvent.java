package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
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
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }
}
