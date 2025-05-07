package it.polimi.ingsw.net.socket.events.serverevents;

import java.util.ArrayList;

public class LobbyEvent extends ServerEvent {
    public String message;
    private ArrayList<String> truckers;
    private int countdown;
    public LobbyEvent(ServerEventType eventType, ArrayList<String> truckers, int countdown) {
        super(ServerEventType.LOBBY);
        message = "Current players: " + truckers + "\n Countdown:" + countdown;
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
