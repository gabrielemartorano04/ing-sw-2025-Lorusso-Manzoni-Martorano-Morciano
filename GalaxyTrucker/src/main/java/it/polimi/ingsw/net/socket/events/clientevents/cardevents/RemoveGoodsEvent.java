package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class RemoveGoodsEvent extends ClientEvent {

    public RemoveGoodsEvent() {

    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
