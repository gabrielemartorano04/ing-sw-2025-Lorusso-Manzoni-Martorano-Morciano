package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class TurnedComponentSuccessEvent extends ServerEvent {
    private Component component;
    public TurnedComponentSuccessEvent(Component component) {
        super(false);
        this.component = component;
    }
    public Component getComponent() {
        return component;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
