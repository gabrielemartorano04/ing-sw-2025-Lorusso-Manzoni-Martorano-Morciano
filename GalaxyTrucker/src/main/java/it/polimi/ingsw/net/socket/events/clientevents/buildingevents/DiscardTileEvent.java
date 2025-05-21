package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.DiscardSuccessEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class DiscardTileEvent extends ClientEvent {
    private int index;
    public DiscardTileEvent(int index) {
            this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getMessage() {
        return "I want to discard the selected tile";
    }

    @Override
    public ServerEvent call(ClientVisitor visitor){
        return visitor.visit(this);
    }
}
