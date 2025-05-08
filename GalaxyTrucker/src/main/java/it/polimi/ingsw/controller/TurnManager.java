package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.server.ClientHandlerServer;

import java.util.ArrayList;

/**
 * class that handles the fact that when the game is in state "FLIGHT" the game evolves with players that can send event to the server
 * only in their turn.
 */

public class TurnManager {
    private final ArrayList<ClientHandlerServer> players;
    private int currentTurnIndex = 0;

    public TurnManager(ArrayList<ClientHandlerServer> players) {
        this.players = new ArrayList<>(players);
    }


    public ClientHandlerServer getCurrentClient() {
        return players.get(currentTurnIndex);
    }

    public Trucker getCurrentTrucker() {
        return getCurrentClient().getTrucker();
    }

    public boolean isPlayerTurn(ClientHandlerServer client) {
        return getCurrentClient().equals(client);
    }

    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
    }

    public void reorderPlayersByPosition() {
        players.sort((a, b) -> Integer.compare(a.getTrucker().getPosition(), b.getTrucker().getPosition()));
        currentTurnIndex = 0; // reset del turno
    }

    public void resetTurnIndex() {
        currentTurnIndex = 0;
    }

}
