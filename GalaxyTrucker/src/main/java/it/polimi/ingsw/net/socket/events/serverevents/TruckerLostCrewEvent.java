package it.polimi.ingsw.net.socket.events.serverevents;

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
}
