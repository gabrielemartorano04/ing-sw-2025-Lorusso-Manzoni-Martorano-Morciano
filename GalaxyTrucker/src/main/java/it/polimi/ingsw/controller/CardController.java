package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cardcontrollers.CardControllerInterface;
import it.polimi.ingsw.model.cards.Card;

public class CardController implements CardControllerInterface {
    private Card currentCard;
    private Game game;
    private int numPlayers;
    private Trucker currentTrucker;

    public CardController(Card card, Game game) {
        this.currentCard = card;
        this.game = game;
        this.numPlayers = game.getTruckerList().size();
    }

    public Card getCurrCard() {
        return currentCard;
    }

    public Game getGame() {
        return game;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public Trucker getCurrentTrucker() {
        return currentTrucker;
    }

    public void setCurrentTrucker(Trucker currentTrucker) {
        this.currentTrucker = currentTrucker;
    }
}
