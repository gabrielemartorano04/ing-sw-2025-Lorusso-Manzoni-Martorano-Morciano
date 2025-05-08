package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;

import java.util.ArrayList;
import java.util.EnumSet;

public class AvailableColorsEvent extends ServerEvent {
    private EnumSet<PlayerColors> colors;
    public AvailableColorsEvent(EnumSet<PlayerColors> colors) {
        super(ServerEventType.SEND_MESSAGE, false);
        this.colors = colors;
        this.colors=colors;
    }

    public EnumSet<PlayerColors> getColors() {
        return colors;
    }

    @Override
    public String getMessage() {
        return "Available colors: " + colors;
    }
}
