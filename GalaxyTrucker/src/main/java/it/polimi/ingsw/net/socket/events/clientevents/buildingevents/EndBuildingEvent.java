package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

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
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
