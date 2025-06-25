package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents a server event indicating the successful selection of a kept component
 */
public class SuccessSelectKept extends ServerEvent {

    /**
     * Component selected from the kept list
     */
    private final Component component;

    /**
     * index of the component in the kept list
     */
    private final int index;

    /**
     * Event constructor
     * @param component selected from kept
     * @param index of the component in the kept list
     */
    public SuccessSelectKept(Component component,int index) {
        super(false);
        this.component = component;
        this.index = index;
    }

    /**
     * @return the component selected
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @return the index of the component in the kept list
     */
    public int getIndex() {
        return index;
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
