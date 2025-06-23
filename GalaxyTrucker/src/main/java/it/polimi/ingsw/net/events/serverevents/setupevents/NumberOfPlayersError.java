package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class NumberOfPlayersError extends ServerEvent {
    public NumberOfPlayersError(){
        super(false);

    }
    @Override
    public String getMessage(){
        return "Number of players error. It has to be between 2 and 4!";
    }
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        //visitor.visit(this);
    }

}
