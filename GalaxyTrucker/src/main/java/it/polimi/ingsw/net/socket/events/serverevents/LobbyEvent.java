package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;

import java.util.ArrayList;

public class LobbyEvent extends ServerEvent {
    public String message;
    private ArrayList<String> truckers;
    public LobbyEvent( ArrayList<String> truckers) {
        super(ServerEventType.LOBBY, true);
        message = "Current players: " + truckers;
        this.truckers = truckers;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<String> getTruckers() {
        return truckers;
    }

    public static void main(String[] args) {
        Trucker trucker1=new Trucker("frenk", PlayerColors.BLUE);
        Trucker trucker2=new Trucker("manzo", PlayerColors.RED);
        ArrayList<String> truckers=new ArrayList<>();
        truckers.add(trucker1.getNickname());
        truckers.add(trucker2.getNickname());
        LobbyEvent le=new LobbyEvent( truckers);
        System.out.println(le.getMessage());
    }
}
