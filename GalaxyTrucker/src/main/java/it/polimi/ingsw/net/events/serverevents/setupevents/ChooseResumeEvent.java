package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class ChooseResumeEvent extends ServerEvent {
    public ChooseResumeEvent() {
        super(ServerEventType.CHOOSE_RESUME, true);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
