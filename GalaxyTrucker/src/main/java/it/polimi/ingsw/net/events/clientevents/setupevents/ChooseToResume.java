package it.polimi.ingsw.net.events.clientevents.setupevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the initial choice to resume an old game
 */
public class ChooseToResume extends ClientEvent {

    /**
     * Decision to resume an old game
     */
    private boolean chooseToResume;

    /**
     * Event constructor
     * @param chooseToResume decision about resuming an old game
     */
    public ChooseToResume(boolean chooseToResume) {
        super();
        this.chooseToResume = chooseToResume;
    }

    /**
     * @return the decision about resuming an old game
     */
    public boolean getChooseToResume() {
        return chooseToResume;
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
