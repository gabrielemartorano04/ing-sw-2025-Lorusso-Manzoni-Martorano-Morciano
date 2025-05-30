package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class RollDiceEvent extends ClientEvent {

    public RollDiceEvent() {

    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
