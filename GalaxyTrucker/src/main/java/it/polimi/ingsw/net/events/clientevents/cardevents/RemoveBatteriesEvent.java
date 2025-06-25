package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Represents an event sent by a client to the server indicating a player's action
 * to remove batteries from specific locations.
 */
public class RemoveBatteriesEvent extends ClientEvent {

    /**
     * List of battery storages' coordinates
     */
    private final ArrayList<int[]> coordinatesToRemoveBatteries;

    /**
     * Nickname of the player who has to remove the batteries
     */
    private final String nickname;

    /**
     * Event constructor
     * @param coordinatesToRemoveBatteries list of coordinates from where to remove batteries
     * @param nickname of the player who has to remove the batteries
     */
    public RemoveBatteriesEvent(ArrayList<int[]> coordinatesToRemoveBatteries,String nickname) {
        this.coordinatesToRemoveBatteries = coordinatesToRemoveBatteries;
        this.nickname = nickname;
    }

    /**
     * @return the list of coordinates from where to remove the batteries
     */
    public ArrayList<int[]> getCoordinatesToRemoveBatteries() {
        return coordinatesToRemoveBatteries;
    }

    /**
     * @return the name of the player who has to remove the batteries
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
