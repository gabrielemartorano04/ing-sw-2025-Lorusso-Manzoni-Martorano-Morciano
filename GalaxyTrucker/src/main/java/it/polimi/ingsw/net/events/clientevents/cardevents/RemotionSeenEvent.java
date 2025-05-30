package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class RemotionSeenEvent extends ClientEvent {

    public RemotionSeenEvent() {
    }
    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
