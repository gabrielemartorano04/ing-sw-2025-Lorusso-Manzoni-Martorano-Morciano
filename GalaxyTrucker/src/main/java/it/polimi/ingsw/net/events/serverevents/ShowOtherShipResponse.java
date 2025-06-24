package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;

/**
 * Represents a response event from the server that provides a spaceship layout
 * and the nickname of another player to be shown to the client
 */
public class ShowOtherShipResponse extends ServerEvent {

    /**
     * Grid of components composing the spaceship
     */
    private final Component[][] spaceship;

    /**
     * Nickname of the player
     */
    private final String nickname;

    /**
     * Event constructor
     * @param spaceship of the player
     * @param nickname of the player
     */
    public ShowOtherShipResponse(Component[][] spaceship, String nickname) {
        super(false);
        this.spaceship = spaceship;
        this.nickname = nickname;
    }

    /**
     * @return the grid of components composing the spaceship
     */
    public Component[][] getSpaceship() {
        return spaceship;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the message associated with the showing of the spaceship
     */
    @Override
    public String getMessage() {
        return "Show "+nickname+"'s ship";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
