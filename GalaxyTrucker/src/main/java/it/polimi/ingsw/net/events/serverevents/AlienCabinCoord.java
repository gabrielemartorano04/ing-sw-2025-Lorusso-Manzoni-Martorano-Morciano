package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

import java.util.ArrayList;

public class AlienCabinCoord extends ServerEvent {
    private ArrayList<int[]> AlienCabinCoord;
    public AlienCabinCoord(ArrayList<int[]> alienCabinCoord) {
        super(false,false);
        this.AlienCabinCoord = alienCabinCoord;
    }
    public ArrayList<int[]> getAlienCabinCoord() {
        return AlienCabinCoord;
    }
    @Override
    public String getMessage() {
        return "I send you the coordinates where you can place aliens: ";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
