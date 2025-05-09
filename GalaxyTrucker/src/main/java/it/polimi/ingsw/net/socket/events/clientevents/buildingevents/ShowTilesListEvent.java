package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class ShowTilesListEvent extends ClientEvent {
    private String message;

    public ShowTilesListEvent() {
        super(ClientEventType.SHOW_TILES, false);
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
