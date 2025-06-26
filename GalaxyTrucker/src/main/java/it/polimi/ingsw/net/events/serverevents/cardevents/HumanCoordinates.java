package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event in which a player selects crew members coordinates
 * during the game's progression
 */
public class HumanCoordinates extends CardEvent {

    /**
     * Spaceship of the trucker
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param nickname of the player
     * @param spaceShip of the trucker
     * @param truckers list of players
     */
    public HumanCoordinates(boolean forAll, String nickname,SpaceShip spaceShip, ArrayList<Trucker> truckers ) {
        super(forAll, ParserState.HUMAN_COORD, nickname, truckers);
        this.spaceShip=spaceShip;
    }

    /**
     * @return the spaceship of the trucker
     */
    public SpaceShip getShip(){
        return spaceShip;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" is choosing human coordinates ";
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
