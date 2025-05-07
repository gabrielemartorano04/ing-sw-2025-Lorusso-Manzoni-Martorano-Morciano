package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;

import java.util.ArrayList;

public class LobbyEvent extends ServerEvent {
    public String message;
    private ArrayList<String> truckers;
    private int countdown;
    public LobbyEvent( ArrayList<String> truckers, int countdown) {
        super(ServerEventType.LOBBY);
        message = "Current players: " + truckers + "\n Countdown:" + countdown;
        this.truckers = truckers;
        this.countdown = countdown;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<String> getTruckers() {
        return truckers;
    }

    public int getCountdown() {
        return countdown;
    }

    public static void main(String[] args) {
        Trucker trucker1=new Trucker("frenk", PlayerColors.BLUE);
        Trucker trucker2=new Trucker("frenk2", PlayerColors.RED);
        ArrayList<String> truckers=new ArrayList<>();
        truckers.add(trucker1.getNickname());
        truckers.add(trucker2.getNickname());
        LobbyEvent le=new LobbyEvent( truckers, 10);
        System.out.println(le.getMessage());
    }
}
