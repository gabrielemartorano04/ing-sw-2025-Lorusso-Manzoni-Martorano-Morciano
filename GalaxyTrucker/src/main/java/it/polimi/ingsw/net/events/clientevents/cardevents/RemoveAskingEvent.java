package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Event sent for a remotion throw request
 */
public class RemoveAskingEvent extends ClientEvent {

    /**
     * List of goods' indexes to remove
     */
    private ArrayList<Integer> indexes;

    /**
     * Nickname of the player who has received the request
     */
    private String nickname;

    /**
     * Event constructor
     * @param indexes of the goods to remove
     * @param nickname of the player who has received the request
     */
    public RemoveAskingEvent(ArrayList<Integer> indexes, String nickname) {

        this.indexes = indexes;
        this.nickname = nickname;
    }

    /**
     * @return the indexes of the goods' list
     */
    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    /**
     * @return the nickname of the player who received the request
     */
    public String getNickname() {
        return nickname;
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
