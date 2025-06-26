package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event where a player decides on the goods to gain in the game.
 */
public class PlotGood extends CardEvent{

    /**
     * Good to gain
     */
    private final Goods good;

    /**
     * Spaceship of the player
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param nickname of the player
     * @param spaceShip of the player
     * @param good to gain
     * @param truckers list of players
     */
    public PlotGood(boolean forAll, String nickname,SpaceShip spaceShip, Goods good, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.PLOT_GOOD, nickname, truckers);
        this.good = good;
        this.spaceShip=spaceShip;
    }

    /**
     * @return the good to gain
     */
    public Goods getGood() {
        return good;
    }

    /**
     * @return the player's spaceship
     */
    public SpaceShip getShip(){
        return spaceShip;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" is deciding which goods to gain\n";
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
