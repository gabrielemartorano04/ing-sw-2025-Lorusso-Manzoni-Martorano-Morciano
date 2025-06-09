package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

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
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client );
    }
}
