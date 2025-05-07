package it.polimi.ingsw.net.socket.events.clientevents;

/**
 * Event client to server
 */
public class EndBuildingEvent extends ClientEvent {
    String message;
    public EndBuildingEvent(){
        super(ClientEventType.END_BUILDING);
        message="my spaceship is done";
    }
    @Override
    public String getMessage(){
        return message;
    }
}
