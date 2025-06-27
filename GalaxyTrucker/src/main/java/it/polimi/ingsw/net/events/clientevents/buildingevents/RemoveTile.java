package it.polimi.ingsw.net.events.clientevents.buildingevents;

import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * client action that should delete a component from client's ship in order to
 * try to make the ship legal
 */
public class RemoveTile extends ClientEvent {

    /**
     * row of the component to be removed
     */
    private final int x;

    /**
     * column of the component to be removed
     */
    private final int y;

    /**
     * constructs the class
     * @param x row decided by the client
     * @param y column decided by the client
     */
    public RemoveTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the column decided by the client
     */
    public int getY() {
        return y;
    }

    /**
     * @return the row decided by the client
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return the event to send to the client or to all clients
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this,client);
    }
}
