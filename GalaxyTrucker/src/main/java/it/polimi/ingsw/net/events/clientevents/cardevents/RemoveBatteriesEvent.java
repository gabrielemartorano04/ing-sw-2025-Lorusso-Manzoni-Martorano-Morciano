package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Event sent for the remotion of batteries
 */
public class RemoveBatteriesEvent extends ClientEvent {

    /**
     * List of battery storages' coordinates
     */
    private ArrayList<int[]> coordinatesToRemoveBatteries;

    /**
     * Nickname of the player who has to remove the batteries
     */
    private String nickname;

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
