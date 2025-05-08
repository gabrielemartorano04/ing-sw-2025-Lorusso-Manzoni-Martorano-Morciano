package it.polimi.ingsw.net.socket.events.serverevents;

public class FirstConnectionEvent extends ServerEvent {

    public FirstConnectionEvent(ServerEventType eventType, boolean forAll) {
        super(ServerEventType.FIRST_CONNECTION, forAll);

    }
}
