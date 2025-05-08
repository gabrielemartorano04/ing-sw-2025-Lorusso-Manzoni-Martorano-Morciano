package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class TruckerLostCrewEvent extends ServerEvent{
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
