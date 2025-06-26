package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event where a player decides whether to land on a planet
 */
public class PlanetDecision extends CardEvent {

    /**
     * List card's planets
     */
    private final ArrayList<Trucker> planetsOccupied;

    /**
     * Spaceship of the player
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param planetsOccupied list card's planets
     * @param nickname of the player
     * @param spaceShip of the player
     * @param truckers list of players
     */
    public PlanetDecision(boolean forAll, ArrayList<Trucker> planetsOccupied, String nickname,SpaceShip spaceShip, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.PLANET_DECISION,nickname,truckers);
        this.planetsOccupied = planetsOccupied;
        this.spaceShip=spaceShip;
    }

    /**
     * @return the trucker's spaceship
     */
    public SpaceShip getShip(){
        return spaceShip;
    }

    /**
     * @return the list of card's planets
     */
    public ArrayList<Trucker> getPlanetsOccupied() {
        return planetsOccupied;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" is deciding if landing on a planet or not";
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
