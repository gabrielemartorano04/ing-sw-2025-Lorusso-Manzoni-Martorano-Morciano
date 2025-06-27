package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * This event is sent from the server when a player turned the hourglass for the first time,
 * so the player's building phase ended.
 */
public class WaitOthersHourglassEvent extends ServerEvent {

    /**
     * Nickname of the player that turned the hourglass
     */
    private final String nickname;

    /**
     * Class constructor
     * @param nickname nickname of the player that turned the hourglass
     */
    public WaitOthersHourglassEvent(String nickname) {
        super(true);
        this.nickname = nickname;
    }

    /**
     * @return nickname of the player that turned the hourglass
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method is used by the client to manage this server event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
