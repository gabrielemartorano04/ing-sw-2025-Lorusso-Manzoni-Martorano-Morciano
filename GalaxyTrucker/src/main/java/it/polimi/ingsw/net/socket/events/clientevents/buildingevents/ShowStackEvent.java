package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class ShowStackEvent extends ClientEvent {
    private int index;
    public ShowStackEvent(final int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public String getMessage() {
        return "I want to stanch n " + index;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
