package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Event sent for the remotion of crew members
 */
public class HumanCoordEvent extends ClientEvent {

    /**
     * List of cabins' coordinates from where to remove crew
     */
    private final ArrayList<int[]> humanCoord;

    /**
     * nickname of the player who has to decide
     */
    private final String nickname;

    /**
     * Event constructor
     * @param humanCoord List of cabins' coordinates from where to remove crew
     * @param nickname nickname of the player who has to decide
     */
    public HumanCoordEvent(ArrayList<int[]> humanCoord, String nickname) {
        this.humanCoord = humanCoord;
        this.nickname = nickname;
    }

    /**
     * @return th list of cabins' coordinates from where to remove crew
     */
    public ArrayList<int[]> getHumanCoord() {
        return humanCoord;
    }

    /**
     * @return the nickname of the player who has to decide
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
