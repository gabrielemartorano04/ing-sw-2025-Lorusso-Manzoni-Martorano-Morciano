package it.polimi.ingsw.net.socket.events.serverevents;

import java.util.ArrayList;

public class CurrentPlayersEvent extends ServerEvent {
    public String message;
    private ArrayList<String> truckers;
    private int countdown;
    public CurrentPlayersEvent(ServerEventType eventType, ArrayList<String> truckers, int countdown) {
        super(eventType);
        message = "Current players: ";
        this.truckers = truckers;
        this.countdown = countdown;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getTruckers() {
        return truckers;
    }

    public int getCountdown() {
        return countdown;
    }
}
