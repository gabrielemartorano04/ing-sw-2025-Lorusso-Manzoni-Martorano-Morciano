package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for linking tiles to the spaceship during building phase
 */
public class LinkTileEvent extends ClientEvent {

    /**
     * Component to link to the spaceship
     */
    private final Component component;

    /**
     * Index of the component in the list of available components
     */
    private int index;

    /**
     * Position row and column where to link the component in the spaceship
     */
    private int x,y;

    /**
     * Event Constructor
     * @param index index of the component in the list of available components
     * @param x row where to link the component in the spaceship
     * @param y column where to link the component in the spaceship
     * @param component to link to the spaceship
     */
    public LinkTileEvent(int index, int x, int y, Component component) {
        this.component = component;
        this.index = index;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the componen to link
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @return the row where to link in the spaceship
     */
    public int getX() {
        return x;
    }

    /**
     * @return the column where to link in the spaceship
     */
    public int getY() {
        return y;
    }

    /**
     * @return the index of the tile in the list of available components
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the massage associate with the link of the component
     */
    @Override
    public String getMessage() {
        return "I want to link the selected component in position ("+x+", "+y+")";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this,client);
    }
}
