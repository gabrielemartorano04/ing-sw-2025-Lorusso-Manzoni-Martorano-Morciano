package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class RemotionSeenEvent extends ClientEvent {

    public RemotionSeenEvent() {
    }
    @Override
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }
}
