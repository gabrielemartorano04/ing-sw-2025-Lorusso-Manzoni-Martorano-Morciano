package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event where batteries are removed from a spaceship as part of a card-based action
 */
public class RemoveBatteries extends CardEvent{

    /**
     * Number of batteries to remove
     */
    private final int batteriesToRemove;

    /**
     * trucker's spaceship
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param nickname of the player
     * @param spaceShip of the player
     * @param batteriesToRemove number of batteries to remove
     * @param truckers list of players
     */
    public RemoveBatteries(boolean forAll, String nickname, SpaceShip spaceShip, int batteriesToRemove, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.REMOVE_BATTERIES, nickname, truckers);
        this.batteriesToRemove = batteriesToRemove;
        this.spaceShip=spaceShip;
    }

    /**
     * @return the number of batteries to remove
     */
    public int getBatteriesToRemove() {
        return batteriesToRemove;
    }

    /**
     * @return the trucker's spaceship
     */
    public SpaceShip getShip() {
        return spaceShip;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" is deciding where to remove batteries";
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
