package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

public class DiscardSuccessEvent extends ServerEvent{

    private String message;

    public DiscardSuccessEvent() {
        super(ServerEventType.DISCART_SUCCESS, false);
        message = "Discard success";
    }

    @Override
    public String toString() {
        return message;
    }
}
