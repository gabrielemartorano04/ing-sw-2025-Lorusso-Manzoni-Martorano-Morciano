package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;

public class CardControllerNew {
    private Card currentCard;
    private Game game;

    public CardControllerNew(Card card, Game game) {
        this.currentCard = card;
        this.game = game;
    }

    public Card getCurrCard() {
        return currentCard;
    }

    public Game getGame() {
        return game;
    }
}
