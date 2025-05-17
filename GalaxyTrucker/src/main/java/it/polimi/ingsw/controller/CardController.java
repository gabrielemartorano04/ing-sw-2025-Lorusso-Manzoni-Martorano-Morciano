package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cardcontrollers.CardControllerInterface;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;

import java.util.ArrayList;

public class CardController implements CardControllerInterface {
    private Card currentCard;
    private GameController gameController;
    private ArrayList<TruckerController> players;
    private int currentTurnIndex = 0;
    private Trucker currentTrucker;

    public CardController() {
    }

    public void setGame(GameController game, ArrayList<ClientHandler> list) {
        this.gameController = game;
        this.players = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            players.add(list.get(i).getTruckerController());
        }
        currentTrucker = this.players.getFirst().getTrucker();
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public Card getCurrCard() {
        return currentCard;
    }

    public GameController getGameController() {
        return gameController;
    }

    public Trucker getCurrentTrucker() {
        return currentTrucker;
    }

    public void setCurrentTrucker(Trucker currentTrucker) {
        this.currentTrucker = currentTrucker;
    }

    public ServerEvent startCard(){
        return currentCard.startCard(currentTrucker.getNickname());
    }

    /**
     * @param t: the trucker that is currently sending an event that needs to respect the turn
     * @return if it is the turn of the client sending the event
     */

    public boolean isPlayerTurn(Trucker t) {
        return this.currentTrucker.equals(t);
    }

    /**
     * sets the new index corresponding to the turn of the current Player
     */

    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
    }

    /**
     * resets the turn index
     */

    public void resetTurnIndex() {
        currentTurnIndex = 0;
    }

}
