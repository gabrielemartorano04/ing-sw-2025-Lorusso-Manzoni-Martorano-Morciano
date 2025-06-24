package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

public class WaitOthersStartFlightEvent extends ServerEvent {
    public WaitOthersStartFlightEvent() {
        super(false,false);

    }
    @Override
    public String getMessage(){
        return "Waiting for other to start flight";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
