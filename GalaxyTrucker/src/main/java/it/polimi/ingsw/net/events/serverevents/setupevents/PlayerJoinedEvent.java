package it.polimi.ingsw.net.events.serverevents.setupevents;


import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event triggered when a player joins the game
 */
public class PlayerJoinedEvent extends ServerEvent {

    /**
     * Nickname of the player that joined the game
     */
    String username;

    /**
     * Spaceship of the player that joined the game
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param username of the player that joined the game
     * @param spaceShip of the player that joined the game
     */
    public PlayerJoinedEvent(String username, SpaceShip spaceShip) {
        super(false);
        this.username = username;
        this.spaceShip = spaceShip;
    }

    /**
     * @return the nickname of the player that joined the game
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the spaceship of the player that joined the game
     */
    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    /**
     * @return the message associated with the joining of a player
     */
    @Override
    public String getMessage() {
        return username+", you joined the game";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
