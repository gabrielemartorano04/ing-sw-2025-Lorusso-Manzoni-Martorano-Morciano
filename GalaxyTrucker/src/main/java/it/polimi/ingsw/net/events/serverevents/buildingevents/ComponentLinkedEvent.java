package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Event from server to client when the component is correctly linked to the spaceship
 */
public class ComponentLinkedEvent extends ServerEvent {
    String message;
    int coordx;
    int coordy;
    Component component;
    private ArrayList<Component> avComponents;

    public ComponentLinkedEvent(Component component, int x, int y, ArrayList<Component> avComponents) {
        super(false);
        coordx = x;
        coordy = y;
        this.component= component;
        this.avComponents = avComponents;
        message = "Component correctly linked";
    }

    public ArrayList<Component> getAvComponents() {
        return avComponents;
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
