package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class SelectKeptTileEvent extends ClientEvent {
    private int index;
    public SelectKeptTileEvent(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public String getMessage() {
        return "I want to select my kept component n: "+index;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
