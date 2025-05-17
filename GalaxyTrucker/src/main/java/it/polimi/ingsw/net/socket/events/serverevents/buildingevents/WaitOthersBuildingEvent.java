package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class WaitOthersBuildingEvent extends ServerEvent {
    private String message;

    public WaitOthersBuildingEvent() {
        super(ServerEventType.WAIT_OTHERS_BUILDING, false);
        message = "Waiting for others players to end building";
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
