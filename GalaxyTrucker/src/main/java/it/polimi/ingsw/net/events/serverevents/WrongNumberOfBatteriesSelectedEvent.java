package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardEvent;
import it.polimi.ingsw.view.ParserState;

public class WrongNumberOfBatteriesSelectedEvent extends CardEvent {

    private String message;

    public WrongNumberOfBatteriesSelectedEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        super(forAll, parserState, nickname);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfoForOthers() {
        return getNickname()+" chose an invalid number of batteries.\n";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
