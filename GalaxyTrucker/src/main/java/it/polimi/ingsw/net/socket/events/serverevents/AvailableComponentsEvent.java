package it.polimi.ingsw.net.socket.events.serverevents;

import java.util.ArrayList;

public class AvailableComponentsEvent extends ServerEvent {
    private String message;
    public AvailableComponentsEvent(ArrayList<String> components) {
        super(ServerEventType.AVAILABLE_COMPONENTS);
        this.message = "Available components" + ;

    }
}
