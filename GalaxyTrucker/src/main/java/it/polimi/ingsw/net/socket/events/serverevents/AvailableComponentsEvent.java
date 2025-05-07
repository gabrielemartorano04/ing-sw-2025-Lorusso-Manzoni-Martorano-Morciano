package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;

public class AvailableComponentsEvent extends ServerEvent {
    private String message;
    private ArrayList<Component> components;
    public AvailableComponentsEvent(ArrayList<Component> components) {
        super(ServerEventType.AVAILABLE_COMPONENTS);
        this.components = components;
        this.message = "Available components" + ;

    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
