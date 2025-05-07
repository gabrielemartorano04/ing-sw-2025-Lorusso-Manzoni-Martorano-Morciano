package it.polimi.ingsw.net.socket.events.serverevents;

public class ShipStateUpdateEvent extends ServerEvent {
    public ShipStateUpdateEvent(ServerEventType eventType) {
        super(ServerEventType.UPDATE_STATE);
    }
}
