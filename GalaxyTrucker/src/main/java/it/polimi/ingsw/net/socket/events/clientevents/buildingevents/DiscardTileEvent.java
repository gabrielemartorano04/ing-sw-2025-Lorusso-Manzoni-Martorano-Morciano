package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

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
    public void call(VirtualServer visitor){
         visitor.visit(this);
    }
}
