package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event sent by the client to the server indicating that a specific
 * component should be turned
 */
public class TurnComponentEvent extends ClientEvent {

    /**
     * Component to turn
     */
    private final Component component;

    /**
     * Event constructor
     * @param component to turn
     */
    public TurnComponentEvent(Component component) {
        this.component = component;
    }

    /**
     * @return the component to turn
     */
    public Component getComponent() {
        return component;
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
