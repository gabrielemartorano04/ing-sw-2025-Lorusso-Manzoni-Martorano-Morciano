package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;

import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

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
    public ServerEvent call(VirtualServer visitor, Client client){
         return visitor.visit(this, client);
    }
}
