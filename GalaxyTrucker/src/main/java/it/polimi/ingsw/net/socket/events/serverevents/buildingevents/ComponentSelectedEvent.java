package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

/**
 * Event from server to client that returns the selected successfully component
 */
public class ComponentSelectedEvent extends ServerEvent {
    private String message;
    private Component component;

    public ComponentSelectedEvent(Component component) {
        super(ServerEventType.COMPONENT_SELECTED, false);
        this.component = component;
        message="Component selected";
    }
    public Component getComponent() {
        return component;
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
