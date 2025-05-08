package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;

import java.util.ArrayList;

public class LobbyEvent extends ServerEvent {
    public String message;
    public int countdown;
    private ArrayList<String> truckers;
    public LobbyEvent( ArrayList<String> truckers, int countdown) {
        super(ServerEventType.LOBBY, true);
        message = "Current players: " + truckers;
        this.countdown = countdown;
        this.truckers = truckers;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<String> getTruckers() {
        return truckers;
    }

    public int getCountdown() {return countdown;}
}
