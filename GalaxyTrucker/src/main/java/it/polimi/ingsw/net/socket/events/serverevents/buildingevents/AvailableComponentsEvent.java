package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class AvailableComponentsEvent extends ServerEvent {
    private String message;
    private ArrayList<Component> components;
    public AvailableComponentsEvent(ArrayList<Component> components) {
        super(ServerEventType.AVAILABLE_COMPONENTS, true);
        this.components = components;
        this.message = "Available components" + components;

    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
