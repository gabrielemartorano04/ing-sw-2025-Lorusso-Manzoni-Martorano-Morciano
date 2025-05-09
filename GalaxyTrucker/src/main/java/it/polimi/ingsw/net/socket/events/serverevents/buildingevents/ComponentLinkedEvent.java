package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

/**
 * Event from server to client when the component is correctly linked to the spaceship
 */
public class ComponentLinkedEvent extends ServerEvent {
    String message;
    int coordx;
    int coordy;
    Component component;
    public ComponentLinkedEvent(Component component, int x, int y) {
        super(ServerEventType.COMPONENT_LINKED, false);
        coordx = x;
        coordy = y;
        this.component= component;
        message = "Component correctly linked";
    }
    @Override
    public String getMessage() {
        return message;
    }
    public Component getComponent() {return component;}
    public int getX() {return coordx;}
    public int getY() {return coordy;}

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
