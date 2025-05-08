package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.util.ArrayList;

public class KeptComponentEvent extends ServerEvent{
    private String message;
    Component kept;

    public KeptComponentEvent(Component kept) {
        super(ServerEventType.KEPT_COMPONENT, false);
        this.kept = kept;
        message = "Kept component " + kept;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Component getKept() {
        return kept;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
