package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class TurnComponentEvent extends ClientEvent {
    private Component component;
    public TurnComponentEvent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
