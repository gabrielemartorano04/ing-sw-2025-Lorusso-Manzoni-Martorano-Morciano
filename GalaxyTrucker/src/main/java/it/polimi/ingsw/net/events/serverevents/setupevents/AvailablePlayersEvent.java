package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public class AvailablePlayersEvent extends ServerEvent {

    ArrayList<PlayerColors> colors = new ArrayList<>();
    ArrayList<String> nicknames = new ArrayList<>();


    public AvailablePlayersEvent(ArrayList<PlayerColors> colors, ArrayList<String> nicknames) {
        super(false);
        this.colors = colors;
        this.nicknames = nicknames;
    }

    public ArrayList<PlayerColors> getColors() {
        return colors;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor){
        visitor.visit(this);
    }
}
