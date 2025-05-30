package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class KeptComponentEvent extends ServerEvent {
    private String message;
    Component kept;
    private ArrayList<Component> avComponents;
    public KeptComponentEvent(Component kept,ArrayList<Component> avComponents) {
        super(ServerEventType.KEPT_COMPONENT, false);
        this.kept = kept;
        message = "Kept component " + kept;
        this.avComponents = avComponents;
    }

    public ArrayList<Component> getAvComponents() {
        return avComponents;
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
