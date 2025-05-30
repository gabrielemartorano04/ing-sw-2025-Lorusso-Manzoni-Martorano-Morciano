package it.polimi.ingsw.net.events.serverevents.flightevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

public class TruckerLostCrewEvent extends ServerEvent {
    private String message;
    private int lostCrew;

    public TruckerLostCrewEvent(int lostCrew) {
        super(ServerEventType.LOST_CREW, false);
        this.lostCrew = lostCrew;
        message = "Trucker lost " + lostCrew + " crew members";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getLostCrew() {
        return lostCrew;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
