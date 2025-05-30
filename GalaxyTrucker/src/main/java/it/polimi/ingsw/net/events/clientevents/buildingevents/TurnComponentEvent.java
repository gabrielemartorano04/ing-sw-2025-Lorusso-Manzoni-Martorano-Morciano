package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class TurnComponentEvent extends ClientEvent {
    private Component component;
    public TurnComponentEvent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
