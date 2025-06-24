package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class ChooseResumeEvent extends ServerEvent {
    public ChooseResumeEvent() {
        super(true,false);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getMessage() {
        return "an old game isn't finished yet, do you want to continue? (y/n)";
    }
}
