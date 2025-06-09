package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class RollDiceEvent extends ClientEvent {

    public RollDiceEvent() {

    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
