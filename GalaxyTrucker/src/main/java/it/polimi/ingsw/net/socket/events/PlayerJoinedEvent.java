package it.polimi.ingsw.net.socket.events;

public class PlayerJoinedEvent extends Event {
    public PlayerJoinedEvent() {
        super(EventType.PLAYER_JOINED);
    }
}
