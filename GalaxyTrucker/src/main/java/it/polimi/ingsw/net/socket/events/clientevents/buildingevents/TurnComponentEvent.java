package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class TurnComponentEvent extends ClientEvent {
    private Component component;
    public TurnComponentEvent(Component component) {
        super(ClientEventType.TURN_COMPONENT);
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
