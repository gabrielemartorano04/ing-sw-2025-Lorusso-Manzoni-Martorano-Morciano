package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * The TurnedComponentSuccessEvent class represents a server event triggered
 * when a component has been successfully rotated
 */
public class TurnedComponentSuccessEvent extends ServerEvent {

    /**
     * Component that has been rotated
     */
    private final Component component;

    /**
     * Event constructor
     * @param component that has been rotated
     */
    public TurnedComponentSuccessEvent(Component component) {
        super(false);
        this.component = component;
    }

    /**
     * @return the component that has been rotated
     */
    public Component getComponent() {
        return component;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
