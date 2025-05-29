package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class ShowTilesListEvent extends ClientEvent {
    private String message;

    public ShowTilesListEvent() {
        message="my spaceship is done";
    }

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }
}
