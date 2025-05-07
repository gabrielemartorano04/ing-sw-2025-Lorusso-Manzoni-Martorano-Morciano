package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.Event;

import java.awt.*;
import java.util.ArrayList;

public class AvailableColorsEvent extends Event {
    private ArrayList<Color> colors;
    public AvailableColorsEvent(ArrayList<Color> colors) {
        super()
        this.colors = colors;
    }
}
