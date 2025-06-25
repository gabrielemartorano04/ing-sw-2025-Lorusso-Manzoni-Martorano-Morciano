package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Represents a server event that occurs when a component has been successfully linked
 * to a specific position on the game board. It contains the related component,
 * the position (row and column) of the linkage, and a list of all available components.
 */
public class ComponentLinkedEvent extends ServerEvent {

    /**
     * Row of the linked position
     */
    int coordx;

    /**
     * Column of the linked position
     */
    int coordy;

    /**
     * Component linked
     */
    Component component;

    /**
     * List of all the available components
     */
    private final ArrayList<Component> avComponents;

    /**
     * Event constructor
     * @param component linked
     * @param x row of the linked position
     * @param y column of the linked position
     * @param avComponents list of all the available components
     */
    public ComponentLinkedEvent(Component component, int x, int y, ArrayList<Component> avComponents) {
        super(false);
        coordx = x;
        coordy = y;
        this.component= component;
        this.avComponents = avComponents;
    }

    /**
     * @return the list of all the available components
     */
    public ArrayList<Component> getAvComponents() {
        return avComponents;
    }

    /**
     * @return the linked component
     */
    public Component getComponent() {return component;}

    /**
     * @return the row of the linked position
     */
    public int getX() {return coordx;}

    /**
     * @return the column of the linked position
     */
    public int getY() {return coordy;}

    /**
     * @return the message associated with the confirmation of the tile's linking
     */
    @Override
    public String getMessage() {
        return "Component correctly linked";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
