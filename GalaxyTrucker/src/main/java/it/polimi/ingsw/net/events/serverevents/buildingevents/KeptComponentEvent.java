package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * The KeptComponentEvent class extends the ServerEvent class and represents an event
 * triggered when a specific component is kept
 */
public class KeptComponentEvent extends ServerEvent {

    /**
     * Component kept from the available ones
     */
    Component kept;

    /**
     * List of available components
     */
    private final ArrayList<Component> avComponents;

    /**
     * Event constructor
     * @param kept component kept from the available ones
     * @param avComponents list of available components
     */
    public KeptComponentEvent(Component kept,ArrayList<Component> avComponents) {
        super(false);
        this.kept = kept;
        this.avComponents = avComponents;
    }

    /**
     * @return the list of available components
     */
    public ArrayList<Component> getAvComponents() {
        return avComponents;
    }

    /**
     * @return the component kept
     */
    public Component getKept() {
        return kept;
    }

    /**
     * @return the message associated with the keeping of the tile
     */
    @Override
    public String getMessage() {
        return "Kept component " + kept;
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
