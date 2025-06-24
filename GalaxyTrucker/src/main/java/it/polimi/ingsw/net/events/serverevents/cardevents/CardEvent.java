package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ParserState;

public abstract class CardEvent extends ServerEvent {
    private final ParserState parserState;
    private final String nickname;

    public CardEvent(boolean forAll, ParserState parserState, String nickname) {
        super(forAll,false);
        this.parserState = parserState;
        this.nickname = nickname;
    }

    public ParserState getParserState() {
        return parserState;
    }

    public String getNickname() {
        return nickname;
    }

}
