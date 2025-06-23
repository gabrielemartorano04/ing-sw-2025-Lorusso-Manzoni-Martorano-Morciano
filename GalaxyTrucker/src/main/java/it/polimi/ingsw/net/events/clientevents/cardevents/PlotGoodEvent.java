package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent to plot goods that has to be deleted
 */
public class PlotGoodEvent extends ClientEvent {

    /**
     * Decision about remotion of the good
     */
    private boolean decision;

    /**
     * Coordinates of the load that contains the good
     */
    private int[] coordinates;

    /**
     * Position of the goods inside the load
     */
    private int positionInLoad;

    /**
     * Nickname of the player who has to
     */
    private String nickname;

    /**
     * Event constructor
     * @param decision about the remotion of the good
     * @param coordinates of the load
     * @param positionInLoad position of the good inside the load
     * @param nickname of the player who  ahs to decide
     */
    public PlotGoodEvent(boolean decision, int[] coordinates, int positionInLoad, String nickname) {

        this.decision = decision;
        this.coordinates = coordinates;
        this.positionInLoad = positionInLoad;
        this.nickname = nickname;
    }

    /**
     * @return the decision about remotion of the good
     */
    public boolean getDecision() {
        return decision;
    }


    /**
     * @return of the load containing the good
     */
    public int[] getCoordinates() {
        return coordinates;
    }

    /**
     * @return of the position of the good in the load
     */
    public int getPositionInLoad() {
        return positionInLoad;
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
