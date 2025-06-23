package it.polimi.ingsw.net.events.clientevents.buildingevents;


import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for keeping tiles during building phase
 */
public class KeepTileEvent extends ClientEvent {

    /**
     * Component to keep
     */
    private Component component;

    /**
     * Event constructor
     * @param component to keep
     */
    public KeepTileEvent(Component component) {
        this.component = component;
    }

    /**
     * @return the component to keep
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @return message associated with the keep of the component
     */
    @Override
    public String getMessage() {
        return "I want to keep the selected tile";
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    public ServerEvent call(VirtualServer visitor, Client client) {
          return visitor.visit(this, client);
    }
}
