package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * This class represents an event triggered when a player selects a component.
 */
public class ComponentSelectedEvent extends ServerEvent {

    /**
     * Index of the selected component in the list
     */
    private final int index;

    /**
     * Nickname of the player who has selected the component
     */
    private final String nickname;

    /**
     * Event constructor
     * @param index of the selected component in the list
     * @param nickname of the player who has selected the component
     */
    public ComponentSelectedEvent(int index, String nickname) {
        super(true);
        this.index=index;
        this.nickname=nickname;
    }

    /**
     * @return the index of the selected component
     */
    public int getIndex() {
        return index;
    }


    /**
     * @return the nickname of the player who has selected the component
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the message associated with the selection of the component
     */
    @Override
    public String getMessage() {
        return "Component selected";
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
