package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardEvent;
import it.polimi.ingsw.view.ParserState;

public class InvalidPositionExceptionEvent extends CardEvent {

    private String message;

    public InvalidPositionExceptionEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        //TODO correggi ingressi super
        super(ServerEventType.INVALID_POSITION_ERROR, forAll, parserState, nickname);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers() {
        return getNickname()+" chose an invalid position.\n";
    }
}
