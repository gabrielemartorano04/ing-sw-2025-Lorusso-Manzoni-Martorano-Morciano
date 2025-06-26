package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event indicating that the removal of a card or action
 * associated with a spaceship has been confirmed
 */
public class RemotionConfirmed extends CardEvent{

    /**
     * Trucker's spaceship
     */
    private final SpaceShip spaceShip;

    /**
     * Event constructor
     * @param parserState state of the parser
     * @param nickname of the player
     * @param spaceShip of the player
     */
    public RemotionConfirmed(ParserState parserState , String nickname,SpaceShip spaceShip) {
        super(true, parserState, nickname, new ArrayList<>());
        this.spaceShip=spaceShip;
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
    public String getInfoForOthers(){
        return getNickname()+" received a remotion confirm";
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
