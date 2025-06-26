package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an abstract event related to a card-based action in the game
 */
public abstract class CardEvent extends ServerEvent {

    /**
     * Current state of the parser during the game's progression
     */
    private final ParserState parserState;

    /**
     * Nickname of the player
     */
    private final String nickname;

    /**
     * List of playing trucker
     */
    private final ArrayList<Trucker> truckers;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param parserState state of the parser
     * @param nickname of the player
     * @param truckers list of player
     */
    public CardEvent(boolean forAll, ParserState parserState, String nickname, ArrayList<Trucker> truckers) {
        super(forAll);
        this.parserState = parserState;
        this.nickname = nickname;
        this.truckers = truckers;
    }

    /**
     * @return the list of player
     */
    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }

    /**
     * @return the state of the parser
     */
    public ParserState getParserState() {
        return parserState;
    }


    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

}
