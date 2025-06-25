package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public abstract class CardEvent extends ServerEvent {
    private final ParserState parserState;
    private final String nickname;
    private final ArrayList<Trucker> truckers;

    public CardEvent(boolean forAll, ParserState parserState, String nickname, ArrayList<Trucker> truckers) {
        super(forAll);
        this.parserState = parserState;
        this.nickname = nickname;
        this.truckers = truckers;
    }

    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }

    public ParserState getParserState() {
        return parserState;
    }

    public String getNickname() {
        return nickname;
    }

}
