package it.polimi.ingsw.net.socket.events.clientevents;

public class SendCoordEvent extends ClientEvent {
    private int x;
    private int y;
    public SendCoordEvent( int x,  int y) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.x = x;
        this.y = y;
    }
}
