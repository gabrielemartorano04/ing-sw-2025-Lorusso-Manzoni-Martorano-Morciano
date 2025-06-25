package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

/**
 * Represents an event triggered by the activation of double engines in the spaceship.
 */
public class ActivateEnginesEvent extends ClientEvent {

    /**
     * Decision about activation
     */
    private boolean decision;

    /**
     * List of coordinates for double engines and battery storages in the spaceship
     */
    private ArrayList<int[]> coordinates, batteryCoordinates;

    /**
     * Nickname of the actual player
     */
    private String nickname;

    /**
     * Event constructor
     * @param decision taken about the activation
     * @param coordinates of the double engines in the spaceship
     * @param batteryCoordinates of the battery storages in the spaceship
     * @param nickname of the actual player
     */
    public ActivateEnginesEvent(boolean decision, ArrayList<int[]> coordinates, ArrayList<int[]> batteryCoordinates,String nickname) {
        this.decision = decision;
        this.coordinates = coordinates;
        this.batteryCoordinates = batteryCoordinates;
        this.nickname = nickname;
    }

    /**
     * @return the decision taken about activation
     */
    public boolean getDecision() {
        return decision;
    }

    /**
     * @return the coordinates' list of the double engines
     */
    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }

    /**
     * @return the coordinates' list of the battery storages
     */
    public ArrayList<int[]> getBatteryCoordinates() {
        return batteryCoordinates;
    }

    /**
     * @return the nickname of the player
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
