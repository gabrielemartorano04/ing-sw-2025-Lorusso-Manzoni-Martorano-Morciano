package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

public class SelectComponentEvent extends ServerEvent{
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
}
