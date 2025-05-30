package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class SelectComponentEvent extends ServerEvent {
    private String message;
    private Component component;

    public SelectComponentEvent(Component component) {
        super(ServerEventType.COMPONENT_SELECTED, false);
        message="Component selected: " + component;
        this.component = component;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
