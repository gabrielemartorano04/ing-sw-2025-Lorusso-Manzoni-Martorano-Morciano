package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event client to server
 */
public class EndBuildingEvent extends ClientEvent {
    String message;
    public EndBuildingEvent(){
        message="my spaceship is done";
    }
    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client);
    }
}
