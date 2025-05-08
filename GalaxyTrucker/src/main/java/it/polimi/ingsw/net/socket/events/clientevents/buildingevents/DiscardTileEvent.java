package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

import it.polimi.ingsw.model.components.Component;

public class DiscardTileEvent extends ClientEvent {
    private Component component;
    public DiscardTileEvent(Component component) {
        super(ClientEventType.DISCARD_TILE, false);
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public String getMessage() {
        return "I want to discard the selected tile";
    }

}
