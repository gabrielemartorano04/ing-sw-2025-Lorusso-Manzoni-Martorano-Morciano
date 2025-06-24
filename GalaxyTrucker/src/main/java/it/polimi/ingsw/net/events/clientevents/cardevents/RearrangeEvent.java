package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the rearrangement of the goods between loads
 */
public class RearrangeEvent extends ClientEvent {

    /**
     * Coordinates of the initial end final load
     */
    private int[] coord1, coord2;

    /**
     * Internal position of the initial and final good
     */
    private int value1, value2;

    /**
     * Decision about to continue the rearrangement or to stop
     */
    private boolean stop;

    /**
     * Nickname of the player who wants to rearrange
     */
    private String nickname;

    /**
     * Event constructor
     * @param coord1 initial position of the load
     * @param coord2 final position of the load
     * @param value1 initial internal position of the good
     * @param value2 final internal position of the good
     * @param stop decision about the rearrangement
     * @param nickname nickname of the player who wants to rearrange
     */
    public RearrangeEvent(int[] coord1, int[] coord2, int value1, int value2, boolean stop,String nickname) {

        this.coord1 = coord1;
        this.coord2 = coord2;
        this.value1 = value1;
        this.value2 = value2;
        this.stop = stop;
        this.nickname = nickname;
    }

    /**
     * @return the initial coordinate of the load
     */
    public int[] getCoord1() {
        return coord1;
    }

    /**
     * @return the final coordinates of the load
     */
    public int[] getCoord2() {
        return coord2;
    }

    /**
     * @return the internal position of the good in the load
     */
    public int getValue1() {
        return value1;
    }

    /**
     * @return the internal position of the good in the load
     */
    public int getValue2() {
        return value2;
    }

    /**
     * @return the decision about to continue the rearrangement or not
     */
    public boolean getStop() {
        return stop;
    }

    /**
     * @return the nickname of the player who wants to rearrange
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
