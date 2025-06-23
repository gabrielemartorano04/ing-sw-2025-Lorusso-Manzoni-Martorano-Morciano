package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event sent for the withdrawal of a player
 */
public class WithdrawRequest extends ClientEvent {

    /**
     * Nickname of the withdrawal player
     */
    private final String nickname;

    /**
     * Decision taken about the possibility to withdraw
     */
    private final boolean choice;

    /**
     * Event constructor
     * @param nickname of the withdrawal player
     * @param choice decision about withdrawal
     */
    public WithdrawRequest(String nickname,boolean choice) {
        this.nickname = nickname;
        this.choice = choice;
    }

    /**
     * @return the nickname of the withdrawal player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the decision taken about withdrawal by the player
     */
    public boolean getChoice(){
        return choice;
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
