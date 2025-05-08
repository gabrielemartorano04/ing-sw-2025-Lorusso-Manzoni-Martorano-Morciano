package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.server.ClientHandlerServer;

import java.util.ArrayList;

/**
 * class that handles the fact that when the game is in state "FLIGHT" the game evolves with players that can send event to the server
 * only in their turn.
 */

public class TurnManager {
    private final ArrayList<PlayerContext> players;
    private int currentTurnIndex = 0;

    public TurnManager(ArrayList<PlayerContext> players) {
        this.players = new ArrayList<>(players);
    }

    public PlayerContext getCurrentPlayerContext() {
        return players.get(currentTurnIndex);
    }

    public ClientHandlerServer getCurrentClient() {
        return getCurrentPlayerContext().getClient();
    }

    public Trucker getCurrentTrucker() {
        return getCurrentPlayerContext().getTrucker();
    }

    public boolean isPlayerTurn(ClientHandlerServer client) {
        return getCurrentClient().equals(client);
    }

    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
    }
}
