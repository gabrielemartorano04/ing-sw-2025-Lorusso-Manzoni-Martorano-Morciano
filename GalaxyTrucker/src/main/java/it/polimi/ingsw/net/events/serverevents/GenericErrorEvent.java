package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

public class GenericErrorEvent extends ServerEvent{
    private String message;

    public GenericErrorEvent(String message, boolean forAll) {
        super(forAll,false);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
