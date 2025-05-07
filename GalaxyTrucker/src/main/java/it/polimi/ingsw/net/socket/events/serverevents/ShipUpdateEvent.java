package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

public class ShipUpdateEvent extends ServerEvent {

    private String message;

    public ShipUpdateEvent(ServerEventType eventType) {
        super(ServerEventType.UPDATE_STATE);
        message = "Ship updated";
    }

    public String getMessage() {
        return message;
    }

    public Component[][] getShip() {

    }
}
