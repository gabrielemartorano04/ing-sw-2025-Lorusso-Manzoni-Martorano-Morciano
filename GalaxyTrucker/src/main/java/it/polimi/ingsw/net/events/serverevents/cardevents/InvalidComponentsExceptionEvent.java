package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class InvalidComponentsExceptionEvent extends CardEvent {

    private final String message;

    public InvalidComponentsExceptionEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        super(forAll, parserState, nickname,new ArrayList<>());
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" chose an invalid component.\n";
    }
}
