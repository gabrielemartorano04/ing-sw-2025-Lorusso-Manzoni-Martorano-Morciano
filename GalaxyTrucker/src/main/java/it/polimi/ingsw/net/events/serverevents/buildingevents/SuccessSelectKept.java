package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class SuccessSelectKept extends ServerEvent {

    private Component component;
    private int index;
    public SuccessSelectKept(Component component,int index) {
        super(ServerEventType.SUCCESS_KEPT, false);
        this.component = component;
        this.index = index;
    }

    public Component getComponent() {
        return component;
    }

    public int getIndex() {
        return index;
    }
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
