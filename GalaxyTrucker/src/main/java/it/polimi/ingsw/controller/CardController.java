package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cardcontrollers.CardControllerInterface;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

public class CardController implements CardControllerInterface {
    private Card currentCard;
    private Game game;
    private Trucker currentTrucker;

    public CardController() {
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public Card getCurrCard() {
        return currentCard;
    }

    public Game getGame() {
        return game;
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
}
