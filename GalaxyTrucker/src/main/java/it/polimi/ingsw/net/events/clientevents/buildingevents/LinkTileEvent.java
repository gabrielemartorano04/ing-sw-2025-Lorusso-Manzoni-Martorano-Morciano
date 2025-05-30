package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

/**
 * Event from client to server. The player wants to link the selected component to the spaceship.
 * The player gives the coordinates.
 * The attribute index represents the index between 0 and 151 of the component that the trucker has currently as selected
 * so that the server can try to link it
 */
public class LinkTileEvent extends ClientEvent {
    private String  message;
    private final Component component;
    private int index;
    private int x,y;
    public LinkTileEvent(int index, int x, int y, Component component) {
        this.component = component;
        this.index = index;
        this.x = x;
        this.y = y;
        message="I want to link the selected component in position ("+x+", "+y+")";
    }

    public Component getComponent() {
        return component;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
