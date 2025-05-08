package it.polimi.ingsw.net.socket.events.serverevents;

public class SetupCompleteEvent extends ServerEvent {
    private String message;
    public SetupCompleteEvent() {
        super(ServerEventType.SETUP_COMPLETE, true);
        message="All players ready";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
