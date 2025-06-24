package it.polimi.ingsw.net.events.serverevents.setupevents;


import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Event from server to client to notify the successful login for a player
 */
public class PlayerJoinedEvent extends ServerEvent {
    String username,message;
    private final SpaceShip spaceShip;

    public PlayerJoinedEvent(String username, SpaceShip spaceShip) {
        super(false,false);
        this.username = username;
        this.spaceShip = spaceShip;
        message = username+", you joined the game";
    }
    public String getUsername() {
        return username;
    }
    @Override
    public String getMessage() {
        return message;
    }
    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
