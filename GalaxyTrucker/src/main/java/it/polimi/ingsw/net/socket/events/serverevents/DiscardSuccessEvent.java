package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

public class DiscardSuccessEvent extends ServerEvent{

    private String message;
    private Component component;

    public DiscardSuccessEvent(Component component) {
        super(ServerEventType.DISCART_SUCCESS, false);
        message = "Discard success";
        this.component = component;
    }

    @Override
    public String toString() {
        return message;
    }

    public Component getComponent() {
        return component;
    }
}
