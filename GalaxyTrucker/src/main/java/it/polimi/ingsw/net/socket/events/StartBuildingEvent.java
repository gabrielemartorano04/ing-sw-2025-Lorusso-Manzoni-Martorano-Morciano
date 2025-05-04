package it.polimi.ingsw.net.socket.events;

/**
 * Event from server to client. It's time for all the players to start building their spaceships
 */
public class StartBuildingEvent extends Event{
    String message;
    public StartBuildingEvent() {
        super(EventType.START_BUILDING);
        message = "It's time to start building";
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
