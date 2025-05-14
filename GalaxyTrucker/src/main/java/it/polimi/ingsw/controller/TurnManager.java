package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;

import java.util.ArrayList;

/**
 * class that handles the fact that when the game is in state "FLIGHT" the game evolves with players that can send event to the server
 * only in their turn.
 */

public class TurnManager {
    private final ArrayList<ClientHandlerSocket> players;
    private int currentTurnIndex = 0;

    public TurnManager(ArrayList<ClientHandlerSocket> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * @return the ClientHandlerServer of the current player
     */
    public ClientHandlerSocket getCurrentClient() {
        return players.get(currentTurnIndex);
    }

    /**
     * @return the current player
     */
    public TruckerController getCurrentTrucker() {
        return getCurrentClient().getTruckerController();
    }

    /**
     * @param client: the client that is currently sending an event that needs to respect the turn
     * @return if it is the turn of the client sending the event
     */

    public boolean isPlayerTurn(ClientHandlerSocket client) {
        return getCurrentClient().equals(client);
    }

    /**
     * sets the new index corresponding to the turn of the current Player
     */

    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
    }

    /**
     * reorders the list based on the position of the players
     */

    public void reorderPlayersByPosition() {
        players.sort((a, b) -> Integer.compare(a.getTruckerController().getPosition(), b.getTruckerController().getPosition()));
        currentTurnIndex = 0; // reset del turno
    }

    /**
     * resets the turn index
     */

    public void resetTurnIndex() {
        currentTurnIndex = 0;
    }
    }
