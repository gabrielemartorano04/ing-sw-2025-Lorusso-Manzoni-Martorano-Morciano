package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;

import java.util.EnumSet;

public class FirstConnectionEvent extends ServerEvent {
    private final EnumSet<PlayerColors> colors;
    private final String message;
    public FirstConnectionEvent(EnumSet<PlayerColors> colors, String message) {
        super(ServerEventType.SEND_MESSAGE, false);
        this.colors=colors;
        this.message = message;
    }

    public EnumSet<PlayerColors> getColors() {
        return colors;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
