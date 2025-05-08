package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.client.ServerEventVisitor;

import java.util.ArrayList;

public class AvailableComponentsEvent extends ServerEvent {
    private String message;
    private AvailableComponents components;
    public AvailableComponentsEvent(AvailableComponents components) {
        super(ServerEventType.AVAILABLE_COMPONENTS, false);
        this.components = components;
        this.message = "Available components" + components;

    }

    @Override
    public String getMessage() {
        return message;
    }

    public AvailableComponents getComponents() {
        return components;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
