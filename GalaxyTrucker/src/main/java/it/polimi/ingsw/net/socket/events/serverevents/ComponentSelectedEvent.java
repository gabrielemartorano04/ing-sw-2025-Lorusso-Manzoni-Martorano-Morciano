package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

/**
 * Event from server to client that returns the selected successfully component
 */
public class ComponentSelectedEvent extends ServerEvent {
    private String message;
    private Component component;

    public ComponentSelectedEvent(Component component) {
        super(ServerEventType.COMPONENT_SELECTED);
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
}
