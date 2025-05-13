package it.polimi.ingsw.net.socket.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

/**
 * Event from client to server. The player wants to link the selected component to the spaceship.
 * The player gives the coordinates.
 */
public class LinkTileEvent extends ClientEvent {
    private String  message;
    private Component component;
    private int x,y;
    public LinkTileEvent(Component component, int x, int y) {
        super(ClientEventType.LINK_TILE, false);
        this.component = component;
        this.x = x;
        this.y = y;
        message="I want to link the selected component in position ("+x+", "+y+")";
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
