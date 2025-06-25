package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class WrongNumberOfBatteriesSelectedEvent extends CardEvent {

    private String message;

    public WrongNumberOfBatteriesSelectedEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        super(forAll, parserState, nickname,new ArrayList<>());
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
