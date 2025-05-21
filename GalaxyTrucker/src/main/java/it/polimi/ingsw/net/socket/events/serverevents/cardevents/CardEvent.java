package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public abstract class CardEvent extends ServerEvent {
    private final ParserState parserState;
    private final String nickname;

    public CardEvent(ServerEventType eventType, boolean forAll, ParserState parserState, String nickname) {
        super(eventType, forAll);
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
