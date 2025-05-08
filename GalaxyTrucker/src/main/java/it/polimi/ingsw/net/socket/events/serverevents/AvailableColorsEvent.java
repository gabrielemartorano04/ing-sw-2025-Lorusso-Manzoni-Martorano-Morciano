package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.client.ServerEventVisitor;

import java.util.ArrayList;
import java.util.EnumSet;

public class AvailableColorsEvent extends ServerEvent {
    private final EnumSet<PlayerColors> colors;
    private final String message;
    public AvailableColorsEvent(EnumSet<PlayerColors> colors, String message) {
        super(ServerEventType.CHOOSE_COLOURS, false);
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

    @Override
    public void call(ServerEventVisitor visitor){
        visitor.visit(this);
    }
}
