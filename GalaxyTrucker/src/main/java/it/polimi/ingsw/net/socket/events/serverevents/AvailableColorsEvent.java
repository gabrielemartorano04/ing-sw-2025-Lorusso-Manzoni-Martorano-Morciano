package it.polimi.ingsw.net.socket.events.serverevents;

import java.awt.*;
import java.util.ArrayList;

public class AvailableColorsEvent extends ServerEvent {
    private ArrayList<String> chooseColors;
    private ArrayList<Color> colors;
    public AvailableColorsEvent(ArrayList<Color> colors) {
        super(ServerEventType.SEND_MESSAGE, false);
        this.colors = colors;
        ArrayList<String> chooseColors = new ArrayList<>();
        int i=0;
        for(Color color : colors){
            chooseColors.set(i, color.toString());
            i++;
        }
    }

    public ArrayList<Color> getColors() {
        return chooseColors;
    }

    @Override
    public String getMessage() {
        return "Available colors: " + colors;
    }
}
