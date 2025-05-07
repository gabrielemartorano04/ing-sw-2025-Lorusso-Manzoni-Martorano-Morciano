package it.polimi.ingsw.net.socket.events.serverevents;

import java.awt.*;
import java.util.ArrayList;

public class AvailableColorsEvent extends ServerEvent {
    private ArrayList<Color> colors;
    public AvailableColorsEvent(ArrayList<Color> colors) {
        super(ServerEventType.SEND_MESSAGE, false);
        this.colors = colors;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    @Override
    public String getMessage() {
        return "Available colors: " + colors;
    }
}
