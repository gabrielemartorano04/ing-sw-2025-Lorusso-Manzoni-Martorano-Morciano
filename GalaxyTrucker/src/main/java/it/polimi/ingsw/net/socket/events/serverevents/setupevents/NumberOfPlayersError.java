package it.polimi.ingsw.net.socket.events.serverevents.setupevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class NumberOfPlayersError extends ServerEvent {
    public NumberOfPlayersError(){
        super(ServerEventType.PLAYERS_NUMBER_ERROR, false);

    }
    @Override
    public String getMessage(){
        return "Number of players error. It has to be between 2 and 4!";
    }
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

}
