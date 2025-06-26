package it.polimi.ingsw.view;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * The EventReceiver interface defines a contract for classes that need to handle server events
 */
public interface EventReceiver {

    /**
     * Handles the reception of a server event
     * @param event the server event that has been received
     */
    void onEventReceived(ServerEvent event);
}
