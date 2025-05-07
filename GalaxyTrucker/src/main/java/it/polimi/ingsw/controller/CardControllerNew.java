package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;

public class CardControllerNew {
    private Card currentCard;
    private Game game;
    private int numPlayers;
    private Trucker currentTrucker;

    public CardControllerNew(Card card, Game game) {
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

    public Trucker getCurrentTrucker() {
        return currentTrucker;
    }
}
