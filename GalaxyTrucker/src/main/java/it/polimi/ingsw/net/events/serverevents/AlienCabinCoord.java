package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;

import java.util.ArrayList;

/**
 * Represents an event sent from the server that contains the coordinates where aliens
 * can be placed in the spaceship
 */
public class AlienCabinCoord extends ServerEvent {

    /**
     * ship layout of the player
     */
    private final SpaceShip spaceShip;

    /**
     * List of available positions for aliens
     */
    private final ArrayList<int[]> AlienCabinCoord;

    /**
     * Event constructor
     * @param alienCabinCoord list of available positions for aliens
     * @param spaceShip layout of player's ship
     */
    public AlienCabinCoord(ArrayList<int[]> alienCabinCoord, SpaceShip spaceShip) {
        super(false);
        this.AlienCabinCoord = alienCabinCoord;
        this.spaceShip = spaceShip;
    }

    /**
     * @return the list of available positions for aliens
     */
    public ArrayList<int[]> getAlienCabinCoord() {
        return AlienCabinCoord;
    }

    /**
     * @return player's ship layout
     */
    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    /**
     * @return the message associated with the list of available positions for aliens
     */
    @Override
    public String getMessage() {
        return "I send you the coordinates where you can place aliens: ";
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
