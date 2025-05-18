package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class WaitOthersStartFlightEvent extends ServerEvent {
    public WaitOthersStartFlightEvent() {
        super(ServerEventType.SEND_MESSAGE,false);

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
