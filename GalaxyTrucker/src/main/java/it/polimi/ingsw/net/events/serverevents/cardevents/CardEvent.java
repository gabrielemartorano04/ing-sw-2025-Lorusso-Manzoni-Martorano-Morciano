package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ParserState;

/**
 * Represents an abstract event related to a card-based interaction in the game.
 * CardEvent is a specialized type of ServerEvent used as a base class for all events
 * that specifically involve card mechanics or card-related actions
 */
public abstract class CardEvent extends ServerEvent {

    /**
     * State of the input parser
     */
    private final ParserState parserState;
    private final String nickname;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or for everyone
     * @param parserState state of the input parser
     * @param nickname of the current player
     */
    public CardEvent(boolean forAll, ParserState parserState, String nickname) {
        super(forAll);
        this.parserState = parserState;
        this.nickname = nickname;
    }

    /**
     * @return the state of the input parser
     */
    public ParserState getParserState() {
        return parserState;
    }

    /**
     * @return the nickname of the current player
     */
    public String getNickname() {
        return nickname;
    }

}
