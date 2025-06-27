package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class ShipNotLegal extends ServerEvent {

    /**
     * spaceship of the client updated after the remove, but still not valid
     */
    private final SpaceShip ship;

    /**
     * Event constructor
     *
     * @param ship ship of the current player
     */
    public ShipNotLegal(SpaceShip ship) {
        super(false);
        this.ship = ship;
    }

    /**
     * @return ship of the client
     */
    public SpaceShip getShip() {
        return ship;
    }

    /**
     *
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
