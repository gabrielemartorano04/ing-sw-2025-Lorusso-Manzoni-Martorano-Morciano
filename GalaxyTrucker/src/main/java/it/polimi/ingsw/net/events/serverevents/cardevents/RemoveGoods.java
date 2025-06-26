package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event where goods are removed from a spaceship during gameplay
 */
public class RemoveGoods extends CardEvent{

    /**
     * List of goods to remove
     */
    private final ArrayList<Goods> goodsList;

    /**
     * Trucker's spaceship
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param nickname of the player
     * @param spaceShip of the player
     * @param goodsList list of goods to remove
     * @param truckers list of player
     */
    public RemoveGoods(boolean forAll, String nickname,SpaceShip spaceShip, ArrayList<Goods> goodsList, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.REMOVE_GOODS, nickname, truckers);
        this.goodsList = goodsList;
        this.spaceShip=spaceShip;
    }

    /**
     * @return the list of goods to remove
     */
    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    /**
     * @return the trucker's spaceship
     */
    public SpaceShip getShip(){
        return spaceShip;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers() {
        return getNickname()+" is deciding goods to remove.\n";
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
