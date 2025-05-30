package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

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
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
