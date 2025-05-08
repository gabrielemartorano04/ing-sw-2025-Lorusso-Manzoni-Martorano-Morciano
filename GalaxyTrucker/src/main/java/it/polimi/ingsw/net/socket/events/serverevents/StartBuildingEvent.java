package it.polimi.ingsw.net.socket.events.serverevents;


/**
 * Event from server to client. It's time for all the players to start building their spaceships
 */
public class StartBuildingEvent extends ServerEvent {
    String message;
    public StartBuildingEvent(String username) {
        super(ServerEventType.START_BUILDING, true);
        message = username+ " logged\nIt's time to start building";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
