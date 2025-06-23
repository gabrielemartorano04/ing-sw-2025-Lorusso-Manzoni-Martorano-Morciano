package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardEvent;
import it.polimi.ingsw.view.ParserState;

public class InvalidComponentsExceptionEvent extends CardEvent {

    private String message;

    public InvalidComponentsExceptionEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        //TODO sistemare ingressi del super
        super(forAll, parserState, nickname);
        this.message = message;
    }

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
