package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class InvalidGoodChosenEvent extends CardEvent {

    private final String message;

    public InvalidGoodChosenEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        super(forAll, parserState, nickname,new ArrayList<>());
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getInfoForOthers() {
        return getNickname()+" chose an invalid good.\n";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
