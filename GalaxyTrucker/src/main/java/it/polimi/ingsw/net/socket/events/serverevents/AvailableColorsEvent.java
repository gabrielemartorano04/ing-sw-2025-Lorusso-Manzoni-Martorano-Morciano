package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;

import java.awt.*;
import java.util.ArrayList;

public class AvailableColorsEvent extends ServerEvent {
    private ArrayList<PlayerColors> colors;
    public AvailableColorsEvent(ArrayList<PlayerColors> colors) {
        super(ServerEventType.SEND_MESSAGE, false);
        this.colors = colors;
        this.colors=colors;
    }

    public ArrayList<PlayerColors> getColors() {
        return colors;
    }

    @Override
    public String getMessage() {
        return "Available colors: " + colors;
    }
}
