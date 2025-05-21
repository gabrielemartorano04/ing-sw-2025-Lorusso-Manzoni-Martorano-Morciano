package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class TurnedComponentSuccessEvent extends ServerEvent {
    private Component component;
    public TurnedComponentSuccessEvent(Component component) {
        super(ServerEventType.SEND_MESSAGE,false);
        this.component = component;
    }
    public Component getComponent() {
        return component;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
