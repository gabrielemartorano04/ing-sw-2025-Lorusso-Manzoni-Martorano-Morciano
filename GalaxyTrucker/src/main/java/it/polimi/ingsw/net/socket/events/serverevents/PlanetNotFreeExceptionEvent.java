package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;
import it.polimi.ingsw.view.ParserState;

public class PlanetNotFreeExceptionEvent extends CardEvent {

    private String message;

    public PlanetNotFreeExceptionEvent(String message, boolean forAll, ParserState parserState, String nickname) {
        //TODO inserire i valori corretti nel super
        super(ServerEventType.PLANET_NOT_FREE_ERROR, forAll, parserState, nickname);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfoForOthers() {
        return getNickname()+" chose a planet that is not free.\n";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
